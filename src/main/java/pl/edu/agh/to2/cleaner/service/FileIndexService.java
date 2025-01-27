package pl.edu.agh.to2.cleaner.service;

import jakarta.transaction.Transactional;
import org.apache.commons.collections4.SetUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import pl.edu.agh.to2.cleaner.context.FileIndexContext;
import pl.edu.agh.to2.cleaner.model.FileContentEmbedder;
import pl.edu.agh.to2.cleaner.model.FileInfo;
import pl.edu.agh.to2.cleaner.repository.FileInfoRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

@Service
public class FileIndexService {
    private final FileInfoRepository repository;
    private final FileIndexContext fileIndexContext;
    private final List<SseEmitter> emitters = new CopyOnWriteArrayList<>();

    public FileIndexService(FileInfoRepository repository, FileIndexContext fileIndexContext) {
        this.repository = repository;
        this.fileIndexContext = fileIndexContext;
    }

    public void registerEmitter(SseEmitter emitter) {
        this.emitters.add(emitter);
        emitter.onCompletion(() -> emitters.remove(emitter));
        emitter.onTimeout(() -> emitters.remove(emitter));
        emitter.onError((e) -> emitters.remove(emitter));
    }

    // Centralized method to send progress updates
    private void sendProgress(String message) {
        List<SseEmitter> failedEmitters = new ArrayList<>();
        for (SseEmitter emitter : emitters) {
            try {
                emitter.send(SseEmitter.event().data(message));
            } catch (Exception e) {
                failedEmitters.add(emitter);
            }
        }
        emitters.removeAll(failedEmitters);
    }

    @Async
    @Transactional
    public void performIndexing(Path rootPath) {
        try {
            fileIndexContext.setIndexing(true);

            sendProgress("Starting file indexing...");

            sendProgress("Retrieving existing files...");
            Map<Path, FileInfo> dbFilesMap = repository.getDescendants(rootPath).stream().collect(Collectors.toMap(FileInfo::toPath, info -> info));
            var dbFilesPaths = dbFilesMap.keySet();

            sendProgress("Scanning file system...");
            HashSet<Path> fsFiles;
            try (var stream = Files.walk(rootPath)) {
                fsFiles = new HashSet<>(stream.map(Path::toAbsolutePath).filter(Files::isRegularFile).toList());
            }

            sendProgress("Identifying files to remove...");
            Set<Path> filesToRemove = SetUtils.difference(dbFilesPaths, fsFiles);
            filesToRemove.forEach(path -> repository.remove(dbFilesMap.get(path)));
            sendProgress(String.format("Removed %d files from index", filesToRemove.size()));

            sendProgress("Identifying files to update or add...");
            // Files that are in both DB and FS (should be updated)
            var toEmbed = SetUtils.intersection(dbFilesPaths, fsFiles).stream().filter(path -> !same(dbFilesMap.get(path), path)).toList();

            // Files that are in FS but not in DB (should be added)
            var toAdd = SetUtils.difference(fsFiles, dbFilesPaths).toSet();
            toAdd.addAll(toEmbed);

            List<FileInfo> filesToProcess = toAdd.stream().map(FileInfo::new).toList();
            int totalFiles = filesToProcess.size();
            sendProgress(String.format("Preparing to process %d files", totalFiles));

            for (int i = 0; i < filesToProcess.size(); i++) {
                FileInfo fileInfo = filesToProcess.get(i);
                repository.add(FileContentEmbedder.embedFiles(List.of(fileInfo)).get(0));

                if ((i + 1) % Math.max(1, totalFiles / 10) == 0 || i == totalFiles - 1) {
                    int progressPercentage = (int) (((i + 1) / (double) totalFiles) * 100);
                    sendProgress(String.format("Processed %d/%d files (%d%%)", i + 1, totalFiles, progressPercentage));
                }
            }

            try {
                Thread.sleep(100);
            } catch (InterruptedException ignored) {}

            sendProgress("Indexing complete");
            sendProgress("END");
            emitters.forEach(SseEmitter::complete);

        } catch (Exception e) {
            sendProgress("An error occurred during indexing: " + e.getMessage());
            emitters.forEach(SseEmitter::complete);
        } finally {
            fileIndexContext.setIndexing(false);
        }
    }

    // Helper method to check if file metadata has changed
    private boolean same(FileInfo dbInfo, Path fsPath) {
        try {
            var fsInfo = new FileInfo(fsPath);

            return dbInfo.getSize().equals(fsInfo.getSize()) && dbInfo.getCreationTimeMS().equals(fsInfo.getCreationTimeMS()) && dbInfo.getModificationTimeMS().equals(fsInfo.getModificationTimeMS()) && dbInfo.getChecksum().equals(FileContentEmbedder.checksum(Files.readAllBytes(fsPath)));
        } catch (IOException e) {
            return false;
        }
    }
}