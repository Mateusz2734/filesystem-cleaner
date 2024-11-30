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
import java.util.Optional;
import java.util.logging.Logger;
import org.apache.commons.io.FilenameUtils;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import pl.edu.agh.to2.cleaner.dao.FileInfoDao;
import pl.edu.agh.to2.cleaner.effect.Move;
import pl.edu.agh.to2.cleaner.model.FileInfo;
import pl.edu.agh.to2.cleaner.effect.Archive;
import pl.edu.agh.to2.cleaner.repository.FileInfoRepository;
import pl.edu.agh.to2.cleaner.session.SessionService;
import pl.edu.agh.to2.cleaner.session.TransactionService;

import java.io.File;

public class Main {

	private static final Logger log = Logger.getLogger(Main.class.toString());

	private static final DateTimeFormatter DATE_FORMATTER =
			DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

	private static String formatDateTime(FileTime fileTime) {

		LocalDateTime localDateTime = fileTime
				.toInstant()
				.atZone(ZoneId.systemDefault())
				.toLocalDateTime();

		return localDateTime.format(DATE_FORMATTER);
	}

	private static final SessionService sessionService = new SessionService();
	private static final FileInfoDao fileInfoDao = new FileInfoDao(sessionService);
	private static final FileInfoRepository fileInfoRepository = new FileInfoRepository(fileInfoDao);
	private static void addFileToDB(FileInfo fileInfo) {
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

	private static void showFile(File file) {
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

				// ADD FILE TO DB
				addFileToDB(new FileInfo(file));
			} catch (IOException e) {
				System.err.println("Cannot get the last modified time - " + e);
			}
		}
	}

	public static void main(String[] args) throws IOException {
//		Application.launch(App.class);
		// TODO: Make this path configurable
		String pathStr = "C:\\Users\\mikol\\Documents\\test_folder\\test_folder_inner";

//		setUp();

		Path dir = Paths.get(pathStr);
		Files.walk(dir).forEach(path -> showFile(path.toFile()));

		sessionService.close();

		// ARCHIVE DEMO
//		List<FileInfo> filesToArchive = Files.walk(dir).map(Path::toFile).filter(file -> !file.isDirectory()).map(file -> {
//            try {
//                return new FileInfo(file);
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
//        }).limit(2).toList() /* OR: collect(Collectors.toList()) */;
//
//		// wypisz, jakie pliki beda archiwizowane
//		filesToArchive.forEach(fileInfo -> System.out.println("zip <- " + fileInfo.getName()));
//
//		Archive archive = new Archive(filesToArchive, pathStr);
//		archive.apply();
//
//		// zobaczmy, co sie zmienilo
//		Files.walk(dir).forEach(path -> showFile(path.toFile()));

		// MOVE DEMO
//		List<FileInfo> filesToMove = Files.walk(dir).map(Path::toFile).filter(file -> !file.isDirectory()).map(file -> {
//			try {
//				return new FileInfo(file);
//			} catch (IOException e) {
//				throw new RuntimeException(e);
//			}
//		}).limit(2).toList();
//
//		filesToMove.forEach(fileInfo -> System.out.println("(moved) <- " + fileInfo.getName()));
//
//		String moveDestStr = pathStr + "\\moved";
//		Move move = new Move(filesToMove, moveDestStr);
//		move.apply();
//
//		Files.walk(dir).forEach(path -> showFile(path.toFile()));




	}
}
