package app.services;

import app.daos.ActorDAO;
import app.daos.ActorDAOImpl;
import app.daos.DirectorDAO;
import app.daos.DirectorDAOImpl;
import app.daos.GenreDAO;
import app.daos.GenreDAOImpl;
import app.daos.MovieDAO;
import app.daos.MovieDAOImpl;
import app.dto.CastDTO;
import app.dto.CreditsDTO;
import app.dto.CrewDTO;
import app.dto.GenreDTO;
import app.dto.MovieDTO;
import app.dto.MovieResponseDTO;
import app.entities.Actor;
import app.entities.Director;
import app.entities.Genre;
import app.entities.Movie;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class MovieService {

    private final MovieDAO movieDAO;
    private final ActorDAO actorDAO;
    private final DirectorDAO directorDAO;
    private final GenreDAO genreDAO;
    private final TmdbService tmdbService;

    public MovieService() {
        this(
                new MovieDAOImpl(),
                new ActorDAOImpl(),
                new DirectorDAOImpl(),
                new GenreDAOImpl(),
                new TmdbService()
        );
    }

    public MovieService(MovieDAO movieDAO, ActorDAO actorDAO, DirectorDAO directorDAO,
                        GenreDAO genreDAO, TmdbService tmdbService) {
        this.movieDAO = movieDAO;
        this.actorDAO = actorDAO;
        this.directorDAO = directorDAO;
        this.genreDAO = genreDAO;
        this.tmdbService = tmdbService;
    }

    public void importMovies() {
        List<Genre> genres = new ArrayList<>();

        for (GenreDTO genreDTO : tmdbService.getGenres()) {
            genres.add(genreDTOToEntity(genreDTO));
        }

        MovieResponseDTO response = tmdbService.getDanishMovies(1);
        int totalPages = response.getTotalPages();

        for (int page = 1; page <= totalPages; page++) {
            if (page > 1) {
                response = tmdbService.getDanishMovies(page);
            }

            for (MovieDTO movieDTO : response.getResults()) {
                Movie movie = movieDTOToEntity(movieDTO);
                CreditsDTO credits = tmdbService.getCredits(movieDTO.getId());

                for (CastDTO castDTO : credits.getCast()) {
                    movie.addActor(castDTOToEntity(castDTO));
                }

                for (CrewDTO crewDTO : credits.getCrew()) {
                    if ("Director".equalsIgnoreCase(crewDTO.getJob())) {
                        movie.setDirector(crewDTOToDirector(crewDTO));
                        break;
                    }
                }

                for (Integer genreId : movieDTO.getGenreIds()) {
                    for (Genre genre : genres) {
                        if (genre.getTmdbId().equals(genreId)) {
                            movie.addGenre(genre);
                            break;
                        }
                    }
                }

                movieDAO.create(movie);
            }
        }
    }

    private Movie movieDTOToEntity(MovieDTO movieDTO) {
        LocalDate releaseDate = null;

        if (movieDTO.getReleaseDate() != null && !movieDTO.getReleaseDate().isBlank()) {
            releaseDate = LocalDate.parse(movieDTO.getReleaseDate());
        }

        return Movie.builder()
                .tmdbId(movieDTO.getId())
                .title(movieDTO.getTitle())
                .releaseDate(releaseDate)
                .rating(movieDTO.getVoteAverage())
                .popularity(movieDTO.getPopularity())
                .build();
    }

    private Actor castDTOToEntity(CastDTO castDTO) {
        Actor actor = actorDAO.getByTmdbId(castDTO.getId());

        if (actor == null) {
            actor = Actor.builder()
                    .tmdbId(castDTO.getId())
                    .name(castDTO.getName())
                    .build();
            actorDAO.create(actor);
        }

        return actor;
    }

    private Director crewDTOToDirector(CrewDTO crewDTO) {
        Director director = directorDAO.getByTmdbId(crewDTO.getId());

        if (director == null) {
            director = Director.builder()
                    .tmdbId(crewDTO.getId())
                    .name(crewDTO.getName())
                    .build();
            directorDAO.create(director);
        }

        return director;
    }

    private Genre genreDTOToEntity(GenreDTO genreDTO) {
        Genre genre = genreDAO.getByTmdbId(genreDTO.getId());

        if (genre == null) {
            genre = Genre.builder()
                    .tmdbId(genreDTO.getId())
                    .name(genreDTO.getName())
                    .build();
            genreDAO.create(genre);
        }

        return genre;
    }

    private MovieDTO movieEntityToDTO(Movie movie) {
        List<Integer> genreIds = new ArrayList<>();

        for (Genre genre : movie.getGenres()) {
            genreIds.add(genre.getTmdbId());
        }

        String releaseDate = null;
        if (movie.getReleaseDate() != null) {
            releaseDate = movie.getReleaseDate().toString();
        }

        return new MovieDTO(
                movie.getTmdbId(),
                movie.getTitle(),
                releaseDate,
                movie.getRating(),
                movie.getPopularity(),
                genreIds
        );
    }

    public MovieDTO createMovie(MovieDTO movieDTO) {
        Movie movie = movieDTOToEntity(movieDTO);
        movieDAO.create(movie);
        return movieEntityToDTO(movie);
    }

    public MovieDTO getMovieById(long id) {
        Movie movie = movieDAO.getById(id);

        if (movie == null) {
            return null;
        }

        return movieEntityToDTO(movie);
    }

    public MovieDTO updateMovie(long id, MovieDTO movieDTO) {
        Movie movie = movieDAO.getById(id);

        if (movie == null) {
            throw new IllegalArgumentException("Movie not found");
        }

        movie.setTitle(movieDTO.getTitle());

        if (movieDTO.getReleaseDate() == null || movieDTO.getReleaseDate().isBlank()) {
            movie.setReleaseDate(null);
        } else {
            movie.setReleaseDate(LocalDate.parse(movieDTO.getReleaseDate()));
        }

        Movie updatedMovie = movieDAO.update(movie);
        return movieEntityToDTO(updatedMovie);
    }

    public void deleteMovie(long id) {
        movieDAO.delete(id);
    }

    public List<MovieDTO> getAllMovies() {
        List<MovieDTO> movies = new ArrayList<>();

        for (Movie movie : movieDAO.getAll()) {
            movies.add(movieEntityToDTO(movie));
        }

        return movies;
    }

    public List<CastDTO> getAllActors() {
        List<CastDTO> actors = new ArrayList<>();

        for (Actor actor : actorDAO.getAll()) {
            actors.add(new CastDTO(actor.getTmdbId(), actor.getName()));
        }

        return actors;
    }

    public List<CrewDTO> getAllDirectors() {
        List<CrewDTO> directors = new ArrayList<>();

        for (Director director : directorDAO.getAll()) {
            directors.add(new CrewDTO(director.getTmdbId(), director.getName(), "Director"));
        }

        return directors;
    }

    public List<GenreDTO> getAllGenres() {
        List<GenreDTO> genres = new ArrayList<>();

        for (Genre genre : genreDAO.getAll()) {
            genres.add(new GenreDTO(genre.getTmdbId(), genre.getName()));
        }

        return genres;
    }

    public List<MovieDTO> searchByTitle(String search) {
        return moviesToDTOs(movieDAO.searchByTitle(search));
    }

    public List<MovieDTO> getMoviesByGenre(String genreName) {
        return moviesToDTOs(movieDAO.getMoviesByGenre(genreName));
    }

    public double getAverageRating() {
        return movieDAO.getAverageRating();
    }

    public List<MovieDTO> getTop10HighestRated() {
        return moviesToDTOs(movieDAO.getTop10HighestRated());
    }

    public List<MovieDTO> getTop10LowestRated() {
        return moviesToDTOs(movieDAO.getTop10LowestRated());
    }

    public List<MovieDTO> getTop10MostPopular() {
        return moviesToDTOs(movieDAO.getTop10MostPopular());
    }

    private List<MovieDTO> moviesToDTOs(List<Movie> movies) {
        List<MovieDTO> movieDTOs = new ArrayList<>();

        for (Movie movie : movies) {
            movieDTOs.add(movieEntityToDTO(movie));
        }

        return movieDTOs;
    }

}
