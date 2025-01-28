package pl.edu.agh.to2.cleaner.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pl.edu.agh.to2.cleaner.model.FileInfo;

import java.util.List;
import java.util.Optional;

@Repository
public interface FileInfoJpaRepository extends JpaRepository<FileInfo, String> {
    Optional<FileInfo> findByPath(String path);

    List<FileInfo> findByPathStartingWith(String path);

    @Query(value = "SELECT * FROM file_info WHERE path LIKE :path ORDER BY size DESC LIMIT :k", nativeQuery = true)
    List<FileInfo> findByPathStartingWithOrderBySizeDescLimitK(@Param("path") String path, @Param("k") int k);
}