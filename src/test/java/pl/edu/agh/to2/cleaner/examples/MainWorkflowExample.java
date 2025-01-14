package pl.edu.agh.to2.cleaner.examples;

import pl.edu.agh.to2.cleaner.command.FileDuplicateFinder;
import pl.edu.agh.to2.cleaner.command.FileTreeIndexer;
import pl.edu.agh.to2.cleaner.command.FileVersionsFinder;
import pl.edu.agh.to2.cleaner.command.UnionFind;
import pl.edu.agh.to2.cleaner.model.EmbeddingServerClient;
import pl.edu.agh.to2.cleaner.model.FileContentEmbedder;
import pl.edu.agh.to2.cleaner.model.FileInfo;
import pl.edu.agh.to2.cleaner.repository.FileInfoRepository;
import pl.edu.agh.to2.cleaner.session.SessionService;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.logging.Level;

public class MainWorkflowExample {
    static {
        java.util.logging.Logger.getLogger("org.hibernate").setLevel(Level.OFF);
    }
    private static final Path path = Path.of("example_dir");
    private static final FileInfoRepository repository = new FileInfoRepository(new SessionService());
    private static final FileTreeIndexer indexer = new FileTreeIndexer(repository);
    private static final String keyword = "macbeth";

    public static void main(String[] args) {
        System.out.println("Starting the server...");
        new Thread(EmbeddingServerClient::run).start();

        System.out.println("Waiting for the server to be able to embed...");
        while (!EmbeddingServerClient.ping()) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ignored) {
            }
        }

        System.out.println("Server is ready to embed.\n");
        System.out.println("Indexing the root directory...");
        try {
            indexer.index(path);
        } catch (IOException e) {
            System.err.println("Failed to index the root directory.");
            e.printStackTrace();
        }
        System.out.println("Indexing finished successfully.\n");

        System.out.println("Embedding keyword...");
        var embedding = EmbeddingServerClient.fetchEmbedding(keyword);

        if (embedding != null) {
            System.out.println("Keyword embedded successfully.\n");
        } else {
            System.out.println("Failed to embed the keyword.");
            return;
        }

        var filesToSearch = repository.getDescendants(path);

        System.out.println("Searching for most similar files...");
        getMostSimilar(embedding, filesToSearch);
        System.out.println();

        versions(filesToSearch);
        System.out.println();

        duplicates(filesToSearch);
        System.out.println();

        System.out.println("Shutting down the server...");
        EmbeddingServerClient.shutdown();
    }

    private static void duplicates(List<FileInfo> files) {
        var connections = new FileDuplicateFinder(files).find();
        var groups = new UnionFind().connectedComponentsFromEdges(connections);

        System.out.println("Duplicates (same size and name):");
        for (var group : groups) {
            System.out.println("Group:");
            for (var file : group) {
                System.out.println("\t" + file.getPath());
            }
        }
    }

    private static void versions(List<FileInfo> files) {
        var connections = new FileVersionsFinder(files).find();
        var groups = new UnionFind().connectedComponentsFromEdges(connections);

        System.out.println("Versions (similar name):");
        for (var group : groups) {
            System.out.println("Group:");
            for (var file : group) {
                System.out.println("\t" +file.getPath());
            }
        }
    }

    private static void getMostSimilar(Float[] embedding, List<FileInfo> files) {
        var mostSimilar = files.stream()
                .map(file -> new Object() {
                    final double similarity = FileContentEmbedder.cosineSimilarity(file.getEmbedding(), embedding);
                    final String path = file.getPath();
                })
                .sorted((a, b) -> Double.compare(b.similarity, a.similarity))
                .limit(2)
                .toList();

        mostSimilar.forEach(file -> System.out.printf("%s - %.2f\n", file.path, file.similarity));
    }
}
