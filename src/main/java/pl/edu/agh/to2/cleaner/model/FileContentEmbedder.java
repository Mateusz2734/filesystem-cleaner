package pl.edu.agh.to2.cleaner.model;

import java.io.*;
import java.nio.file.Files;
import java.util.List;
import java.util.zip.CRC32;

public class FileContentEmbedder {
    public static double cosineSimilarity(Float[] vectorA, Float[] vectorB) {
        var length = Math.min(vectorA.length, vectorB.length);

        double dotProduct = 0.0, normA = 0.0, normB = 0.0;
        for (int i = 0; i < length; i++) {
            dotProduct += vectorA[i] * vectorB[i];
            normA += Math.pow(vectorA[i], 2);
            normB += Math.pow(vectorB[i], 2);
        }

        return dotProduct / (Math.sqrt(normA) * Math.sqrt(normB));
    }

    public static List<FileInfo> embedFiles(List<FileInfo> files) {
        for (FileInfo fileInfo : files) {
            try {
                var bytes = Files.readAllBytes(fileInfo.toPath());
                var fileContents = new String(bytes);

                var embedding = EmbeddingServerClient.fetchEmbedding(fileContents);

                if (embedding != null) {
                    fileInfo.setChecksum(checksum(bytes));
                    fileInfo.setEmbedding(embedding);
                }

            } catch (IOException ignored) {
            }
        }

        return files;
    }

    public static long checksum(byte[] bytes) {
        CRC32 crc32 = new CRC32();

        crc32.update(bytes, 0, bytes.length);

        return crc32.getValue();
    }
}

