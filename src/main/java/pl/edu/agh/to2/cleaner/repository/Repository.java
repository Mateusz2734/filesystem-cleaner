package pl.edu.agh.to2.cleaner.repository;

import java.util.List;
import java.util.Optional;

public interface Repository<T> {
    Optional<T> add(T repositoryObj);

    Optional<T> getById(Long id);

    List<T> findAll();

    void remove(T repositoryObj);

}
