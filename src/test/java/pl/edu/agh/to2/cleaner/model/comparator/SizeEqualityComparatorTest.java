package pl.edu.agh.to2.cleaner.model.comparator;

import org.junit.jupiter.api.Test;
import pl.edu.agh.to2.cleaner.model.FileInfo;

import java.nio.file.attribute.FileTime;
import java.time.Instant;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SizeEqualityComparatorTest {

    @Test
    void compare_filesWithSameSize() {
        var update = FileTime.from(Instant.now());
        var create = FileTime.from(Instant.now().minusMillis(TimeUnit.DAYS.toMillis(1)));

        var file1 = new FileInfo("file1", "./file1", 100, create, update);
        var file2 = new FileInfo("file2", "./file2", 100, create, update);

        var comparator = new SizeEqualityComparator();
        assertTrue(comparator.compare(file1, file2));
    }

    @Test
    void compare_filesWithDifferentSizes() {
        var update = FileTime.from(Instant.now());
        var create = FileTime.from(Instant.now().minusMillis(TimeUnit.DAYS.toMillis(1)));

        var file1 = new FileInfo("file1", "./file1", 100, create, update);
        var file2 = new FileInfo("file2", "./file2", 200, create, update);

        var comparator = new SizeEqualityComparator();
        assertFalse(comparator.compare(file1, file2));
    }

    @Test
    void compare_filesWithZeroSize() {
        var update = FileTime.from(Instant.now());
        var create = FileTime.from(Instant.now().minusMillis(TimeUnit.DAYS.toMillis(1)));

        var file1 = new FileInfo("file1", "./file1", 0, create, update);
        var file2 = new FileInfo("file2", "./file2", 0, create, update);

        var comparator = new SizeEqualityComparator();
        assertTrue(comparator.compare(file1, file2));
    }

    @Test
    void compare_filesWithNegativeSize() {
        var update = FileTime.from(Instant.now());
        var create = FileTime.from(Instant.now().minusMillis(TimeUnit.DAYS.toMillis(1)));

        var file1 = new FileInfo("file1", "./file1", -100, create, update);
        var file2 = new FileInfo("file2", "./file2", -100, create, update);

        var comparator = new SizeEqualityComparator();
        assertTrue(comparator.compare(file1, file2));
    }
}
