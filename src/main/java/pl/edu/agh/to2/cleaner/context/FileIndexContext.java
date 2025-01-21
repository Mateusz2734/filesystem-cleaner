package pl.edu.agh.to2.cleaner.context;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
public class FileIndexContext {
    private volatile boolean indexing = false;
    private volatile String indexedRoot;
    private final List<String> alreadyIndexed = new CopyOnWriteArrayList<>();

    public synchronized boolean isIndexing() {
        return indexing;
    }

    public synchronized void setIndexing(boolean indexing) {
        this.indexing = indexing;
    }

    public synchronized String getIndexedRoot() {
        return indexedRoot;
    }

    public synchronized void setIndexedRoot(String indexedRoot) {
        this.indexedRoot = indexedRoot;
    }

    public synchronized List<String> getAlreadyIndexed() {
        return alreadyIndexed;
    }

    public synchronized void addIndexed(String path) {
        alreadyIndexed.add(path);
    }
}

