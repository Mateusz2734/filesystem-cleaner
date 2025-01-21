package pl.edu.agh.to2.cleaner.service;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import pl.edu.agh.to2.cleaner.context.FileIndexContext;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
public class FileIndexService {
    private final FileIndexContext fileIndexContext;
    private final List<SseEmitter> emitters = new CopyOnWriteArrayList<>();

    public FileIndexService(FileIndexContext fileIndexContext) {
        this.fileIndexContext = fileIndexContext;
    }

    public void registerEmitter(SseEmitter emitter) {
        this.emitters.add(emitter);
        emitter.onCompletion(() -> emitters.remove(emitter));
        emitter.onTimeout(() -> emitters.remove(emitter));
        emitter.onError((e) -> emitters.remove(emitter));
    }

    @Async
    public void performIndexing(Path root) {
        try {
            fileIndexContext.setIndexing(true);

            for (int i = 1; i <= 100; i++) {
                // Simulate work
                Thread.sleep(100);
                sendProgress(String.format("Progress: %d%%", i));
            }

            sendProgress("END");
            emitters.forEach(SseEmitter::complete);

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            sendProgress("Indexing interrupted.");
        } catch (Exception e) {
            sendProgress("An error occurred during indexing.");
        } finally {
            fileIndexContext.setIndexing(false);
        }
    }

    private void sendProgress(String message) {
        List<SseEmitter> failedEmitters = new ArrayList<>();
        for (SseEmitter emitter : emitters) {
            try {
                emitter.send(SseEmitter.event().data(message));
            } catch (IOException | IllegalStateException e) {
                failedEmitters.add(emitter);
            }
        }
        emitters.removeAll(failedEmitters);
    }
}


