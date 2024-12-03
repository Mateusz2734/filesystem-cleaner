package pl.edu.agh.to2.cleaner.effect;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.edu.agh.to2.cleaner.model.FileInfo;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;


class DeleteTest {

    private File singleTempFile;
    private List<File> tempFiles;

    @BeforeEach
    void setUp() throws IOException {
        singleTempFile = File.createTempFile("SingleToDelete", ".txt");

        tempFiles = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            File tempFile = File.createTempFile("InListToDelete" + i, ".txt");
            tempFiles.add(tempFile);
        }
    }

    @Test
    void testDeleteSingle() throws Exception {
        FileInfo fileInfo = new FileInfo(singleTempFile);

        Delete deleteEffect = new Delete(fileInfo);
        deleteEffect.apply();

        assertFalse(singleTempFile.exists());
    }

    @Test
    void testDeleteList() throws Exception {
        List<FileInfo> fileInfoList = new ArrayList<>();
        for (File tempFile : tempFiles) {
            fileInfoList.add(new FileInfo(tempFile));
        }

        Delete deleteEffect = new Delete(fileInfoList);
        deleteEffect.apply();

        for (File tempFile : tempFiles) {
            assertFalse(tempFile.exists());
        }
    }

}