package app.daos;

import app.TestDatabase;
import app.entities.Genre;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class GenreDAOTest extends TestDatabase {

    private GenreDAO genreDAO;

    @BeforeEach
    void setUp() {
        clearDatabase();
        genreDAO = new GenreDAOImpl(emf);
    }

    @Test
    void canCreateReadUpdateAndDeleteGenre() {
        Genre genre = Genre.builder().tmdbId(12).name("Eventyr").build();
        genreDAO.create(genre);

        assertEquals("Eventyr", genreDAO.getById(genre.getId()).getName());
        assertEquals(genre.getId(), genreDAO.getByTmdbId(12).getId());

        genre.setName("Dansk Eventyr");
        genreDAO.update(genre);
        assertEquals("Dansk Eventyr", genreDAO.getById(genre.getId()).getName());

        genreDAO.delete(genre.getId());
        assertNull(genreDAO.getById(genre.getId()));
    }
}
