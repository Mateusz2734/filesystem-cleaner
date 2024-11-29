package pl.edu.agh.to2.cleaner.command;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.junit.jupiter.api.Test;
import pl.edu.agh.to2.cleaner.model.FileInfo;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FileDuplicateFinderTest {

    @Test
    void testDuplicateFiles() {
        FileInfo file1 = new FileInfo("file1", "", 100, null, null);
        FileInfo file2 = new FileInfo("file1", "", 100, null, null); // Duplicate of file1
        FileInfo file3 = new FileInfo("file2", "", 200, null, null);

        FileDuplicateFinder finder = new FileDuplicateFinder(List.of(file1, file2, file3));

        List<ImmutablePair<FileInfo, FileInfo>> duplicates = finder.find();

        assertTrue(duplicates.contains(new ImmutablePair<>(file1, file2)));
        assertFalse(duplicates.contains(new ImmutablePair<>(file1, file3)));
    }

    @Test
    void testNoDuplicates() {
        FileInfo file1 = new FileInfo("file1", "", 100, null, null);
        FileInfo file2 = new FileInfo("file2", "", 200, null, null);
        FileInfo file3 = new FileInfo("file3", "", 300, null, null);

        FileDuplicateFinder finder = new FileDuplicateFinder(List.of(file1, file2, file3));

        List<ImmutablePair<FileInfo, FileInfo>> duplicates = finder.find();

        assertTrue(duplicates.isEmpty());
    }

    @Test
    void testEmptyFileList() {
        FileDuplicateFinder finder = new FileDuplicateFinder(List.of());
        List<ImmutablePair<FileInfo, FileInfo>> duplicates = finder.find();

        assertTrue(duplicates.isEmpty());
    }

    @Test
    void testSingleFile() {
        FileInfo file1 = new FileInfo("single_file", "", 100, null, null);

        FileDuplicateFinder finder = new FileDuplicateFinder(List.of(file1));

        List<ImmutablePair<FileInfo, FileInfo>> duplicates = finder.find();

        assertTrue(duplicates.isEmpty());
    }

    @Test
    void testPartialMatch() {
        FileInfo file1 = new FileInfo("file1", "", 100, null, null);
        FileInfo file2 = new FileInfo("file1", "", 200, null, null); // Same name, different size
        FileInfo file3 = new FileInfo("file2", "", 100, null, null); // Same size, different name

        FileDuplicateFinder finder = new FileDuplicateFinder(List.of(file1, file2, file3));

        List<ImmutablePair<FileInfo, FileInfo>> duplicates = finder.find();

        assertTrue(duplicates.isEmpty());
    }
}

