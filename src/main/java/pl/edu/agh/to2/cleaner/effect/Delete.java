package pl.edu.agh.to2.cleaner.effect;

import pl.edu.agh.to2.cleaner.model.FileInfo;

import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class Delete implements IOSideEffect {
    private final List<FileInfo> files = new ArrayList<>();

    public Delete(FileInfo file) {
        files.add(file);
    }

    public Delete(List<FileInfo> files) {
        this.files.addAll(files);
    }

    @Override
    public void apply() throws IOException {
        for (FileInfo file : files) {
            Files.delete(file.getPath());
        }
    }

    @Override
    public String getLogString() {
        return "Deleted " + files.size() + " files";
    }
}
