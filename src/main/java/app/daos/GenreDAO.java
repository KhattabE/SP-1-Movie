package app.daos;

import app.entities.Genre;

import java.util.List;

    public interface GenreDAO {

        void create(Genre genre);

        Genre getById(int id);

        Genre getByTmdbId(int tmdbId);

        List<Genre> getAll();

        Genre update(Genre genre);

        void delete(int id);
    }


