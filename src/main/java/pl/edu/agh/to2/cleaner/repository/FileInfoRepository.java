package pl.edu.agh.to2.cleaner.repository;

import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import jakarta.persistence.EntityManager;
import pl.edu.agh.to2.cleaner.model.FileInfo;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

@Service
public class FileInfoRepository {
    private final EntityManager entityManager;
    private final FileInfoJpaRepository fileInfoJpaRepository;

    public FileInfoRepository(EntityManager entityManager, FileInfoJpaRepository fileInfoJpaRepository) {
        this.entityManager = entityManager;
        this.fileInfoJpaRepository = fileInfoJpaRepository;
    }

    @Transactional
    public boolean remove(FileInfo fileInfo) {
        entityManager.remove(fileInfo);
        return true;
    }

    @Transactional
    public Optional<FileInfo> add(FileInfo fileInfo) {
        // Use merge to either update existing or create new
        FileInfo mergedFileInfo = entityManager.merge(fileInfo);
        return Optional.of(mergedFileInfo);
    }

    @Transactional(readOnly = true)
    public List<FileInfo> getDescendants(Path path) {
        return getDescendants(path.toString());
    }

    @Transactional(readOnly = true)
    public List<FileInfo> getDescendants(String root) {
        String normalizedRoot = normalizeRootDirectoryPath(root);

        return fileInfoJpaRepository.findByPathStartingWith(FilenameUtils.separatorsToUnix(normalizedRoot));
    }

    @Transactional(readOnly = true)
    public List<FileInfo> findAll() {
        return fileInfoJpaRepository.findAll();
    }


    @Transactional(readOnly = true)
    public Optional<FileInfo> findByPath(String path) {
        String normalizedPath = FilenameUtils.separatorsToUnix(path);
        return fileInfoJpaRepository.findByPath(normalizedPath);
    }

    @Transactional
    public List<FileInfo> getLargestFiles(String root, int limit) {
        String normalizedRoot = normalizeRootDirectoryPath(root);
        return fileInfoJpaRepository.findByPathStartingWithOrderBySizeDescLimitK(normalizedRoot + "%", limit);
    }

    @Transactional
    public Optional<FileInfo> changePathAndName(FileInfo fileInfo, String newPath, String newName) {
        newPath = normalizeRootDirectoryPath(newPath);
        if (findByPath(newPath).isEmpty()) {
            entityManager.remove(fileInfo);

            var newFileInfo = new FileInfo(newPath, newName, fileInfo.getSize(), fileInfo.getModificationTimeMS(), fileInfo.getCreationTimeMS());
            newFileInfo.setEmbedding(fileInfo.getEmbedding());
            newFileInfo.setChecksum(fileInfo.getChecksum());
            entityManager.merge(newFileInfo);
            return Optional.of(newFileInfo);
        }
        return Optional.empty();
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
}