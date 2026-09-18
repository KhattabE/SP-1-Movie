package app.services;

import app.dto.CreditsDTO;
import app.dto.GenreDTO;
import app.dto.GenreResponseDTO;
import app.dto.MovieResponseDTO;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.util.List;

public class TmdbService {

    private static final String BASE_URL = "https://api.themoviedb.org/3";

    private final String API_KEY;
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    public TmdbService() {
        API_KEY = System.getenv("TMDB_READ_ACCESS_TOKEN");

        if (API_KEY == null || API_KEY.isBlank()) {
            throw new IllegalStateException("TMDB_READ_ACCESS_TOKEN is not configured");
        }

        httpClient = HttpClient.newHttpClient();

        objectMapper = new ObjectMapper()
                .setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE)
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public MovieResponseDTO getDanishMovies(int page) {
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusYears(5);

        String endpoint = "/discover/movie"
                + "?with_origin_country=DK"
                + "&language=da-DK"
                + "&sort_by=popularity.desc"
                + "&primary_release_date.gte=" + startDate
                + "&primary_release_date.lte=" + endDate
                + "&page=" + page;

        return sendGet(endpoint, MovieResponseDTO.class);
    }

    public CreditsDTO getCredits(int movieId) {
        return sendGet("/movie/" + movieId + "/credits?language=da-DK", CreditsDTO.class);
    }

    public List<GenreDTO> getGenres() {
        GenreResponseDTO response = sendGet("/genre/movie/list?language=da", GenreResponseDTO.class);

        return response.getGenres();
    }

    private <T> T sendGet(String endpoint, Class<T> responseType) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + endpoint))
                .header("Authorization", "Bearer " + API_KEY)
                .header("Accept", "application/json")
                .GET()
                .build();

        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() < 200 || response.statusCode() >= 300) {
                throw new IllegalStateException("TMDB request failed with status " + response.statusCode());
            }

            return objectMapper.readValue(response.body(), responseType);
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("TMDB request was interrupted", exception);
        } catch (IOException exception) {
            throw new IllegalStateException("Could not read the TMDB response", exception);
        }
    }
}
