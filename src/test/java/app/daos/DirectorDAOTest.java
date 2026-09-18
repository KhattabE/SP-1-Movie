package app.daos;

import app.TestDatabase;
import app.entities.Director;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class DirectorDAOTest extends TestDatabase {

    private DirectorDAO directorDAO;

    @BeforeEach
    void setUp() {
        clearDatabase();
        directorDAO = new DirectorDAOImpl(emf);
    }

    @Test
    void canCreateReadUpdateAndDeleteDirector() {
        Director director = Director.builder().tmdbId(200).name("Frederik Jensen").build();
        directorDAO.create(director);

        assertEquals("Frederik Jensen", directorDAO.getById(director.getId()).getName());
        assertEquals(director.getId(), directorDAO.getByTmdbId(200).getId());

        director.setName("Frederik Larsen");
        directorDAO.update(director);
        assertEquals("Frederik Larsen", directorDAO.getById(director.getId()).getName());

        directorDAO.delete(director.getId());
        assertNull(directorDAO.getById(director.getId()));
    }
}
