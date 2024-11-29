package pl.edu.agh.to2.cleaner.model.comparator;

import pl.edu.agh.to2.cleaner.model.FileInfo;

public class NameEqualityComparator implements FileComparator {
    @Override
    public boolean compare(FileInfo file1, FileInfo file2) {
        return file1.getName().equals(file2.getName());
    }
}
