package pl.edu.agh.to2.cleaner.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import pl.edu.agh.to2.cleaner.effect.Delete;
import pl.edu.agh.to2.cleaner.repository.FileInfoRepository;

import java.io.IOException;
import java.util.List;

@Service
public class FileManipulationService {
    private final FileInfoRepository fileInfoRepository;
    private static final Logger logger = LoggerFactory.getLogger(FileManipulationService.class);

    public FileManipulationService(FileInfoRepository fileInfoRepository) {
        this.fileInfoRepository = fileInfoRepository;
    }

    public void delete(List<String> filenames) {
        for (String filename : filenames) {
            try {
                var optFile = fileInfoRepository.getByPath(filename);
                if (optFile.isEmpty()) {
                    continue;
                }

                var file = optFile.get();

                new Delete(file).apply();

                fileInfoRepository.remove(file);

                logger.info("DELETE|{}|", file.getPath());
            } catch (IOException ignored) {
            }
        }
    }

    public void move(List<String> filenames, String destination) {
//        TODO
    }

    public void archive(List<String> filenames, String destination) {
//        TODO
    }
}
