package pl.edu.agh.to2.cleaner.model.comparator;

import pl.edu.agh.to2.cleaner.model.FileInfo;

import java.util.List;

public class CompositeFileComparator implements FileComparator {
    private final List<FileComparator> comparators;
    private final boolean requireAll;

    public CompositeFileComparator(List<FileComparator> comparators, boolean requireAll) {
        this.comparators = comparators;
        this.requireAll = requireAll;
    }

    @Override
    public boolean compare(FileInfo file1, FileInfo file2) {
        if (requireAll) {
            return comparators.stream().allMatch(cmp -> cmp.compare(file1, file2));
        }

        return comparators.stream().anyMatch(cmp -> cmp.compare(file1, file2));
    }
}