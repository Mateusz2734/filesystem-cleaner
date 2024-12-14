package pl.edu.agh.to2.cleaner.repository;

import jakarta.persistence.PersistenceException;
import org.hibernate.Session;
import pl.edu.agh.to2.cleaner.session.SessionService;

import java.util.Optional;

public abstract class Repository<T> {
    final SessionService sessionService;

    protected Repository(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    public Optional<T> add(final T object) throws PersistenceException {
        try (Session session = sessionService.getSession()) {
            return sessionService.doAsTransaction(() -> {
                session.merge(object);
                return object;
            });
        }
    }

    public boolean remove(final T object) throws PersistenceException {
        try (Session session = sessionService.getSession()) {
            return sessionService.doAsTransaction(() -> {
                session.remove(object);
                return true;
            }).orElse(false);
        }
    }

    public Session currentSession() {
        return sessionService.getSession();
    }

}
