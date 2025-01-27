package pl.edu.agh.to2.cleaner.command;

import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class PathValidator {
    public static ValidationResult validateDirectoryPath(String pathString) {
        if (pathString == null || pathString.trim().isEmpty()) {
            return new ValidationResult(false, "Path cannot be null or empty");
        }

        try {
            Path path = Paths.get(pathString);

            if (!Files.exists(path)) {
                return new ValidationResult(false, "Path does not exist");
            }

            if (!path.isAbsolute()) {
                return new ValidationResult(false, "Path must be an absolute path");
            }

            if (!Files.isDirectory(path)) {
                return new ValidationResult(false, "Path is not a directory");
            }

            if (!Files.isReadable(path)) {
                return new ValidationResult(false, "Directory is not readable");
            }

            return new ValidationResult(true, "Valid directory path");

        } catch (InvalidPathException e) {
            return new ValidationResult(false, "Invalid path syntax: " + e.getMessage());
        } catch (SecurityException e) {
            return new ValidationResult(false, "Security error accessing path: " + e.getMessage());
        }
    }


    public record ValidationResult(boolean valid, String message) {
        public boolean isValid() {
            return valid;
        }
    }
}
