package app.daos;

import app.config.HibernateConfig;
import app.entities.Actor;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

import java.util.List;

public class ActorDAOImpl implements ActorDAO {

    private final EntityManagerFactory emf;

    public ActorDAOImpl() {
        this(HibernateConfig.getEntityManagerFactory());
    }

    public ActorDAOImpl(EntityManagerFactory emf) {
        this.emf = emf;
    }

    @Override
    public void create(Actor actor) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();

            em.persist(actor);

            em.getTransaction().commit();
        }
    }

    @Override
    public Actor getById(long id) {
        try (EntityManager em = emf.createEntityManager()) {
            return em.find(Actor.class, id);
        }
    }

    @Override
    public Actor getByTmdbId(int tmdbId) {
        try (EntityManager em = emf.createEntityManager()) {
            return em.createQuery(
                            "SELECT a FROM Actor a WHERE a.tmdbId = :tmdbId",
                            Actor.class
                    )
                    .setParameter("tmdbId", tmdbId)
                    .getResultStream()
                    .findFirst()
                    .orElse(null);
        }
    }

    @Override
    public List<Actor> getAll() {
        try (EntityManager em = emf.createEntityManager()) {
            return em.createQuery(
                            "SELECT a FROM Actor a",
                            Actor.class
                    )
                    .getResultList();
        }
    }

    @Override
    public Actor update(Actor actor) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();

            Actor updatedActor = em.merge(actor);

            em.getTransaction().commit();

            return updatedActor;
        }
    }

    @Override
    public void delete(long id) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();

            Actor actor = em.find(Actor.class, id);

            if (actor != null) {
                em.remove(actor);
            }

            em.getTransaction().commit();
        }
    }
}
