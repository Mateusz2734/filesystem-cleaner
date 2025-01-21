package pl.edu.agh.to2.cleaner.command;

import org.apache.commons.lang3.tuple.ImmutablePair;
import pl.edu.agh.to2.cleaner.model.FileInfo;

import java.util.List;

public interface FileFinder {
    List<ImmutablePair<FileInfo, FileInfo>> find();
    void setFiles(List<FileInfo> files);
}
