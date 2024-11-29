package pl.edu.agh.to2.cleaner.model.comparator;

import org.junit.jupiter.api.Test;
import pl.edu.agh.to2.cleaner.model.FileInfo;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class NameSimilarityComparatorTest {
    @Test
    void compare_filesWithSimilarNamesAboveThreshold() {
        var file1 = new FileInfo("file1", "./file1", 100, null, null);
        var file2 = new FileInfo("file1_", "./file2", 100, null, null);

        var comparator = new NameSimilarityComparator(0.8);
        assertTrue(comparator.compare(file1, file2));
    }

    @Test
    void compare_filesWithSimilarNamesBelowThreshold() {
        var file1 = new FileInfo("file1", "./file1", 100, null, null);
        var file2 = new FileInfo("file2", "./file2", 100, null, null);

        var comparator = new NameSimilarityComparator(0.9);
        assertFalse(comparator.compare(file1, file2));
    }

    @Test
    void compare_filesWithIdenticalNames() {
        var file1 = new FileInfo("file1", "./file1", 100, null, null);
        var file2 = new FileInfo("file1", "./file2", 100, null, null);

        var comparator = new NameSimilarityComparator(0.8);
        assertTrue(comparator.compare(file1, file2));
    }

    @Test
    void compare_filesWithCompletelyDifferentNames() {
        var file1 = new FileInfo("file1", "./file1", 100, null, null);
        var file2 = new FileInfo("different", "./file2", 100, null, null);

        var comparator = new NameSimilarityComparator(0.8);
        assertFalse(comparator.compare(file1, file2));
    }

    @Test
    void compare_filesWithEmptyNames() {
        var file1 = new FileInfo("", "./file1", 100, null, null);
        var file2 = new FileInfo("", "./file2", 100, null, null);

        var comparator = new NameSimilarityComparator(0.8);
        assertTrue(comparator.compare(file1, file2));
    }
}
