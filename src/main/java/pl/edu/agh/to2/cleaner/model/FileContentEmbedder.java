package pl.edu.agh.to2.cleaner.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import pl.edu.agh.to2.cleaner.repository.FileInfoRepository;
import pl.edu.agh.to2.cleaner.session.SessionService;

import java.io.*;
import java.util.List;

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

    public static List<FileInfo> embed(List<FileInfo> files) {
        var mapper = new ObjectMapper();
        var pb = new ProcessBuilder("uv", "run", "embed.py");

        try {
            Process process = pb.start();

            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(process.getOutputStream()));
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            for (FileInfo fileInfo : files) {
                var fileContents = new String(java.nio.file.Files.readAllBytes(fileInfo.toPath())).replaceAll(System.lineSeparator(), " ");

                writer.write(fileContents + "\n");
                writer.flush();

                Float[] embedding = mapper.readValue(reader.readLine(), Float[].class);

                fileInfo.setEmbedding(embedding);
            }

            writer.write("exit\n");
            writer.flush();
            writer.close();
            process.waitFor();
        } catch (Exception e) {
            System.err.println("Fatal error occurred while embedding file contents.");
        }
        return files;
    }

    public static void main(String[] args) {
        var sessionService = new SessionService();
        var repository = new FileInfoRepository(sessionService);

        List<FileInfo> files = List.of(new FileInfo(new File("example_dir/macbeth.txt")), new FileInfo(new File("example_dir/act1.txt")), new FileInfo(new File("example_dir/inner/deep/act3.txt")), new FileInfo(new File("example_dir/inner/placeholder.txt")));

        for (FileInfo fileInfo : files) {
            repository.add(fileInfo);
        }

        files = FileContentEmbedder.embed(files);

        for (FileInfo fileInfo : files) {
            repository.add(fileInfo);
        }

        for (FileInfo f1 : files) {
            for (FileInfo f2 : files) {
                if (f1 == f2 || f1.getEmbedding() == null || f2.getEmbedding() == null) {
                    continue;
                }
                System.out.println(f1.getName() + " vs " + f2.getName() + ": " + cosineSimilarity(f1.getEmbedding(), f2.getEmbedding()));
            }
        }
    }
}

