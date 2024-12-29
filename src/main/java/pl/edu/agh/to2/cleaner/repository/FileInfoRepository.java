package pl.edu.agh.to2.cleaner.repository;

import org.apache.commons.io.FilenameUtils;
import pl.edu.agh.to2.cleaner.model.FileInfo;
import pl.edu.agh.to2.cleaner.session.SessionService;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

public class FileInfoRepository extends Repository<FileInfo> {
    public FileInfoRepository(SessionService sessionService) {
        super(sessionService);
    }

    

    private String normalizeRootDirectoryPath(String root) {
        // Conversion for root: <relative path> -> <absolute path>.
        String absoluteRoot;
        try {
            absoluteRoot = Paths.get(root).toAbsolutePath().normalize().toString();
        } catch (Exception e) {
            throw new IllegalArgumentException("Cannot convert the relative path to its corresponding absolute path: " + root, e);
        }

        // Directory separator normalization (e.g. replace "\" with "/").
        String normalizedRoot = FilenameUtils.separatorsToUnix(absoluteRoot);
        if (!normalizedRoot.endsWith("/")) {
            normalizedRoot += "/";
        }

        return normalizedRoot;
    }

    public List<FileInfo> getDescendants(Path root) {
        return getDescendants(root.toAbsolutePath().toString());
    }
  
    public List<FileInfo> getDescendants(String root) {
        String normalizedRoot = normalizeRootDirectoryPath(root);
        return currentSession().createQuery("from FileInfo where path like :root", FileInfo.class).
                setParameter("root", FilenameUtils.separatorsToUnix(normalizedRoot) + "%").list();
    }

    public List<FileInfo> getLargestFiles(String root, int limit) {
        String normalizedRoot = normalizeRootDirectoryPath(root);
        return currentSession().createQuery(
                        "from FileInfo fi where fi.path like :root order by fi.size desc",
                        FileInfo.class
                )
                .setParameter("root", normalizedRoot + "%")
                .setMaxResults(limit) // We assume that there are no directories in the database.
                .list();
    }

    public Optional<FileInfo> getByPath(String path) {
        return currentSession().createQuery("from FileInfo where path = :path", FileInfo.class).setParameter("path", FilenameUtils.separatorsToUnix(path)).uniqueResultOptional();
    }

    public boolean move(FileInfo fileInfo, String newPath) {
        newPath = FilenameUtils.separatorsToUnix(newPath);

        if (getByPath(newPath).isEmpty()) {
            fileInfo.setPath(newPath);
            add(fileInfo);
            return true;
        }

        return false;
    }

    public boolean rename(FileInfo fileInfo, String newName) {
        // newName must end with the file extension
        var newPath = FilenameUtils.separatorsToUnix(FilenameUtils.getFullPath(fileInfo.getPath()) + newName);

        if (getByPath(newPath).isEmpty()) {
            remove(fileInfo);
            fileInfo.setName(newName);
            fileInfo.setPath(newPath);
            add(fileInfo);
            return true;
        }
        return false;
    }
}
