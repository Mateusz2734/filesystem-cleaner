package pl.edu.agh.to2.cleaner.command;

import org.apache.commons.lang3.tuple.ImmutablePair;
import pl.edu.agh.to2.cleaner.model.FileInfo;

import java.util.*;

public class UnionFind {
    private static final Map<FileInfo, FileInfo> parent = new HashMap<>();
    private static final Map<FileInfo, Integer> rank = new HashMap<>();

    private static synchronized void addElement(FileInfo element) {
        parent.putIfAbsent(element, element);
        rank.putIfAbsent(element, 0);
    }

    private static synchronized FileInfo find(FileInfo element) {
        if (!parent.get(element).equals(element)) {
            parent.put(element, find(parent.get(element)));
        }
        return parent.get(element);
    }


    private static synchronized void union(FileInfo a, FileInfo b) {
        FileInfo rootA = find(a);
        FileInfo rootB = find(b);
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
        parent.clear();
        rank.clear();

        for (ImmutablePair<FileInfo, FileInfo> edge : connections) {
            addElement(edge.getLeft());
            addElement(edge.getRight());
        }

        for (ImmutablePair<FileInfo, FileInfo> edge : connections) {
            union(edge.getLeft(), edge.getRight());
        }

        Map<FileInfo, Set<FileInfo>> components = new HashMap<>();
        for (FileInfo element : parent.keySet()) {
            FileInfo root = find(element);
            components.computeIfAbsent(root, k -> new HashSet<>()).add(element);
        }

        return new ArrayList<>(components.values());
    }
}