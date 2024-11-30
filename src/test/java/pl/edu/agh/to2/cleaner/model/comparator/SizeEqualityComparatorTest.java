package pl.edu.agh.to2.cleaner.model.comparator;

import org.junit.jupiter.api.Test;
import pl.edu.agh.to2.cleaner.model.FileInfo;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SizeEqualityComparatorTest {

    @Test
    void compare_filesWithSameSize() {
        var file1 = new FileInfo("./file1", "file1", 100L, null, null);
        var file2 = new FileInfo("./file2", "file2", 100L, null, null);

        var comparator = new SizeEqualityComparator();
        assertTrue(comparator.compare(file1, file2));
    }

    @Test
    void compare_filesWithDifferentSizes() {
        var file1 = new FileInfo("./file1", "file1", 100L, null, null);
        var file2 = new FileInfo("./file2", "file2", 200L, null, null);

        var comparator = new SizeEqualityComparator();
        assertFalse(comparator.compare(file1, file2));
    }

    @Test
    void compare_filesWithZeroSize() {
        var file1 = new FileInfo("./file1", "file1", 0L, null, null);
        var file2 = new FileInfo("./file2", "file2", 0L, null, null);

        var comparator = new SizeEqualityComparator();
        assertTrue(comparator.compare(file1, file2));
    }

    @Test
    void compare_filesWithNegativeSize() {
        var file1 = new FileInfo("./file1", "file1", -100L, null, null);
        var file2 = new FileInfo("./file2", "file2", -100L, null, null);

        var comparator = new SizeEqualityComparator();
        assertTrue(comparator.compare(file1, file2));
    }
}
