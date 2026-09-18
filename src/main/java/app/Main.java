package app;

import app.services.MovieService;

public class Main {
    public static void main(String[] args) {
        MovieService movieService = new MovieService();

        movieService.importMovies();

        System.out.println("Imported movies: " + movieService.getAllMovies().size());
    }
}
