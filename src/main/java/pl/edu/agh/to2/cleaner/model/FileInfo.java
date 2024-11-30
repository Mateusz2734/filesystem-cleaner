package pl.edu.agh.to2.cleaner.model;

import jakarta.persistence.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.concurrent.TimeUnit;

@Entity
@Table(name = FileInfo.TABLE_NAME)
public class FileInfo {
    public static final String TABLE_NAME = "file_info";

    public static class Columns {
        public static final String ID = "id";
        public static final String NAME = "name";
        public static final String PATH = "path";
        public static final String SIZE = "size";
        public static final String MODIFICATION_TIME = "modification_time";
        public static final String CREATION_TIME = "creation_time";

    }

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    @Column(name = Columns.ID)
    private Long id;

    @Column(name = Columns.NAME, nullable = false /*, length = 255, unique = false <-- default */)
    private String name;

    @Column(name = Columns.PATH, nullable = false, unique = true)
    private String path;

    @Column(name = Columns.SIZE, nullable = false)
    private Long size;

    @Column(name = Columns.MODIFICATION_TIME)
    private Long modificationTimeMS;

    @Column(name = Columns.CREATION_TIME)
    private Long creationTimeMS;

    public FileInfo(String path, String name, long size, Long modificationTimeMS, Long creationTimeMS) {
        this.path = path;
        this.name = name; // TODO: extract name from path
        this.size = size;
        this.modificationTimeMS = modificationTimeMS;
        this.creationTimeMS = creationTimeMS;

        // extracting name from path
//        File file = new File(path);
//        String extractedName = file.getName();
    }

    public FileInfo(File file) throws IOException {
        this.path = file.toPath().toString();
        this.name = file.getName();
        this.size = file.length();
        BasicFileAttributes attr =
                Files.readAttributes(file.toPath(), BasicFileAttributes.class);
        this.modificationTimeMS = attr.lastModifiedTime().to(TimeUnit.MILLISECONDS);
        this.creationTimeMS = attr.creationTime().to(TimeUnit.MILLISECONDS);
    }

    public FileInfo() {

    }

    public String getName() {
        return name;
    }

    public Path toPath() {
        return Paths.get(path);
    }

    public String getPath() {
        return path;
    }

    public long getSize() {
        return size;
    }

    public Long getModificationTimeMS() {
        return modificationTimeMS;
    }

    public Long getCreationTimeMS() {
        return creationTimeMS;
    }
}