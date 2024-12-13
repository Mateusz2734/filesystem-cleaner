package pl.edu.agh.to2.cleaner.examples;

import pl.edu.agh.to2.cleaner.effect.Archive;
import pl.edu.agh.to2.cleaner.effect.Move;
import pl.edu.agh.to2.cleaner.model.FileInfo;
import pl.edu.agh.to2.cleaner.repository.FileInfoRepository;
import pl.edu.agh.to2.cleaner.session.SessionService;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;

public class AppUsageExample {
    private final SessionService sessionService = new SessionService();
    private final FileInfoRepository repository = new FileInfoRepository(sessionService);
    private final String rootDirectoryPath;

    public AppUsageExample(String rootDirectoryPath) {
        this.rootDirectoryPath = rootDirectoryPath;
    }

    public static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Set<Object> seen = ConcurrentHashMap.newKeySet();
        return t -> seen.add(keyExtractor.apply(t));
    }

    public static void main(String[] args) {
        AppUsageExample appUsageExample = new AppUsageExample("example_dir");
        appUsageExample.demoNormal();
//		appUsageExample.demoArchive();
//		appUsageExample.demoMove();
    }

    private void showFile(File file) {
        if (file.isFile()) {
            var fileInfo = new FileInfo(file);
            System.out.println(fileInfo.describe());
            repository.add(fileInfo);
        }
    }

    private List<FileInfo> loadFilesIntoList(Path directoryPath) {
        List<FileInfo> filesToArchive;
        try {
            filesToArchive = Files.walk(directoryPath).map(Path::toFile).filter(file -> !file.isDirectory()).map(FileInfo::new).filter(distinctByKey(FileInfo::getName)).limit(2).toList();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return filesToArchive;
    }

    private void printPathsInDirectory(Path directoryPath) {
        try {
            Files.walk(directoryPath).forEach(path -> showFile(path.toFile()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void demoNormal() {
        Path dir = Paths.get(rootDirectoryPath);
        try {
            Files.walk(dir).forEach(path -> showFile(path.toFile()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        sessionService.close();
    }

    public void demoArchive() {
        var dir = Paths.get(rootDirectoryPath);
        var filesToArchive = loadFilesIntoList(dir);

        // Print the names of the files that will be archived.
        filesToArchive.forEach(fileInfo -> System.out.println("zip <- " + fileInfo.getName()));

        // Archive the files.
        Archive archive = new Archive(filesToArchive, rootDirectoryPath);
        archive.apply();

        // Print the directories and files within the given root directory
        // in order to see what has changed.
        printPathsInDirectory(dir);
    }

    public void demoMove() {
        Path dir = Paths.get(rootDirectoryPath);
        List<FileInfo> filesToMove = loadFilesIntoList(dir);

        // Print the names of the files that will be moved.
        filesToMove.forEach(fileInfo -> System.out.println("(moved) <- " + fileInfo.getName()));

        // Move the files.
        String moveDestStr = rootDirectoryPath + "\\moved";
        Move move = new Move(filesToMove, moveDestStr);
        move.apply();

        // Print the directories and files within the given root directory
        // in order to see what has changed.
        printPathsInDirectory(dir);
    }
}
