package pl.edu.agh.to2.cleaner.dao;

import jakarta.persistence.PersistenceException;
import org.hibernate.Session;
import pl.edu.agh.to2.cleaner.session.SessionService;

import java.util.Optional;

public abstract class GenericDao<T> {

    private final SessionService sessionService;

    private final Class<T> entityClass;

    protected GenericDao(SessionService sessionService, Class<T> entityClass) {
        this.sessionService = sessionService;
        this.entityClass = entityClass;
    }

    public Optional<T> findById(Long id) {
        return sessionService.doAsTransaction(() -> currentSession().get(entityClass, id));
    }

    public Optional<T> save(final T object) throws PersistenceException {
        Session session = sessionService.getSession();
        return sessionService.doAsTransaction(() -> {
            session.persist(object);
            return object;
        });
    }

    public boolean remove(final T object) throws PersistenceException {
        Session session = sessionService.getSession();
        return sessionService.doAsTransaction(() -> {
            session.remove(object);
            return true;
        }).orElse(false);
    }

    public Session currentSession() {
        return sessionService.getSession();
    }
}
