package app.daos;

import app.config.HibernateConfig;
import app.entities.Director;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

import java.util.List;

public class DirectorDAOImpl implements DirectorDAO {

    private final EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();

    @Override
    public void create(Director director) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();

            em.persist(director);

            em.getTransaction().commit();
        }
    }

    @Override
    public Director getById(int id) {
        try (EntityManager em = emf.createEntityManager()) {
            return em.find(Director.class, id);
        }
    }

    @Override
    public Director getByTmdbId(int tmdbId) {
        try (EntityManager em = emf.createEntityManager()) {
            return em.createQuery(
                            "SELECT d FROM Director d WHERE d.tmdbId = :tmdbId",
                            Director.class
                    )
                    .setParameter("tmdbId", tmdbId)
                    .getResultStream()
                    .findFirst()
                    .orElse(null);
        }
    }

    @Override
    public List<Director> getAll() {
        try (EntityManager em = emf.createEntityManager()) {
            return em.createQuery(
                            "SELECT d FROM Director d",
                            Director.class
                    )
                    .getResultList();
        }
    }

    @Override
    public Director update(Director director) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();

            Director updatedDirector = em.merge(director);

            em.getTransaction().commit();

            return updatedDirector;
        }
    }

    @Override
    public void delete(int id) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();

            Director director = em.find(Director.class, id);

            if (director != null) {
                em.remove(director);
            }

            em.getTransaction().commit();
        }
    }
}