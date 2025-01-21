package pl.edu.agh.to2.cleaner.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.commons.io.file.PathUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import pl.edu.agh.to2.cleaner.context.FileIndexContext;
import pl.edu.agh.to2.cleaner.service.FileIndexService;

import java.nio.file.Path;

@RestController
@RequestMapping("/api/index")
public class FileIndexController {
    private final FileIndexService fileIndexService;
    private final FileIndexContext fileIndexContext;

    public FileIndexController(FileIndexService fileIndexService, FileIndexContext fileIndexContext) {
        this.fileIndexService = fileIndexService;
        this.fileIndexContext = fileIndexContext;
    }

    @GetMapping(value = "/progress", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter streamProgress() {
        try {
            SseEmitter emitter = new SseEmitter();
            fileIndexService.registerEmitter(emitter);
            return emitter;
        } catch (Exception e) {
            System.err.println("Failed to establish SSE connection: " + e.getMessage());
            return null;
        }
    }

    @PostMapping("/start")
    public synchronized  ResponseEntity<String> startIndexing(@RequestBody Request dto) throws JsonProcessingException {
        if (fileIndexContext.isIndexing()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(errorMap("Indexing is already in progress."));
        }

        Path rootPath;
        try {
            rootPath = Path.of(dto.root());
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(errorMap("Root must be a valid path."));
        }

        if (!PathUtils.isDirectory(rootPath)) {
            return ResponseEntity.badRequest().body(errorMap("Root must be a valid directory."));
        }

        fileIndexContext.setIndexing(true);
        fileIndexService.performIndexing(rootPath);
        return ResponseEntity.ok("{}");
    }

    private record Request(String root) {}

    private String errorMap(String error) {
        return "{\"error\": \"" + error + "\"}";
    }
}
