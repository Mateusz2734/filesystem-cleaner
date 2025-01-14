package pl.edu.agh.to2.cleaner.command;

import pl.edu.agh.to2.cleaner.model.FileInfo;
import pl.edu.agh.to2.cleaner.model.comparator.NameSimilarityComparator;
import java.util.List;

public class FileVersionsFinder extends FileGroupFinder {
    public FileVersionsFinder(List<FileInfo> files) {
        super(files, new NameSimilarityComparator());
    }

    public FileVersionsFinder(List<FileInfo> files, double similarityThreshold) {
        super(files, new NameSimilarityComparator(similarityThreshold));
    }

    public FileVersionsFinder() {
        super(new NameSimilarityComparator());
    }

}
