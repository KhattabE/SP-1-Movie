package app.daos;

import app.entities.Actor;

import java.util.List;

    public interface ActorDAO {

        void create(Actor actor);

        Actor getById(long id);

        Actor getByTmdbId(int tmdbId);

        List<Actor> getAll();

        Actor update(Actor actor);

        void delete(long id);
    }


