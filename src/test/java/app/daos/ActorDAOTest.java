package app.daos;

import app.TestDatabase;
import app.entities.Actor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class ActorDAOTest extends TestDatabase {

    private ActorDAO actorDAO;

    @BeforeEach
    void setUp() {
        clearDatabase();
        actorDAO = new ActorDAOImpl(emf);
    }

    @Test
    void canCreateReadUpdateAndDeleteActor() {
        Actor actor = Actor.builder().tmdbId(100).name("Mads Mikkelsen").build();
        actorDAO.create(actor);

        assertEquals("Mads Mikkelsen", actorDAO.getById(actor.getId()).getName());
        assertEquals(actor.getId(), actorDAO.getByTmdbId(100).getId());

        actor.setName("Mads Petersen");
        actorDAO.update(actor);
        assertEquals("Mads Petersen", actorDAO.getById(actor.getId()).getName());

        actorDAO.delete(actor.getId());
        assertNull(actorDAO.getById(actor.getId()));
    }
}
