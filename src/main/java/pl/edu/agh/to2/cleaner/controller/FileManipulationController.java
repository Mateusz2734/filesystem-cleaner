package pl.edu.agh.to2.cleaner.controller;

import org.springframework.web.bind.annotation.*;
import pl.edu.agh.to2.cleaner.service.FileManipulationService;

import java.util.List;

@RestController
@RequestMapping("/api")
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
}
