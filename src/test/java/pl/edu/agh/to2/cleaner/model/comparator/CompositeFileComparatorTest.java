package pl.edu.agh.to2.cleaner.model.comparator;

import org.junit.jupiter.api.Test;
import pl.edu.agh.to2.cleaner.model.FileInfo;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CompositeFileComparatorTest {
    @Test
    void testAllMatchTrueWithRequireAll() {
        CompositeFileComparator comparator = new CompositeFileComparator(List.of(new AlwaysTrueComparator(), new AlwaysTrueComparator()), true);

        var file1 = new FileInfo("file1", "./file1", 100, null, null);
        var file2 = new FileInfo("file2", "./file2", 200, null, null);

        assertTrue(comparator.compare(file1, file2));
    }

    @Test
    void testOneMatchFalseWithRequireAll() {
        CompositeFileComparator comparator = new CompositeFileComparator(List.of(new AlwaysTrueComparator(), new AlwaysFalseComparator()), true);

        var file1 = new FileInfo("file1", "./file1", 100, null, null);
        var file2 = new FileInfo("file2", "./file2", 200, null, null);

        assertFalse(comparator.compare(file1, file2));
    }

    @Test
    void testAtLeastOneMatchTrueWithRequireAny() {
        CompositeFileComparator comparator = new CompositeFileComparator(List.of(new AlwaysFalseComparator(), new AlwaysTrueComparator()), false);

        var file1 = new FileInfo("file1", "./file1", 100, null, null);
        var file2 = new FileInfo("file2", "./file2", 200, null, null);

        assertTrue(comparator.compare(file1, file2));
    }

    @Test
    void testNoMatchWithRequireAny() {
        CompositeFileComparator comparator = new CompositeFileComparator(List.of(new AlwaysFalseComparator(), new AlwaysFalseComparator()), false);

        var file1 = new FileInfo("file1", "./file1", 100, null, null);
        var file2 = new FileInfo("file2", "./file2", 200, null, null);

        assertFalse(comparator.compare(file1, file2));
    }

    @Test
    void testEmptyComparatorListWithRequireAll() {
        CompositeFileComparator comparator = new CompositeFileComparator(List.of(), true);

        var file1 = new FileInfo("file1", "./file1", 100, null, null);
        var file2 = new FileInfo("file2", "./file2", 200, null, null);

        assertTrue(comparator.compare(file1, file2));
    }

    @Test
    void testEmptyComparatorListWithRequireAny() {
        CompositeFileComparator comparator = new CompositeFileComparator(List.of(), false);

        var file1 = new FileInfo("file1", "./file1", 100, null, null);
        var file2 = new FileInfo("file2", "./file2", 200, null, null);

        assertFalse(comparator.compare(file1, file2));
    }

    // Trivial FileComparators for testing
    static class AlwaysTrueComparator implements FileComparator {
        @Override
        public boolean compare(FileInfo file1, FileInfo file2) {
            return true;
        }
    }

    static class AlwaysFalseComparator implements FileComparator {
        @Override
        public boolean compare(FileInfo file1, FileInfo file2) {
            return false;
        }
    }
}
