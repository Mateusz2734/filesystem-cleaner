package pl.edu.agh.to2.cleaner.controller;

import org.h2.store.fs.FileUtils;
import org.springframework.web.bind.annotation.*;
import pl.edu.agh.to2.cleaner.service.FileManipulationService;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/file")
public class FileManipulationController {
    private final FileManipulationService fileService;

    public FileManipulationController(FileManipulationService fileService) {
        this.fileService = fileService;
    }

    @PostMapping("/move")
    public String move(@RequestBody List<String> files) {
        return "Received: " + String.join(", ", files);
    }

    @PostMapping("/archive")
    public String archive(@RequestBody List<String> files) {
        return "Received: " + String.join(", ", files);
    }

    @PostMapping("/delete")
    public void delete(@RequestBody List<String> files) {
        fileService.delete(files);
    }

    @GetMapping("/groups")
    public List<FileGroup> groupFiles(@RequestParam String rootPath) {


        return fileService.groupFiles(rootPath);
//        return List.of(
//                new FileGroup("Large files", List.of(
//                        new FileMetadata("path/to/file1", 1024, 123L, 123L),
//                        new FileMetadata("path/to/file2", 2048, 123324234L, 123L)
//                )),
//                new FileGroup("Old files", List.of(
//                        new FileMetadata("path/to/file3", 1024, 123L, 123L),
//                        new FileMetadata("path/to/file4", 2048, 123L, 123L)
//                ))
//        );
    }

    public record FileMetadata(String path, long size, Long createdAt, Long modifiedAt) {}
    public record FileGroup(String reason, List<FileMetadata> files) {}
}
