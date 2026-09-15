package app.daos;

import app.entities.Movie;

import java.util.List;

    public interface MovieDAO {

        void create(Movie movie);

        Movie getById(int id);

        List<Movie> getAll();

        Movie update(Movie movie);

        void delete(int id);

        List<Movie> searchByTitle(String search);

        List<Movie> getMoviesByGenre(String genreName);

        double getAverageRating();

        List<Movie> getTop10HighestRated();

        List<Movie> getTop10LowestRated();

        List<Movie> getTop10MostPopular();
    }





