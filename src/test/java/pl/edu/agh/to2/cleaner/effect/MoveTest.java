package pl.edu.agh.to2.cleaner.effect;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.edu.agh.to2.cleaner.model.FileInfo;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MoveTest {

    private File tempDirectorySource;
    private File tempDirectoryDestination;

    @BeforeEach
    public void setUp() throws IOException {
        tempDirectorySource = Files.createTempDirectory("sourceDir").toFile();
        tempDirectoryDestination = Files.createTempDirectory("destinationDir").toFile();
    }

    @AfterEach
    public void cleanUp() {
        if (tempDirectorySource.exists()) {
            deleteDirectory(tempDirectorySource);
        }
        if (tempDirectoryDestination.exists()) {
            deleteDirectory(tempDirectoryDestination);
        }
    }

    private void deleteDirectory(File directory) {
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    deleteDirectory(file);
                } else {
                    file.delete();
                }
            }
        }
        directory.delete();
    }

    @Test
    public void testMoveSingle() throws IOException {
        File tempFile = File.createTempFile("testFile", ".txt", tempDirectorySource);
        FileInfo fileInfo = new FileInfo(tempFile);

        Move move = new Move(fileInfo, tempDirectoryDestination.getAbsolutePath());
        move.apply();

        File movedFile = new File(tempDirectoryDestination, tempFile.getName());
        assertFalse(tempFile.exists());
        assertTrue(movedFile.exists());
    }

    @Test
    public void testMoveList() throws IOException {
        List<FileInfo> filesToMove = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            File tempFile = File.createTempFile("testFile" + i, ".txt", tempDirectorySource);
            filesToMove.add(new FileInfo(tempFile));
        }

        Move move = new Move(filesToMove, tempDirectoryDestination.getAbsolutePath());
        move.apply();

        for (FileInfo fileInfo : filesToMove) {
            File originalFile = new File(fileInfo.getPath());
            File movedFile = new File(tempDirectoryDestination, originalFile.getName());

            assertFalse(originalFile.exists());
            assertTrue(movedFile.exists());
        }
    }
}