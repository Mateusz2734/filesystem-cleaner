package pl.edu.agh.to2.cleaner.command;

import org.apache.commons.lang3.tuple.ImmutablePair;
import pl.edu.agh.to2.cleaner.model.FileInfo;
import pl.edu.agh.to2.cleaner.model.comparator.FileComparator;

import java.util.ArrayList;
import java.util.List;

abstract class FileGroupFinder implements FileFinder {
    private List<FileInfo> files;
    private final FileComparator comparator;

    public FileGroupFinder(FileComparator comparator) {
        this.comparator = comparator;
    }

    public FileGroupFinder(List<FileInfo> files, FileComparator comparator) {
        this.files = files;
        this.comparator = comparator;
    }

    @Override
    public List<ImmutablePair<FileInfo, FileInfo>> find() {
        var connections = new ArrayList<ImmutablePair<FileInfo, FileInfo>>();

        for (int i = 0; i < files.size(); i++) {
            for (int j = 0; j < files.size(); j++) {
                if (i == j) {
                    continue;
                }
                if (comparator.compare(files.get(i), files.get(j))) {
                    connections.add(new ImmutablePair<>(files.get(i), files.get(j)));
                }
            }
        }
        return connections;
    }

    @Override
    public void setFiles(List<FileInfo> files) {
        this.files = files;
    }
}
