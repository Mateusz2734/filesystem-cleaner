package pl.edu.agh.to2.cleaner.dao;

import pl.edu.agh.to2.cleaner.model.FileInfo;
import pl.edu.agh.to2.cleaner.session.SessionService;

import jakarta.persistence.PersistenceException;
import java.io.File;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class FileInfoDao extends GenericDao<FileInfo> {
    public FileInfoDao(SessionService sessionService) {
        super(sessionService, FileInfo.class);
    }

    public Optional<FileInfo> create(final String path, final String name, final Long size, final Long modifiedTimeMS, final Long creationTimeMS) {
        FileInfo fileInfo = new FileInfo(path, name, size, modifiedTimeMS, creationTimeMS);
        return save(fileInfo);
    }

    public List<FileInfo> findAll() {
        try {
            return currentSession().createQuery("Select f from FileInfo f order by f.path", FileInfo.class)
                    .getResultList();
        } catch (PersistenceException e) {
            return Collections.emptyList();
        }
    }
}
