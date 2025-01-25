package pl.edu.agh.to2.cleaner.repository;

import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import jakarta.persistence.EntityManager;
import pl.edu.agh.to2.cleaner.model.FileInfo;

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
        return fileInfoJpaRepository.findByPath(path);
    }

    @Transactional
    public List<FileInfo> getLargestFiles(String root, int limit) {
        String normalizedRoot = normalizeRootDirectoryPath(root);
        return fileInfoJpaRepository.findByPathStartingWithOrderBySizeDescLimitK(normalizedRoot, limit);
    }

    @Transactional
    public boolean move(FileInfo fileInfo, String newPath) {
        // First, check if the new path is already taken
        if (findByPath(newPath).isEmpty()) {
            // Remove old entity
            entityManager.remove(entityManager.contains(fileInfo) ? fileInfo : entityManager.merge(fileInfo));

            // Update path and save as new entity
            fileInfo.setPath(newPath);
            entityManager.merge(fileInfo);
            return true;
        }
        return false;
    }

    @Transactional
    public boolean rename(FileInfo fileInfo, String newName) {
        String newPath = FilenameUtils.separatorsToUnix(
                FilenameUtils.getFullPath(fileInfo.getPath()) + newName
        );

        // Similar to move, but updating name and path
        if (findByPath(newPath).isEmpty()) {
            // Remove old entity
            entityManager.remove(entityManager.contains(fileInfo) ? fileInfo : entityManager.merge(fileInfo));

            // Update name and path
            fileInfo.setName(newName);
            fileInfo.setPath(newPath);
            entityManager.merge(fileInfo);
            return true;
        }
        return false;
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