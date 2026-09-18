package app.daos;

import app.config.HibernateConfig;
import app.entities.Movie;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

import java.util.List;

public class MovieDAOImpl implements MovieDAO {

    private final EntityManagerFactory emf;

    public MovieDAOImpl() {
        this(HibernateConfig.getEntityManagerFactory());
    }

    public MovieDAOImpl(EntityManagerFactory emf) {
        this.emf = emf;
    }

    @Override
    public void create(Movie movie) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            em.persist(movie);
            em.getTransaction().commit();
        }
    }

    @Override
    public Movie getById(long id) {
        try (EntityManager em = emf.createEntityManager()) {
            Movie movie = em.find(Movie.class, id);
            loadRelationships(movie);
            return movie;
        }
    }

    @Override
    public List<Movie> getAll() {
        try (EntityManager em = emf.createEntityManager()) {
            List<Movie> movies = em.createQuery("SELECT m FROM Movie m", Movie.class)
                    .getResultList();
            loadRelationships(movies);
            return movies;
        }
    }

    @Override
    public Movie update(Movie movie) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();

            Movie updatedMovie = em.merge(movie);
            loadRelationships(updatedMovie);

            em.getTransaction().commit();

            return updatedMovie;
        }
    }

    @Override
    public void delete(long id) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();

            Movie movie = em.find(Movie.class, id);

            if (movie != null) {
                em.remove(movie);
            }

            em.getTransaction().commit();
        }
    }

    @Override
    public List<Movie> searchByTitle(String search) {
        try (EntityManager em = emf.createEntityManager()) {
            List<Movie> movies = em.createQuery(
                            "SELECT m FROM Movie m WHERE LOWER(m.title) LIKE LOWER(:search)",
                            Movie.class
                    )
                    .setParameter("search", "%" + search + "%")
                    .getResultList();
            loadRelationships(movies);
            return movies;
        }
    }

    @Override
    public List<Movie> getMoviesByGenre(String genreName) {
        try (EntityManager em = emf.createEntityManager()) {
            List<Movie> movies = em.createQuery(
                            "SELECT m FROM Movie m JOIN m.genres g WHERE LOWER(g.name) = LOWER(:genreName)",
                            Movie.class
                    )
                    .setParameter("genreName", genreName)
                    .getResultList();
            loadRelationships(movies);
            return movies;
        }
    }

    @Override
    public double getAverageRating() {
        try (EntityManager em = emf.createEntityManager()) {
            Double average = em.createQuery(
                            "SELECT AVG(m.rating) FROM Movie m",
                            Double.class
                    )
                    .getSingleResult();

            return average != null ? average : 0.0;
        }
    }

    @Override
    public List<Movie> getTop10HighestRated() {
        try (EntityManager em = emf.createEntityManager()) {
            List<Movie> movies = em.createQuery(
                            "SELECT m FROM Movie m ORDER BY m.rating DESC",
                            Movie.class
                    )
                    .setMaxResults(10)
                    .getResultList();
            loadRelationships(movies);
            return movies;
        }
    }

    @Override
    public List<Movie> getTop10LowestRated() {
        try (EntityManager em = emf.createEntityManager()) {
            List<Movie> movies = em.createQuery(
                            "SELECT m FROM Movie m ORDER BY m.rating ASC",
                            Movie.class
                    )
                    .setMaxResults(10)
                    .getResultList();
            loadRelationships(movies);
            return movies;
        }
    }

    @Override
    public List<Movie> getTop10MostPopular() {
        try (EntityManager em = emf.createEntityManager()) {
            List<Movie> movies = em.createQuery(
                            "SELECT m FROM Movie m ORDER BY m.popularity DESC",
                            Movie.class
                    )
                    .setMaxResults(10)
                    .getResultList();
            loadRelationships(movies);
            return movies;
        }
    }

    private void loadRelationships(Movie movie) {
        if (movie != null) {
            movie.getActors().size();
            movie.getGenres().size();
        }
    }

    private void loadRelationships(List<Movie> movies) {
        for (Movie movie : movies) {
            loadRelationships(movie);
        }
    }
}
