package ru.practicum.moviehub.http;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.moviehub.api.ErrorResponse;
import ru.practicum.moviehub.model.Movie;

import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GetMovieByYearTest extends TestUtils {
    private static Movie firstFilm;
    private static Movie secondFilm;

    @BeforeEach
    void beforeEach() {
        firstFilm = new Movie(1, "Film", "Director", 2000);
        secondFilm = new Movie(2, "Film", "Director", 2000);
        store.addMovie(firstFilm);
        store.addMovie(secondFilm);
    }

    @Test
    void getMovies_whenYearIsCorrect_return2Films() throws Exception {

        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(BASE + "/movies?year=2000"))
                .GET()
                .header("Content-Type", "application/json; charset=UTF-8")
                .build();

        HttpResponse<String> resp =
                client.send(req, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));

        Collection<Movie> movies = store.getMovieByYear(firstFilm.getYear());
        String body = resp.body().trim();
        assertEquals(gson.toJson(movies), body);
    }

    @Test
    void getMovies_whenYearIsNotFound_return0Films() throws Exception {

        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(BASE + "/movies?year=1000"))
                .GET()
                .header("Content-Type", "application/json; charset=UTF-8")
                .build();

        HttpResponse<String> resp =
                client.send(req, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));

        String body = resp.body().trim();
        assertEquals("[]", body);
    }

    @Test
    void getMovies_whenYearIsNotNumber_returnError() throws Exception {

        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(BASE + "/movies?year=adc"))
                .GET()
                .header("Content-Type", "application/json; charset=UTF-8")
                .build();

        HttpResponse<String> resp =
                client.send(req, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));

        String body = resp.body().trim();
        ErrorResponse errorResponse = new ErrorResponse("Ошибка поиска",
                "Некорректный параметр запроса — 'year'");
        assertEquals(gson.toJson(errorResponse), body);
    }
}
