package app.daos;

import app.TestDatabase;
import app.entities.Actor;
import app.entities.Director;
import app.entities.Genre;
import app.entities.Movie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class MovieDAOTest extends TestDatabase {

    private MovieDAO movieDAO;
    private ActorDAO actorDAO;
    private DirectorDAO directorDAO;
    private GenreDAO genreDAO;

    @BeforeEach
    void setUp() {
        clearDatabase();
        movieDAO = new MovieDAOImpl(emf);
        actorDAO = new ActorDAOImpl(emf);
        directorDAO = new DirectorDAOImpl(emf);
        genreDAO = new GenreDAOImpl(emf);
    }

    @Test
    void canCreateReadUpdateAndDeleteMovie() {
        Movie movie = createMovie(1, "Rasmus' Eventyr", 5.0, 10.0);
        movieDAO.create(movie);

        assertEquals("Rasmus' Eventyr", movieDAO.getById(movie.getId()).getTitle());

        movie.setTitle("Rasmus' Store Eventyr");
        movieDAO.update(movie);
        assertEquals("Rasmus' Store Eventyr", movieDAO.getById(movie.getId()).getTitle());

        movieDAO.delete(movie.getId());
        assertNull(movieDAO.getById(movie.getId()));
    }

    @Test
    void canUseSearchGenreAndStatisticsQueries() {
        Genre adventure = Genre.builder().tmdbId(12).name("Eventyr").build();
        Actor actor = Actor.builder().tmdbId(100).name("Mads Mikkelsen").build();
        Director director = Director.builder().tmdbId(200).name("Zlatko Buric").build();
        genreDAO.create(adventure);
        actorDAO.create(actor);
        directorDAO.create(director);

        for (int number = 1; number <= 12; number++) {
            Movie movie = createMovie(number, "Københavnerdrøm " + number, number, 100 - number);
            movie.addGenre(adventure);
            movie.addActor(actor);
            movie.setDirector(director);
            movieDAO.create(movie);
        }

        List<Movie> movies = movieDAO.getAll();
        assertEquals(12, movies.size());
        assertEquals(1, movies.get(0).getActors().size());
        assertEquals(1, movies.get(0).getGenres().size());
        assertEquals(4, movieDAO.searchByTitle("KØBENHAVNERDRØM 1").size());
        assertEquals(12, movieDAO.getMoviesByGenre("eventyr").size());
        assertEquals(6.5, movieDAO.getAverageRating());

        assertEquals(10, movieDAO.getTop10HighestRated().size());
        assertEquals(12.0, movieDAO.getTop10HighestRated().get(0).getRating());
        assertEquals(1.0, movieDAO.getTop10LowestRated().get(0).getRating());
        assertEquals(99.0, movieDAO.getTop10MostPopular().get(0).getPopularity());
    }

    private Movie createMovie(int tmdbId, String title, double rating, double popularity) {
        return Movie.builder()
                .tmdbId(tmdbId)
                .title(title)
                .releaseDate(LocalDate.of(2025, 1, 1))
                .rating(rating)
                .popularity(popularity)
                .build();
    }
}
