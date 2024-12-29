package pl.edu.agh.to2.cleaner.effect;

import pl.edu.agh.to2.cleaner.model.FileInfo;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Rename implements IOSideEffect {
    private final FileInfo fileInfo;
    private final String newFileName;

    public Rename(FileInfo fileInfo, String newFileName) {
        this.fileInfo = fileInfo;
        this.newFileName = newFileName;
    }

    @Override
    public void apply() throws IOException {
        Path originalPath = fileInfo.toPath();
        File originalFile = originalPath.toFile();

        if (!originalFile.exists()) {
            throw new IOException("File does not exist: " + originalFile.getAbsolutePath());
        }

        Path renamedPath = originalPath.resolveSibling(newFileName);

        if (Files.exists(renamedPath)) {
            throw new IOException("File with the new name already exists: " + renamedPath.toAbsolutePath());
        }

        // Zmiana nazwy
        if (!originalFile.renameTo(renamedPath.toFile())) {
            throw new IOException("Failed to rename file: " + originalFile.getAbsolutePath());
        }

        // Aktualizacja danych
        fileInfo.setName(newFileName);
        fileInfo.setPath(renamedPath.toString());
    }

    @Override
    public String getLogString() {
        return null;
    }
}