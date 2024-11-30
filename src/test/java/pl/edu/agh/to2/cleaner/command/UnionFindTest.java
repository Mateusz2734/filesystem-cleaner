package pl.edu.agh.to2.cleaner.command;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.junit.jupiter.api.Test;
import pl.edu.agh.to2.cleaner.model.FileInfo;

import java.nio.file.attribute.FileTime;
import java.time.Instant;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UnionFindTest {
    @Test
    void connectedComponentsFromEdges_singleConnection() {
        var update = FileTime.from(Instant.now()).toMillis();
        var create = FileTime.from(Instant.now().minusMillis(10)).toMillis();

        var file1 = new FileInfo("./file1", "file1", 100L, create, update);
        var file2 = new FileInfo("./file2", "file2", 100L, create, update);

        var connections = List.of(new ImmutablePair<>(file1, file2));

        var uf = new UnionFind();
        var components = uf.connectedComponentsFromEdges(connections);

        assertEquals(1, components.size());
        assertTrue(components.get(0).contains(file1));
        assertTrue(components.get(0).contains(file2));
    }

    @Test
    void connectedComponentsFromEdges_multipleConnections() {
        var update = FileTime.from(Instant.now()).toMillis();
        var create = FileTime.from(Instant.now().minusMillis(10)).toMillis();

        var file1 = new FileInfo("./file1", "file1", 100L, create, update);
        var file2 = new FileInfo("./file2", "file2", 100L, create, update);
        var file3 = new FileInfo("./file3", "file3", 100L, create, update);
        var file4 = new FileInfo("./file4", "file4", 100L, create, update);
        var file5 = new FileInfo("./file5", "file5", 100L, create, update);

        var connections = List.of(new ImmutablePair<>(file1, file2), new ImmutablePair<>(file5, file3), new ImmutablePair<>(file4, file2));

        var uf = new UnionFind();
        var components = uf.connectedComponentsFromEdges(connections);

        assertEquals(2, components.size());
        assertTrue(components.stream().anyMatch(component -> component.contains(file1) && component.contains(file2) && component.contains(file4)));
        assertTrue(components.stream().anyMatch(component -> component.contains(file3) && component.contains(file5)));
    }

    @Test
    void connectedComponentsFromEdges_noConnections() {
        List<ImmutablePair<FileInfo, FileInfo>> connections = List.of();

        var uf = new UnionFind();
        var components = uf.connectedComponentsFromEdges(connections);

        assertEquals(0, components.size());
    }

    @Test
    void connectedComponentsFromEdges_disconnectedComponents() {
        var update = FileTime.from(Instant.now()).toMillis();
        var create = FileTime.from(Instant.now().minusMillis(10)).toMillis();

        var file1 = new FileInfo("./file1", "file1", 100L, create, update);
        var file2 = new FileInfo("./file2", "file2", 100L, create, update);
        var file3 = new FileInfo("./file3", "file3", 100L, create, update);
        var file4 = new FileInfo("./file4", "file4", 100L, create, update);

        var connections = List.of(new ImmutablePair<>(file1, file2), new ImmutablePair<>(file3, file4));

        var uf = new UnionFind();
        var components = uf.connectedComponentsFromEdges(connections);

        assertEquals(2, components.size());
        assertTrue(components.stream().anyMatch(component -> component.contains(file1) && component.contains(file2)));
        assertTrue(components.stream().anyMatch(component -> component.contains(file3) && component.contains(file4)));
    }
}