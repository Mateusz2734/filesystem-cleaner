package pl.edu.agh.to2.cleaner.command;

import org.apache.commons.collections4.SetUtils;
import pl.edu.agh.to2.cleaner.model.FileContentEmbedder;
import pl.edu.agh.to2.cleaner.model.FileInfo;
import pl.edu.agh.to2.cleaner.repository.FileInfoRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

public class FileTreeIndexer {
    private final FileInfoRepository repository;

    public FileTreeIndexer(FileInfoRepository repository) {
        this.repository = repository;
    }

    // rootPath must be a valid path to a directory
    public void index(Path rootPath) throws IOException {
        Map<Path, FileInfo> dbFilesMap = repository.getDescendants(rootPath).stream().collect(Collectors.toMap(FileInfo::toPath, info -> info));
        var dbFilesPaths = dbFilesMap.keySet();

        HashSet<Path> fsFiles;
        try (var stream = Files.walk(rootPath)) {
            fsFiles = new HashSet<>(stream.map(Path::toAbsolutePath).filter(Files::isRegularFile).toList());
        }

        // Files that are in DB but not in FS (should be removed)
        SetUtils.difference(dbFilesPaths, fsFiles).forEach(path -> repository.remove(dbFilesMap.get(path)));

        // Files that are in both DB and FS (should be updated)
        var toEmbed = SetUtils.intersection(dbFilesPaths, fsFiles).stream().filter(path -> !same(dbFilesMap.get(path), path)).toList();

        // Files that are in FS but not in DB (should be added)
        var toAdd = SetUtils.difference(fsFiles, dbFilesPaths).toSet();
        toAdd.addAll(toEmbed);
        FileContentEmbedder.embedFiles(toAdd.stream().map(FileInfo::new).toList()).forEach(repository::add);
    }

    private boolean same(FileInfo dbInfo, Path fsPath) {
        try {
            var fsInfo = new FileInfo(fsPath);

            return dbInfo.getSize().equals(fsInfo.getSize()) && dbInfo.getCreationTimeMS().equals(fsInfo.getCreationTimeMS()) && dbInfo.getModificationTimeMS().equals(fsInfo.getModificationTimeMS()) && dbInfo.getChecksum().equals(FileContentEmbedder.checksum(Files.readAllBytes(fsPath)));
        } catch (IOException e) {
            return false;
        }
    }
}
