package pl.edu.agh.to2.cleaner.model;

import pl.edu.agh.to2.cleaner.repository.FileInfoRepository;
import pl.edu.agh.to2.cleaner.session.SessionService;

import java.io.*;
import java.util.List;
import java.util.logging.Level;

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
                    var fileContents = new String(java.nio.file.Files.readAllBytes(fileInfo.toPath())).replaceAll(System.lineSeparator(), " ");

                    var embedding = EmbeddingServerClient.fetchEmbedding(fileContents);

                    if (embedding != null) {
                        fileInfo.setEmbedding(embedding);
                    }

                } catch (IOException ignored) {}
            }

            return files;
    }

    public static void main(String[] args) {
        // Disable Hibernate logging for this example
        java.util.logging.Logger.getLogger("org.hibernate").setLevel(Level.OFF);
        new Thread(EmbeddingServerClient::run).start();

        var sessionService = new SessionService();
        var repository = new FileInfoRepository(sessionService);

        System.out.println("Waiting for the server to start...");
        while (!EmbeddingServerClient.ping()) {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException ignored) {}
        }

        List<FileInfo> files = List.of(new FileInfo(new File("example_dir/macbeth.txt")), new FileInfo(new File("example_dir/act1.txt")), new FileInfo(new File("example_dir/inner/deep/act3.txt")), new FileInfo(new File("example_dir/inner/placeholder.txt")));

        for (FileInfo fileInfo : files) {
            repository.add(fileInfo);
        }

        System.out.println("Embedding files...");
        files = FileContentEmbedder.embedFiles(files);

        for (FileInfo fileInfo : files) {
            repository.add(fileInfo);
        }

        for (FileInfo f1 : files) {
            System.out.println();
            for (FileInfo f2 : files) {
                if (f1 == f2 || f1.getEmbedding() == null || f2.getEmbedding() == null) {
                    continue;
                }
                System.out.println(f1.getName() + " vs " + f2.getName() + ": " + cosineSimilarity(f1.getEmbedding(), f2.getEmbedding()));
            }
        }

        EmbeddingServerClient.shutdown();
    }
}

