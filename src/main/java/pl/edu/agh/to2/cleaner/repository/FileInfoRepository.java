package pl.edu.agh.to2.cleaner.repository;

import org.apache.commons.io.FilenameUtils;
import pl.edu.agh.to2.cleaner.model.FileInfo;
import pl.edu.agh.to2.cleaner.session.SessionService;

import java.util.List;
import java.util.Optional;

public class FileInfoRepository extends Repository<FileInfo> {
    public FileInfoRepository(SessionService sessionService) {
        super(sessionService);
    }

    public List<FileInfo> getDescendants(String root) {
        return currentSession().createQuery("from FileInfo where path like :root", FileInfo.class).setParameter("root", FilenameUtils.separatorsToUnix(root) + "%").list();
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
