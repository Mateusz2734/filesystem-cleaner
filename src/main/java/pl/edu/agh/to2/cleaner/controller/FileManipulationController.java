package pl.edu.agh.to2.cleaner.controller;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.edu.agh.to2.cleaner.command.PathValidator;
import pl.edu.agh.to2.cleaner.service.FileManipulationService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/file")
public class FileManipulationController {
    private final FileManipulationService fileService;

    public FileManipulationController(FileManipulationService fileService) {
        this.fileService = fileService;
    }

    @PostMapping("/move")
    public ResponseEntity<ActionResponse> move(@RequestBody MoveRequest moveRequest) {
        if (StringUtils.isBlank(moveRequest.getDestination())) {
            return ResponseEntity.badRequest()
                    .body(new ActionResponse("Destination path cannot be empty"));
        }

        try {
            var destination = FilenameUtils.separatorsToUnix(moveRequest.getDestination());
            fileService.move(moveRequest.getFiles(),destination);
            return ResponseEntity.ok(new ActionResponse("Files moved successfully"));
        } catch (Exception e) {
            log.error("Error moving files", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ActionResponse("Failed to move files: " + e.getMessage()));
        }
    }

    @PostMapping("/archive")
    public ResponseEntity<ActionResponse> archive(@RequestBody MoveRequest archiveRequest) {
        if (StringUtils.isBlank(archiveRequest.getDestination())) {
            return ResponseEntity.badRequest()
                    .body(new ActionResponse("Destination path cannot be empty"));
        }

        try {
            var destination = FilenameUtils.separatorsToUnix(archiveRequest.getDestination());
            fileService.archive(archiveRequest.getFiles(), destination);
            return ResponseEntity.ok(new ActionResponse("Files archived successfully"));
        } catch (Exception e) {
            log.error("Error archiving files", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ActionResponse("Failed to archive files: " + e.getMessage()));
        }
    }

    @PostMapping("/delete")
    public ResponseEntity<ActionResponse> delete(@RequestBody List<String> files) {
        try {
            fileService.delete(files);
            return ResponseEntity.ok(new ActionResponse("Files deleted successfully"));
        } catch (Exception e) {
            log.error("Error deleting files", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ActionResponse("Failed to delete files: " + e.getMessage()));
        }
    }

    @GetMapping("/groups")
    public ResponseEntity<List<FileGroup>> groupFiles(@RequestParam String rootPath) {
        if (StringUtils.isBlank(rootPath)) {
            return ResponseEntity.badRequest().build();
        }

        var pathValidationResult = PathValidator.validateDirectoryPath(rootPath);
        if (!pathValidationResult.isValid()) {
            log.warn("Invalid root path for grouping: {}", rootPath);
            return ResponseEntity.badRequest().build();
        }

        try {
            List<FileGroup> groups = fileService.groupFiles(rootPath);

            if (groups.isEmpty()) {
                return ResponseEntity.noContent().build();
            }

            return ResponseEntity.ok(groups);
        } catch (Exception e) {
            log.error("Error grouping files", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/search")
    public ResponseEntity<?> search(@RequestParam String rootPath, @RequestParam String query) {
        if (StringUtils.isBlank(rootPath) || StringUtils.isBlank(query)) {
            return ResponseEntity.badRequest()
                    .body(new ErrorResponse("Root path and query must not be empty"));
        }

        var pathValidationResult = PathValidator.validateDirectoryPath(rootPath);
        if (!pathValidationResult.isValid()) {
            log.warn("Invalid root path for search: {}", rootPath);
            return ResponseEntity.badRequest()
                    .body(new ErrorResponse(pathValidationResult.message()));
        }

        try {
            List<FileMetadata> searchResults = fileService.search(query, rootPath);

            if (searchResults == null || searchResults.isEmpty()) {
                return ResponseEntity.noContent().build();
            }

            return ResponseEntity.ok(searchResults);
        } catch (Exception e) {
            log.error("Error searching files", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("An unexpected error occurred during search"));
        }
    }

    @Data
    public static class MoveRequest {
        private List<String> files;
        private String destination;
    }

    public record FileMetadata(String path, long size, Long createdAt, Long modifiedAt) {
        public static FileMetadata fromFileInfo(pl.edu.agh.to2.cleaner.model.FileInfo info) {
            return new FileMetadata(info.getPath(), info.getSize(), info.getCreationTimeMS(), info.getModificationTimeMS());
        }
    }

    public record FileGroup(String reason, List<FileMetadata> files) {}

    @Data
    @AllArgsConstructor
    private static class ErrorResponse {
        private String message;
    }

    @Data
    @AllArgsConstructor
    private static class ActionResponse {
        private String message;
    }
}