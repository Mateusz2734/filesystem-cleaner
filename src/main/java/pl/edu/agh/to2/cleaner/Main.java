package pl.edu.agh.to2.cleaner;

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
import java.util.logging.Logger;
import org.apache.commons.io.FilenameUtils;
import pl.edu.agh.to2.cleaner.model.FileInfo;
import pl.edu.agh.to2.cleaner.effect.Archive;

import java.io.File;

public class Main {

	private static final Logger log = Logger.getLogger(Main.class.toString());

	private static final DateTimeFormatter DATE_FORMATTER =
			DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

	public static String formatDateTime(FileTime fileTime) {

		LocalDateTime localDateTime = fileTime
				.toInstant()
				.atZone(ZoneId.systemDefault())
				.toLocalDateTime();

		return localDateTime.format(DATE_FORMATTER);
	}

	public static void showFile(File file) {
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

			} catch (IOException e) {
				System.err.println("Cannot get the last modified time - " + e);
			}
			// TODO: logika dodawania pliku do bazy danych
		}
	}

	public static void main(String[] args) throws IOException {
//		Application.launch(App.class);
		// TODO: Make this path configurable
		String pathStr = "C:\\USERS\\MATEU\\PYCHARMPROJECTS\\TO-PROJEKT\\TESTDIR";

		Path dir = Paths.get(pathStr);
		Files.walk(dir).forEach(path -> showFile(path.toFile()));

		List<FileInfo> filesToArchive = Files.walk(dir).map(Path::toFile).filter(file -> !file.isDirectory()).map(file -> {
            try {
                return new FileInfo(file);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }).limit(2).toList() /* OR: collect(Collectors.toList()) */;

		// WYPISZ, JAKIE PLIKI BEDA ZARCHIWIZOWANE
		filesToArchive.forEach(fileInfo -> System.out.println("zip <- " + fileInfo.getName()));

		Archive archive = new Archive(filesToArchive, pathStr);
		archive.apply();

		// zobaczmy, co sie zmienilo
		Files.walk(dir).forEach(path -> showFile(path.toFile()));

	}
}
