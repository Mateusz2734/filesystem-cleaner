package pl.edu.agh.to2.cleaner.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import pl.edu.agh.to2.cleaner.command.FileDuplicateFinder;
import pl.edu.agh.to2.cleaner.command.FileGroupFinder;
import pl.edu.agh.to2.cleaner.command.FileVersionsFinder;
import pl.edu.agh.to2.cleaner.command.UnionFind;
import pl.edu.agh.to2.cleaner.controller.FileManipulationController;
import pl.edu.agh.to2.cleaner.effect.Delete;
import pl.edu.agh.to2.cleaner.model.FileInfo;
import pl.edu.agh.to2.cleaner.repository.FileInfoRepository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

    public void move(List<String> filenames, String destination) {
//        TODO
    }

    public void archive(List<String> filenames, String destination) {
//        TODO
    }

    public List<FileManipulationController.FileGroup> groupFiles(String rootPath) {
        List<FileManipulationController.FileGroup> groups = new ArrayList<>();

        var files = repository.getDescendants(rootPath);

        // Process file versions
        processFileGroups(new FileVersionsFinder(files), "Versions", groups);

        // Process file duplicates
        processFileGroups(new FileDuplicateFinder(files), "Duplicates", groups);

        return groups;
    }

    private void processFileGroups(
            FileGroupFinder finder,
            String groupName,
            List<FileManipulationController.FileGroup> groups) {

        var connections = finder.find();
        new UnionFind().connectedComponentsFromEdges(connections).forEach(group -> {
            var metadataList = group.stream()
                    .map(file -> new FileManipulationController.FileMetadata(
                            file.getPath(),
                            file.getSize(),
                            file.getCreationTimeMS(),
                            file.getModificationTimeMS()))
                    .toList();

            groups.add(new FileManipulationController.FileGroup(groupName, metadataList));
        });
    }
}
