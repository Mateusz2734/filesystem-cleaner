package pl.edu.agh.to2.cleaner.model.comparator;

import pl.edu.agh.to2.cleaner.model.FileInfo;

public interface FileComparator {
    boolean compare(FileInfo file1, FileInfo file2);
}
