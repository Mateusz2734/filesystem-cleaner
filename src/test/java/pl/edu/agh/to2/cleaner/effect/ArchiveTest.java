package pl.edu.agh.to2.cleaner.effect;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.edu.agh.to2.cleaner.model.FileInfo;

import java.io.File;
import java.nio.file.Files;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.io.FileInputStream;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ArchiveTest {

    private File singleTempFile;
    private List<File> tempFiles;
    private String tempDirectoryPath;

    @BeforeEach
    void setUp() throws IOException {

        File tempDirectory = File.createTempFile("testDir", "");
        if (tempDirectory.delete() && tempDirectory.mkdir()) {
            tempDirectoryPath = tempDirectory.getAbsolutePath();
        }

        singleTempFile = File.createTempFile("SingleToArchive", ".txt", tempDirectory);

        tempFiles = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            File tempFile = File.createTempFile("InListToArchive" + i, ".txt", tempDirectory);
            tempFiles.add(tempFile);
        }
    }

    @AfterEach
    void cleanUp() {
        for (File tempFile : tempFiles) {
            tempFile.delete();
        }

        if (singleTempFile != null && singleTempFile.exists()) {
            singleTempFile.delete();
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
    void testArchiveSingle() throws Exception {
        FileInfo fileInfo = new FileInfo(singleTempFile);

        Archive archiveEffect = new Archive(fileInfo, tempDirectoryPath);
        archiveEffect.apply();

        assertFalse(singleTempFile.exists());

        File zipFile = new File(tempDirectoryPath + "/compressed.zip");
        assertTrue(zipFile.exists());
        assertTrue(isFileInZip(zipFile, singleTempFile.getName()));
    }

    @Test
    void testArchiveList() throws Exception {
        List<FileInfo> fileInfoList = new ArrayList<>();
        for (File tempFile : tempFiles) {
            fileInfoList.add(new FileInfo(tempFile));
        }

        Archive archiveEffect = new Archive(fileInfoList, tempDirectoryPath);
        archiveEffect.apply();

        for (File tempFile : tempFiles) {
            assertFalse(tempFile.exists());
        }

        File zipFile = new File(tempDirectoryPath + "/compressed.zip");
        assertTrue(zipFile.exists());
        for (File tempFile : tempFiles) {
            assertTrue(isFileInZip(zipFile, tempFile.getName()));
        }
    }

    @Test
    void testArchiveFilesWithSameName() throws Exception {
        // Dodatkowy test na pokazanie zastosowania Rename
        // na rozwiązanie problamy archiwizacji plików o tej samej nazwie

        File tempDir1 = Files.createTempDirectory("folder1").toFile();
        File tempDir2 = Files.createTempDirectory("folder2").toFile();

        File file1 = new File(tempDir1, "duplicateName.txt");
        File file2 = new File(tempDir2, "duplicateName.txt");

        file1.createNewFile();
        file2.createNewFile();

        FileInfo fileInfo1 = new FileInfo(file1);
        FileInfo fileInfo2 = new FileInfo(file2);

        // Wywołanie apply z Rename
        String newName = "renamedDuplicate.txt";
        Rename renameEffect = new Rename(fileInfo2, newName);
        renameEffect.apply();

        List<FileInfo> fileInfoList = new ArrayList<>();
        fileInfoList.add(fileInfo1);
        fileInfoList.add(fileInfo2);

        Archive archiveEffect = new Archive(fileInfoList, tempDirectoryPath);
        archiveEffect.apply();

        File zipFile = new File(tempDirectoryPath + "/compressed.zip");
        assertTrue(zipFile.exists());
        assertTrue(isFileInZip(zipFile, fileInfo1.getName()));
        assertTrue(isFileInZip(zipFile, newName));

        // Dodatkowe sprzątanie
        Files.deleteIfExists(file1.toPath());
        Files.deleteIfExists(file2.toPath());
        Files.deleteIfExists(tempDir1.toPath());
        Files.deleteIfExists(tempDir2.toPath());
    }

    private boolean isFileInZip(File zipFile, String fileName) throws IOException {
        try (ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream(zipFile))) {
            ZipEntry entry;
            while ((entry = zipInputStream.getNextEntry()) != null) {
                if (entry.getName().equals(fileName)) {
                    return true;
                }
            }
        }
        return false;
    }
}