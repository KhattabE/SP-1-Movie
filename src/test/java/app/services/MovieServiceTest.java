package app.services;

import app.TestDatabase;
import app.daos.ActorDAO;
import app.daos.ActorDAOImpl;
import app.daos.DirectorDAO;
import app.daos.DirectorDAOImpl;
import app.daos.GenreDAO;
import app.daos.GenreDAOImpl;
import app.daos.MovieDAO;
import app.daos.MovieDAOImpl;
import app.dto.MovieDTO;
import app.entities.Actor;
import app.entities.Director;
import app.entities.Genre;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class MovieServiceTest extends TestDatabase {

    private MovieService movieService;
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
        movieService = new MovieService(movieDAO, actorDAO, directorDAO, genreDAO, null);
    }

    @Test
    void canCreateReadUpdateSearchAndDeleteMovieUsingDTOs() {
        MovieDTO movie = new MovieDTO(
                10,
                "Rasmus' Eventyr",
                "2025-01-01",
                7.5,
                20.0,
                List.of()
        );

        movieService.createMovie(movie);
        long databaseId = movieDAO.getAll().get(0).getId();

        assertEquals("Rasmus' Eventyr", movieService.getMovieById(databaseId).getTitle());
        assertEquals(1, movieService.searchByTitle("EVENTYR").size());

        MovieDTO update = new MovieDTO(
                10,
                "Rasmus' Store Eventyr",
                "2024-02-02",
                7.5,
                20.0,
                List.of()
        );

        movieService.updateMovie(databaseId, update);
        assertEquals("Rasmus' Store Eventyr", movieService.getMovieById(databaseId).getTitle());

        movieService.deleteMovie(databaseId);
        assertNull(movieService.getMovieById(databaseId));
    }

    @Test
    void canReturnActorsDirectorsAndGenresAsDTOs() {
        actorDAO.create(Actor.builder().tmdbId(100).name("Mads Mikkelsen").build());
        directorDAO.create(Director.builder().tmdbId(200).name("Zlatko Buric").build());
        genreDAO.create(Genre.builder().tmdbId(18).name("Eventyr").build());

        assertEquals("Mads Mikkelsen", movieService.getAllActors().get(0).getName());
        assertEquals("Zlatko Buric", movieService.getAllDirectors().get(0).getName());
        assertEquals("Eventyr", movieService.getAllGenres().get(0).getName());
    }

    @Test
    void canHandleMovieWithoutReleaseDate() {
        MovieDTO movie = new MovieDTO(11, "Sommer Uden Slutning", "", 6.0, 10.0, List.of());

        movieService.createMovie(movie);
        long databaseId = movieDAO.getAll().get(0).getId();

        assertNull(movieService.getMovieById(databaseId).getReleaseDate());
    }
}
