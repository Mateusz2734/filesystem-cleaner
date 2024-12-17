package pl.edu.agh.to2.cleaner.model;

import jakarta.persistence.*;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;

@Entity
@Table(name = FileInfo.TABLE_NAME)
public class FileInfo {
    public static final String TABLE_NAME = "file_info";
    @Column(name = Columns.NAME, nullable = false /*, length = 255, unique = false <-- default */)
    private String name;
    @Id
    @Column(name = Columns.PATH, nullable = false, unique = true)
    private String path;
    @Column(name = Columns.SIZE, nullable = false)
    private Long size;
    @Column(name = Columns.MODIFICATION_TIME)
    private Long modificationTimeMS;
    @Column(name = Columns.CREATION_TIME)
    private Long creationTimeMS;
    @Column(name = Columns.EMBEDDING)
    private Float[] embedding;

    public FileInfo() {
    }

    public FileInfo(File file) {
        this.path = FilenameUtils.separatorsToUnix(file.getAbsolutePath());
        this.name = file.getName();
        this.size = file.length();
        try {
            BasicFileAttributes attr = Files.readAttributes(file.toPath(), BasicFileAttributes.class);
            this.modificationTimeMS = attr.lastModifiedTime().to(TimeUnit.MILLISECONDS);
            this.creationTimeMS = attr.creationTime().to(TimeUnit.MILLISECONDS);
        } catch (IOException e) {
            this.modificationTimeMS = 0L;
            this.creationTimeMS = 0L;
        }
    }

    public FileInfo(Path path) {
        this(path.toFile());
    }

    // Should be used only for testing purposes
    public FileInfo(String path, String name, Long size, Long modificationTimeMS, Long creationTimeMS) {
        this.path = path;
        this.name = name;
        this.size = size;
        this.modificationTimeMS = modificationTimeMS;
        this.creationTimeMS = creationTimeMS;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmbedding(Float[] embedding) {
        this.embedding = embedding;
    }

    public Float[] getEmbedding() {
        return embedding;
    }

    public Path toPath() {
        return Paths.get(path);
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Long getSize() {
        return size;
    }

    public Long getModificationTimeMS() {
        return modificationTimeMS;
    }

    public Long getCreationTimeMS() {
        return creationTimeMS;
    }

    public String describe() {
        return "File: " + path + " | Extension: " + FilenameUtils.getExtension(path) + " | Size: " + size + " B | Modification time: " + formatTime(modificationTimeMS) + " | Creation time: " + formatTime(creationTimeMS);
    }

    @Override
    public String toString() {
        return getPath();
    }

    private String formatTime(Long time) {
        FileTime fileTime = FileTime.from(time, TimeUnit.MILLISECONDS);
        LocalDateTime localDateTime = fileTime.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

        return localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    public static class Columns {
        public static final String NAME = "name";
        public static final String PATH = "path";
        public static final String SIZE = "size";
        public static final String MODIFICATION_TIME = "modification_time";
        public static final String CREATION_TIME = "creation_time";
        public static final String EMBEDDING = "embedding";
    }
}