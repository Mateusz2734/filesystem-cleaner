package pl.edu.agh.to2.cleaner.command;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.junit.jupiter.api.Test;
import pl.edu.agh.to2.cleaner.model.FileInfo;

import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class FileVersionsFinderTest {

    @Test
    void testDefaultComparator() {
        FileInfo file1 = new FileInfo("", "doc_v01", 100L, null, null);
        FileInfo file2 = new FileInfo("", "doc_v02", 100L, null, null);
        FileInfo file3 = new FileInfo("", "other_file", 200L, null, null);

        FileVersionsFinder finder = new FileVersionsFinder(List.of(file1, file2, file3));

        List<ImmutablePair<FileInfo, FileInfo>> pairs = finder.find();

        assertTrue(pairs.contains(new ImmutablePair<>(file1, file2)));
        assertFalse(pairs.contains(new ImmutablePair<>(file1, file3)));
    }

    @Test
    void testCustomThresholdComparator() {
        FileInfo file1 = new FileInfo("", "document_v1", 100L, null, null);
        FileInfo file2 = new FileInfo("", "document_v2", 100L, null, null);
        FileInfo file3 = new FileInfo("", "other_file", 200L, null, null);

        FileVersionsFinder finder = new FileVersionsFinder(List.of(file1, file2, file3), 0.9);

        List<ImmutablePair<FileInfo, FileInfo>> pairs = finder.find();

        assertTrue(pairs.contains(new ImmutablePair<>(file1, file2)));
        assertFalse(pairs.contains(new ImmutablePair<>(file1, file3)));
    }

    @Test
    void testEmptyFileList() {
        FileVersionsFinder finder = new FileVersionsFinder(List.of());
        List<ImmutablePair<FileInfo, FileInfo>> pairs = finder.find();
        assertTrue(pairs.isEmpty());
    }

    @Test
    void testSingleFile() {
        FileInfo file1 = new FileInfo("", "single_file", 100L, null, null);
        FileVersionsFinder finder = new FileVersionsFinder(List.of(file1));
        List<ImmutablePair<FileInfo, FileInfo>> pairs = finder.find();
        assertTrue(pairs.isEmpty());
    }

    @Test
    void testNoMatches() {
        FileInfo file1 = new FileInfo("", "file1", 100L, null, null);
        FileInfo file2 = new FileInfo("", "file2", 100L, null, null);
        FileInfo file3 = new FileInfo("", "other_file", 100L, null, null);

        FileVersionsFinder finder = new FileVersionsFinder(List.of(file1, file2, file3));
        List<ImmutablePair<FileInfo, FileInfo>> pairs = finder.find();

        // Assuming NameSimilarityComparator does not match file1/file2 to file3
        assertFalse(pairs.contains(new ImmutablePair<>(file1, file3)));
        assertFalse(pairs.contains(new ImmutablePair<>(file2, file3)));
    }
}

