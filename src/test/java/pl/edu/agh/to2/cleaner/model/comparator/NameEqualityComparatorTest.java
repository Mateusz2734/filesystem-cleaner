package pl.edu.agh.to2.cleaner.model.comparator;

import org.junit.jupiter.api.Test;
import pl.edu.agh.to2.cleaner.model.FileInfo;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class NameEqualityComparatorTest {
    @Test
    void compare_withSameName() {
        var file1 = new FileInfo("./file1", "file1", null, null, null);
        var file2 = new FileInfo("./file2", "file1", null, null, null);

        var comparator = new NameEqualityComparator();
        assertTrue(comparator.compare(file1, file2));
    }

    @Test
    void compare_withDifferentNames() {
        var file1 = new FileInfo("./file1", "file1", null, null, null);
        var file2 = new FileInfo("./file2", "file2", null, null, null);

        var comparator = new NameEqualityComparator();
        assertFalse(comparator.compare(file1, file2));
    }

    @Test
    void compare_withEmptyNames() {
        var file1 = new FileInfo("./file1", "", null, null, null);
        var file2 = new FileInfo("./file2", "", null, null, null);

        var comparator = new NameEqualityComparator();
        assertTrue(comparator.compare(file1, file2));
    }
}
