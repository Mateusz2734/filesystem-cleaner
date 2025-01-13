package pl.edu.agh.to2.cleaner.command;

import pl.edu.agh.to2.cleaner.model.FileInfo;
import pl.edu.agh.to2.cleaner.model.comparator.CompositeFileComparator;
import pl.edu.agh.to2.cleaner.model.comparator.NameEqualityComparator;
import pl.edu.agh.to2.cleaner.model.comparator.SizeEqualityComparator;

import java.util.List;

public class FileDuplicateFinder extends FileGroupFinder implements FileFinder {
    public FileDuplicateFinder(List<FileInfo> files) {
        super(files, new CompositeFileComparator(List.of(new NameEqualityComparator(), new SizeEqualityComparator()), true));
    }

    public FileDuplicateFinder() {
        super(new CompositeFileComparator(List.of(new NameEqualityComparator(), new SizeEqualityComparator()), true));
    }
}
