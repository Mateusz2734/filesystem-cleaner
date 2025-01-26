package pl.edu.agh.to2.cleaner.command;

import org.apache.commons.lang3.tuple.ImmutablePair;
import pl.edu.agh.to2.cleaner.model.FileInfo;

import java.util.*;

public class UnionFind {
    private static void addElement(FileInfo element, Map<FileInfo, FileInfo> parent, Map<FileInfo, Integer> rank) {
        parent.putIfAbsent(element, element);
        rank.putIfAbsent(element, 0);
    }

    private static FileInfo find(FileInfo element, Map<FileInfo, FileInfo> parent) {
        if (!parent.get(element).equals(element)) {
            parent.put(element, find(parent.get(element), parent));
        }
        return parent.get(element);
    }


    private static void union(FileInfo a, FileInfo b, Map<FileInfo, FileInfo> parent, Map<FileInfo, Integer> rank) {
        FileInfo rootA = find(a, parent);
        FileInfo rootB = find(b, parent);
        if (!rootA.equals(rootB)) {
            if (rank.get(rootA) > rank.get(rootB)) {
                parent.put(rootB, rootA);
            } else if (rank.get(rootA) < rank.get(rootB)) {
                parent.put(rootA, rootB);
            } else {
                parent.put(rootB, rootA);
                rank.put(rootA, rank.get(rootA) + 1);
            }
        }
    }

    public static List<Set<FileInfo>> connectedComponentsFromEdges(List<ImmutablePair<FileInfo, FileInfo>> connections) {
        final Map<FileInfo, FileInfo> parent = new HashMap<>();
        final Map<FileInfo, Integer> rank = new HashMap<>();

        for (ImmutablePair<FileInfo, FileInfo> edge : connections) {
            addElement(edge.getLeft(), parent, rank);
            addElement(edge.getRight(), parent, rank);
        }

        for (ImmutablePair<FileInfo, FileInfo> edge : connections) {
            union(edge.getLeft(), edge.getRight(), parent, rank);
        }

        Map<FileInfo, Set<FileInfo>> components = new HashMap<>();
        for (FileInfo element : parent.keySet()) {
            FileInfo root = find(element, parent);
            components.computeIfAbsent(root, k -> new HashSet<>()).add(element);
        }

        return new ArrayList<>(components.values());
    }
}