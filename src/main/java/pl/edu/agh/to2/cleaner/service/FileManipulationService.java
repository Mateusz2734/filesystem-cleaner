package pl.edu.agh.to2.cleaner.service;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import pl.edu.agh.to2.cleaner.command.FileDuplicateFinder;
import pl.edu.agh.to2.cleaner.command.FileGroupFinder;
import pl.edu.agh.to2.cleaner.command.FileVersionsFinder;
import pl.edu.agh.to2.cleaner.command.UnionFind;
import pl.edu.agh.to2.cleaner.controller.FileManipulationController;
import pl.edu.agh.to2.cleaner.effect.Archive;
import pl.edu.agh.to2.cleaner.effect.Delete;
import pl.edu.agh.to2.cleaner.effect.Move;
import pl.edu.agh.to2.cleaner.model.EmbeddingServerClient;
import pl.edu.agh.to2.cleaner.model.FileContentEmbedder;
import pl.edu.agh.to2.cleaner.model.FileInfo;
import pl.edu.agh.to2.cleaner.repository.FileInfoRepository;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@Service
public class FileManipulationService {
    private static final Logger logger = LoggerFactory.getLogger(FileManipulationService.class);
    private final FileInfoRepository repository;

    public FileManipulationService(FileInfoRepository repository) {
        this.repository = repository;
    }

    public void delete(List<String> filenames) {
        for (String filename : filenames) {
            try {
                var optFile = repository.findByPath(filename);
                if (optFile.isEmpty()) {
                    continue;
                }

                var file = optFile.get();

                new Delete(file).apply();

                repository.remove(file);

                logger.info("DELETE|{}|", file.getPath());
            } catch (IOException ignored) {
            }
        }
    }


    //    TODO: Rethink code below
    public void move(List<String> filenames, String destination) {
        for (String filename : filenames) {
            try {
                var optFile = repository.findByPath(filename);
                if (optFile.isEmpty()) {
                    continue;
                }

                FileInfo file = optFile.get();

                var resolved = resolveUniqueDestinationPath(destination, file.getName());

                if (repository.changePathAndName(file, resolved.getLeft(), resolved.getRight()).isPresent()) {
                    new Move(file, destination).apply();

                    logger.info("MOVE|{}|{}", filename, resolved.getLeft());
                }
            } catch (IOException ignored) {
            }
        }
    }

    public void archive(List<String> paths, String destination) {
        List<FileInfo> filesToArchive = new ArrayList<>();

        for (String path : paths) {
            var optFile = repository.findByPath(path);
            if (optFile.isEmpty()) {
                continue;
            }
            filesToArchive.add(optFile.get());
        }

        Map<String, Integer> nameCounts = filesToArchive.stream().collect(HashMap::new, (map, file) -> map.merge(file.getName(), 1, Integer::sum), HashMap::putAll);

        try {
            List<FileInfo> resolvedFiles = resolveUniqueNames(filesToArchive, destination);
            new Archive(resolvedFiles, destination).apply();

            for (FileInfo file : resolvedFiles) {
                repository.remove(file);
                logger.info("ARCHIVE|{}|{}", file.getPath(), destination + "/compressed.zip");
            }
        } catch (IOException ignored) {
        }
    }

    public Pair<String, String> resolveUniqueDestinationPath(String destinationDirectory, String originalFileName) {
        Path resolvedPath = Paths.get(destinationDirectory).resolve(originalFileName);
        String resolvedPathString = resolvedPath.toString();
        int counter = 0;

        while (repository.findByPath(resolvedPathString).isPresent()) {
            counter++;
            resolvedPathString = appendNameSuffix(resolvedPathString, counter);
        }

        Path resolvedPathObj = Paths.get(resolvedPathString);
        String resolvedFileName = resolvedPathObj.getFileName().toString();

        return new ImmutablePair<>(resolvedPathObj.toString(), resolvedFileName);
    }

    private List<FileInfo> resolveUniqueNames(List<FileInfo> files, String destinationDirectory) {
        List<FileInfo> resolvedFiles = new ArrayList<>();

        for (FileInfo file : files) {
            // Resolve unique path for each file
            var resolved = resolveUniqueDestinationPath(destinationDirectory, file.getName());

            // Update the file's path and name in the repository
            repository.changePathAndName(file, resolved.getLeft(), resolved.getRight());

            resolvedFiles.add(file);
        }

        return resolvedFiles;
    }

    public String appendNameSuffix(String originalPath, int counter) {
        Path path = Paths.get(originalPath);
        String fileName = path.getFileName().toString();

        int dotIndex = fileName.lastIndexOf('.');
        String suffix = "_" + counter;

        if (dotIndex != -1) {
            fileName = fileName.substring(0, dotIndex) + suffix + fileName.substring(dotIndex);
        } else {
            fileName += suffix;
        }

        return FilenameUtils.separatorsToUnix(path.getParent().resolve(fileName).toString());
    }
//    TODO: End of code to Rethink

    public List<FileManipulationController.FileMetadata> search(String query, String rootPath) {
        var embedding = EmbeddingServerClient.fetchEmbedding(query);

        if (embedding == null) {
            return null;
        }

        var files = repository.getDescendants(rootPath);

        return files.stream().map(file -> new Object() {
            final double similarity = FileContentEmbedder.cosineSimilarity(file.getEmbedding(), embedding);
            final FileInfo info = file;
        }).sorted((a, b) -> Double.compare(b.similarity, a.similarity)).limit(2).map(obj -> FileManipulationController.FileMetadata.fromFileInfo(obj.info)).toList();
    }

    public List<FileManipulationController.FileGroup> groupFiles(String rootPath) {
        List<FileManipulationController.FileGroup> groups = new ArrayList<>();

        var files = repository.getDescendants(rootPath);

        groups.add(createFileGroup("Largest", repository.getLargestFiles(rootPath, 5)));
        processFileGroups(new FileVersionsFinder(files), "Versions", groups);
        processFileGroups(new FileDuplicateFinder(files), "Duplicates", groups);

        return groups;
    }

    private void processFileGroups(FileGroupFinder finder, String groupName, List<FileManipulationController.FileGroup> groups) {
        var connections = finder.find();
        UnionFind.connectedComponentsFromEdges(connections).forEach(group -> groups.add(createFileGroup(groupName, group)));
    }

    private FileManipulationController.FileGroup createFileGroup(String groupName, Collection<FileInfo> files) {
        List<FileManipulationController.FileMetadata> metadataList = files.stream().map(file -> new FileManipulationController.FileMetadata(file.getPath(), file.getSize(), file.getCreationTimeMS(), file.getModificationTimeMS())).toList();

        return new FileManipulationController.FileGroup(groupName, metadataList);
    }
}
