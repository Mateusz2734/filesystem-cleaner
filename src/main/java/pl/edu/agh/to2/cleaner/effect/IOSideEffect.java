package pl.edu.agh.to2.cleaner.effect;

import java.io.IOException;

public interface IOSideEffect {
    void apply() throws IOException;
}
