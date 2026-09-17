package app.daos;

import app.config.HibernateConfig;
import app.entities.Genre;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

import java.util.List;

public class GenreDAOImpl implements GenreDAO {

    private final EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();

    @Override
    public void create(Genre genre) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();

            em.persist(genre);

            em.getTransaction().commit();
        }
    }

    @Override
    public Genre getById(int id) {
        try (EntityManager em = emf.createEntityManager()) {
            return em.find(Genre.class, id);
        }
    }

    @Override
    public Genre getByTmdbId(int tmdbId) {
        try (EntityManager em = emf.createEntityManager()) {
            return em.createQuery(
                            "SELECT g FROM Genre g WHERE g.tmdbId = :tmdbId",
                            Genre.class
                    )
                    .setParameter("tmdbId", tmdbId)
                    .getResultStream()
                    .findFirst()
                    .orElse(null);
        }
    }

    @Override
    public List<Genre> getAll() {
        try (EntityManager em = emf.createEntityManager()) {
            return em.createQuery(
                            "SELECT g FROM Genre g",
                            Genre.class
                    )
                    .getResultList();
        }
    }

    @Override
    public Genre update(Genre genre) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();

            Genre updatedGenre = em.merge(genre);

            em.getTransaction().commit();

            return updatedGenre;
        }
    }

    @Override
    public void delete(int id) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();

            Genre genre = em.find(Genre.class, id);

            if (genre != null) {
                em.remove(genre);
            }

            em.getTransaction().commit();
        }
    }
}