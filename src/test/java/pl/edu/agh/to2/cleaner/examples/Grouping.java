package pl.edu.agh.to2.cleaner.examples;

import pl.edu.agh.to2.cleaner.command.FileDuplicateFinder;
import pl.edu.agh.to2.cleaner.command.FileVersionsFinder;
import pl.edu.agh.to2.cleaner.command.UnionFind;
import pl.edu.agh.to2.cleaner.model.FileInfo;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

public class Grouping {
    public static void main(String[] args) throws IOException {
        var dir = "example_dir";
        var files = Files.walk(Path.of(dir))
                .filter(Files::isRegularFile)
                .map(FileInfo::new)
                .toList();

        {
            var connections = new FileVersionsFinder(files).find();
            var groups = UnionFind.connectedComponentsFromEdges(connections);

            System.out.println("Versions (similar name):");
            for (var group : groups) {
                System.out.println("Group:");
                for (var file : group) {
                    System.out.println("\t" +file.getPath());
                }
            }
        }

        System.out.println("\n");

        {
            var connections = new FileDuplicateFinder(files).find();
            var groups = UnionFind.connectedComponentsFromEdges(connections);

            System.out.println("Duplicates (same size and name):");
            for (var group : groups) {
                System.out.println("Group:");
                for (var file : group) {
                    System.out.println("\t" + file.getPath());
                }
            }
        }
    }
}
