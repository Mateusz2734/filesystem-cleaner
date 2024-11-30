package pl.edu.agh.to2.cleaner.session;

import java.util.Optional;
import java.util.function.Supplier;

public interface TransactionService {
    <T> Optional<T> doAsTransaction(Supplier<T> task);
}
