package app.daos;

import app.entities.Movie;

import java.util.List;

public class MovieDAOImpl implements MovieDAO{
    @Override
    public void create(Movie movie) {

    }

    @Override
    public Movie getById(int id) {
        return null;
    }

    @Override
    public List<Movie> getAll() {
        return List.of();
    }

    @Override
    public Movie update(Movie movie) {
        return null;
    }

    @Override
    public void delete(int id) {

    }

    @Override
    public List<Movie> searchByTitle(String search) {
        return List.of();
    }

    @Override
    public List<Movie> getMoviesByGenre(String genreName) {
        return List.of();
    }

    @Override
    public double getAverageRating() {
        return 0;
    }

    @Override
    public List<Movie> getTop10HighestRated() {
        return List.of();
    }

    @Override
    public List<Movie> getTop10LowestRated() {
        return List.of();
    }

    @Override
    public List<Movie> getTop10MostPopular() {
        return List.of();
    }
}
