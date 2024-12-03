package pl.edu.agh.to2.cleaner.effect;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.edu.agh.to2.cleaner.model.FileInfo;

import java.io.File;
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