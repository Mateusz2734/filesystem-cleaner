package pl.edu.agh.to2.cleaner.effect;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.edu.agh.to2.cleaner.model.FileInfo;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class RenameTest {

    private File tempFile;
    private String tempDirectoryPath;

    @BeforeEach
    void setUp() throws IOException {
        File tempDirectory = File.createTempFile("testDir", "");
        if (tempDirectory.delete() && tempDirectory.mkdir()) {
            tempDirectoryPath = tempDirectory.getAbsolutePath();
        }

        tempFile = File.createTempFile("testFile", ".txt", tempDirectory);
    }

    @AfterEach
    void cleanUp() {
        if (tempFile != null && tempFile.exists()) {
            tempFile.delete();
        }

        File tempDirectory = new File(tempDirectoryPath);
        if (tempDirectory.exists()) {
            for (File file : tempDirectory.listFiles()) {
                file.delete();
            }
            tempDirectory.delete();
        }
    }

    @Test
    void testRenameFile() throws Exception {
        FileInfo fileInfo = new FileInfo(tempFile);
        String newFileName = "renamedFile.txt";

        Rename renameEffect = new Rename(fileInfo, newFileName);
        renameEffect.apply();

        File renamedFile = new File(tempFile.getParent(), newFileName);
        assertTrue(renamedFile.exists());
        assertEquals(renamedFile.getName(), newFileName);
        assertFalse(tempFile.exists());
    }

    @Test
    void testRenameFile_FileDoesNotExist() {
        File nonExistentFile = new File(tempDirectoryPath, "nonExistent.txt");
        FileInfo fileInfo = new FileInfo(nonExistentFile);
        String newFileName = "newName.txt";

        Rename renameEffect = new Rename(fileInfo, newFileName);

        IOException exception = assertThrows(IOException.class, renameEffect::apply);
        assertTrue(exception.getMessage().contains("File does not exist"));
    }

    @Test
    void testRenameFile_NewNameAlreadyExists() throws Exception {
        FileInfo fileInfo = new FileInfo(tempFile);
        File existingFile = new File(tempFile.getParent(), "existingFile.txt");
        if (!existingFile.createNewFile()) {
            fail("Failed to create existing file for test");
        }

        Rename renameEffect = new Rename(fileInfo, existingFile.getName());

        IOException exception = assertThrows(IOException.class, renameEffect::apply);
        assertTrue(exception.getMessage().contains("File with the new name already exists"));
    }
}