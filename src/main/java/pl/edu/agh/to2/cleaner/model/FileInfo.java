package pl.edu.agh.to2.cleaner.model;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;

public class FileInfo {
    private String name;
    private Path path;
    private long size;
    private FileTime modificationTime;
    private FileTime creationTime;

    public FileInfo(String name, String path, long size, FileTime modificationTime, FileTime creationTime) {
        this.path = Paths.get(path);
        this.name = name; // TODO: extract name from path
        this.size = size;
        this.modificationTime = modificationTime;
        this.creationTime = creationTime;

        // extracting name from path
//        File file = new File(path);
//        String extractedName = file.getName();
    }

    public FileInfo(File file) throws IOException {
        this.path = file.toPath();
        this.name = file.getName();
        this.size = file.length();
        BasicFileAttributes attr =
                Files.readAttributes(file.toPath(), BasicFileAttributes.class);
        this.modificationTime = attr.lastModifiedTime();
        this.creationTime = attr.creationTime();
    }

    public String getName() {
        return name;
    }

    public Path getPath() {
        return path;
    }

    public long getSize() {
        return size;
    }

    public FileTime getModificationTime() {
        return modificationTime;
    }

    public FileTime getCreationTime() {
        return creationTime;
    }
}