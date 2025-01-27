package pl.edu.agh.to2.cleaner.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.edu.agh.to2.cleaner.repository.FileInfoRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class FileManipulationServiceTest {
    @Mock
    private FileInfoRepository repository; // Define the mock repository

    @InjectMocks
    private FileManipulationService fileService; // Inject the mock repository

    @Test
    public void testAppendNameSuffix_WithExtension() {
        String originalPath = "/dest/file1.txt";
        String result = fileService.appendNameSuffix(originalPath, 1);

        // Validate result
        assertEquals("/dest/file1_1.txt", result);
    }

    @Test
    public void testAppendNameSuffix_WithoutExtension() {
        String originalPath = "/dest/file1";
        String result = fileService.appendNameSuffix(originalPath, 1);

        // Validate result
        assertEquals("/dest/file1_1", result);
    }
}
