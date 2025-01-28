package pl.edu.agh.to2.cleaner.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import pl.edu.agh.to2.cleaner.command.PathValidator;
import pl.edu.agh.to2.cleaner.context.FileIndexContext;
import pl.edu.agh.to2.cleaner.service.FileIndexService;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
@RestController
@RequestMapping("/api/index")
public class FileIndexController {
    private final FileIndexService fileIndexService;
    private final FileIndexContext fileIndexContext;

    private static final long SSE_TIMEOUT = TimeUnit.MINUTES.toMillis(5);

    public FileIndexController(FileIndexService fileIndexService, FileIndexContext fileIndexContext) {
        this.fileIndexService = fileIndexService;
        this.fileIndexContext = fileIndexContext;
    }

    @GetMapping(value = "/progress", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter streamProgress() {
        SseEmitter emitter = new SseEmitter(SSE_TIMEOUT);

        try {
            fileIndexService.registerEmitter(emitter);

            emitter.onCompletion(() -> log.info("SSE progress stream completed"));
            emitter.onTimeout(() -> {
                log.warn("SSE progress stream timed out after {} minutes", SSE_TIMEOUT / TimeUnit.MINUTES.toMillis(1));
                emitter.complete();
            });
            emitter.onError(e -> {
                log.error("SSE progress stream error", e);
                emitter.complete();
            });

            return emitter;
        } catch (Exception e) {
            log.error("Failed to establish SSE connection", e);
            SseEmitter failedEmitter = new SseEmitter(0L);
            failedEmitter.complete();
            return failedEmitter;
        }
    }

    @PostMapping("/start")
    public ResponseEntity<Map<String, String>> startIndexing(@RequestBody IndexingRequest request) {
        if (fileIndexContext.isIndexing()) {
            return createErrorResponse(
                    HttpStatus.CONFLICT,
                    "Indexing is already in progress."
            );
        }

        var response = PathValidator.validateDirectoryPath(request.root());

        if (!response.isValid()) {
            return createErrorResponse(
                    HttpStatus.BAD_REQUEST,
                    response.message()
            );
        }
        Path rootPath = Path.of(request.root()).toAbsolutePath();
        log.info("Starting file indexing for root path: {}", rootPath);

        try {
            fileIndexContext.setIndexing(true);
            fileIndexService.performIndexing(rootPath);

            return ResponseEntity.ok(Map.of("status", "Indexing started"));
        } catch (Exception e) {
            log.error("Error during file indexing", e);

            fileIndexContext.setIndexing(false);

            return createErrorResponse(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "An unexpected error occurred during indexing."
            );
        }
    }

    private ResponseEntity<Map<String, String>> createErrorResponse(HttpStatus status, String message) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", message);
        return ResponseEntity.status(status).body(errorResponse);
    }

    private record IndexingRequest(String root) {}
}