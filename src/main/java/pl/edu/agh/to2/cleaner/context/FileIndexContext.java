package pl.edu.agh.to2.cleaner.context;

import org.springframework.stereotype.Component;

@Component
public class FileIndexContext {
    private volatile boolean indexing = false;

    public synchronized boolean isIndexing() {
        return indexing;
    }

    public synchronized void setIndexing(boolean indexing) {
        this.indexing = indexing;
    }
}

