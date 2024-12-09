package pl.edu.agh.to2.cleaner.examples;

import org.apache.commons.io.FilenameUtils;
import pl.edu.agh.to2.cleaner.Main;
import pl.edu.agh.to2.cleaner.dao.FileInfoDao;
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
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

public class AppUsageExample {
    private final Logger log = Logger.getLogger(Main.class.toString());
    private final DateTimeFormatter DATE_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final SessionService sessionService = new SessionService();
    private final FileInfoDao fileInfoDao = new FileInfoDao(sessionService);
    private final FileInfoRepository fileInfoRepository = new FileInfoRepository(fileInfoDao);

    private final String rootDirectoryPath;

    private String formatDateTime(FileTime fileTime) {

        LocalDateTime localDateTime = fileTime
                .toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();

        return localDateTime.format(DATE_FORMATTER);
    }

    private void addFileToDB(FileInfo fileInfo) {
        sessionService.openSession();

        Optional<FileInfo> existingFileInfo = fileInfoRepository.findAll().stream()
                .filter(existing -> existing.getPath().equals(fileInfo.getPath()))
                .findFirst();

        if (existingFileInfo.isEmpty()) {
            var createdFileInfoRecord = fileInfoDao.create(
                    fileInfo.getPath(),
                    fileInfo.getName(),
                    fileInfo.getSize(),
                    fileInfo.getModificationTimeMS(),
                    fileInfo.getCreationTimeMS()
            );
        }
        sessionService.closeSession();
    }

    private void showFile(File file) {
        if (file.isDirectory()) {
            System.out.println("Directory: " + file.getAbsolutePath());
        } else {
            FileTime fileTimeModified;
            FileTime fileTimeCreated;
            try {
                Path filePath = Paths.get(file.getPath());
                BasicFileAttributes attr =
                        Files.readAttributes(filePath, BasicFileAttributes.class);

                fileTimeModified = attr.lastModifiedTime();
                fileTimeCreated = attr.creationTime();

                String createdPrint = formatDateTime(fileTimeCreated);
                String modifiedPrint = formatDateTime(fileTimeModified);
                System.out.println("File: " + file.getAbsolutePath() + " | Extension: " + FilenameUtils.getExtension(file.getAbsolutePath()) + " | " + file.length() + " B" + " | " + createdPrint + " | " + modifiedPrint);

                // Add the file to the Database
                addFileToDB(new FileInfo(file));
            } catch (IOException e) {
                System.err.println("Cannot get the last modified time - " + e);
            }
        }
    }

    private List<FileInfo> loadFilesIntoList(Path directoryPath) {
        List<FileInfo> filesToArchive;
        try {
            filesToArchive = Files.walk(directoryPath).map(Path::toFile).filter(file -> !file.isDirectory()).map(file -> {
                try {
                    return new FileInfo(file);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }).limit(2).toList();
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

    public AppUsageExample(String rootDirectoryPath) {
        this.rootDirectoryPath = rootDirectoryPath;
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
        Path dir = Paths.get(rootDirectoryPath);
        List<FileInfo> filesToArchive = null /* OR: collect(Collectors.toList()) */;
        filesToArchive = loadFilesIntoList(dir);

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
