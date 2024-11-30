package pl.edu.agh.to2.cleaner.repository;

import pl.edu.agh.to2.cleaner.dao.FileInfoDao;
import pl.edu.agh.to2.cleaner.model.FileInfo;

import java.util.List;
import java.util.Optional;

public class FileInfoRepository implements Repository<FileInfo> {

    private FileInfoDao fileInfoDao;

    public FileInfoRepository(FileInfoDao fileInfoDao) {
        this.fileInfoDao = fileInfoDao;
    }

    @Override
    public Optional<FileInfo> add(FileInfo fileInfo) {
        return fileInfoDao.create(
                fileInfo.getPath(),
                fileInfo.getName(),
                fileInfo.getSize(),
                fileInfo.getModificationTimeMS(),
                fileInfo.getCreationTimeMS()
        );
    }

    @Override
    public Optional<FileInfo> getById(Long id) {
        return fileInfoDao.findById(id);
    }

    @Override
    public List<FileInfo> findAll() {
        return fileInfoDao.findAll();
    }

    @Override
    public void remove(FileInfo fileInfo) {
        fileInfoDao.remove(fileInfo);
    }
}
