package pl.edu.agh.to2.cleaner.effect;

import pl.edu.agh.to2.cleaner.model.FileInfo;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

public class Move implements IOSideEffect {
    private final List<FileInfo> filesToMove = new ArrayList<>();
    private final String moveDestination; // CANNOT END WITH '/'

    public Move(FileInfo file, String path) {
        filesToMove.add(file);
        this.moveDestination = path;
    }

    public Move(List<FileInfo> files, String path) {
        filesToMove.addAll(files);
        this.moveDestination = path;
    }

    @Override
    public void apply() {
        try {
            for (FileInfo fileInfo : filesToMove) {
                File fileToMove = new File(fileInfo.getPath().toString());

                Path src = fileToMove.toPath();

                File destDirectory = new File(moveDestination);
                if (!destDirectory.exists()) {
                    // Create necessary directories.
                    Files.createDirectories(destDirectory.toPath());

                }

                Path dest = Paths.get(moveDestination + "\\" + fileInfo.getName());

//                Path destAfterMoving = Files.move(src, dest);
                Files.move(src, dest, StandardCopyOption.ATOMIC_MOVE);

//                if (destAfterMoving == null) {
//                    System.out.println("Unable to move the file: " + src.getFileName());
//                }

            }
        } catch (IOException e) {
//            e.printStackTrace();
            System.err.println("Move failed: " + e.getMessage());
        }

    }

    @Override
    public String getLogString() {
        return null;
    }
}
