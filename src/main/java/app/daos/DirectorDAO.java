package app.daos;

import app.entities.Director;

import java.util.List;

    public interface DirectorDAO {

        void create(Director director);

        Director getById(long id);

        Director getByTmdbId(int tmdbId);

        List<Director> getAll();

        Director update(Director director);

        void delete(long id);
    }


