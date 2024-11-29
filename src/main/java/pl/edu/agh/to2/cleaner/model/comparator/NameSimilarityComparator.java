package pl.edu.agh.to2.cleaner.model.comparator;

import pl.edu.agh.to2.cleaner.model.FileInfo;
import pl.edu.agh.to2.cleaner.similarity.DamerauLevenshtein;

public class NameSimilarityComparator implements FileComparator {
    private double threshold = 0.8;

    public NameSimilarityComparator() {
    }

    public NameSimilarityComparator(double threshold) {
        this.threshold = threshold;
    }

    @Override
    public boolean compare(FileInfo file1, FileInfo file2) {
        return DamerauLevenshtein.similarity(file1.getName(), file2.getName()) > threshold;
    }
}
