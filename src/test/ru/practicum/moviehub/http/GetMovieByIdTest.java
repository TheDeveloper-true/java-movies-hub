package ru.practicum.moviehub.http;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.moviehub.api.ErrorResponse;
import ru.practicum.moviehub.model.Movie;

import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GetMovieByIdTest extends TestUtils {
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
    void getMovies_whenIdIs1_return1Film() throws Exception {

        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(BASE + "/movies/1"))
                .GET()
                .header("Content-Type", "application/json; charset=UTF-8")
                .build();

        HttpResponse<String> resp =
                client.send(req, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));

        String contentTypeHeaderValue =
                resp.headers().firstValue("Content-Type").orElse("");
        assertEquals("application/json; charset=UTF-8", contentTypeHeaderValue,
                "Content-Type должен содержать формат данных и кодировку");

        String body = resp.body().trim();
        assertEquals(gson.toJson(firstFilm), body);
    }

    @Test
    void getMovies_whenIdNotFound_returnError() throws Exception {
        store.addMovie(firstFilm);
        store.addMovie(secondFilm);
        ErrorResponse errorResponse = new ErrorResponse("Ошибка поиска", "Фильм не найден");
        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(BASE + "/movies/22"))
                .GET()
                .header("Content-Type", "application/json; charset=UTF-8")
                .build();

        HttpResponse<String> resp =
                client.send(req, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));

        String body = resp.body().trim();
        assertEquals(gson.toJson(errorResponse), body);
    }

    @Test
    void getMovies_whenIdIsIncorrect_returnError() throws Exception {
        store.addMovie(firstFilm);
        store.addMovie(secondFilm);
        ErrorResponse errorResponse = new ErrorResponse("Ошибка ID", "Некорректный ID");
        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(BASE + "/movies/abc"))
                .GET()
                .header("Content-Type", "application/json; charset=UTF-8")
                .build();

        HttpResponse<String> resp =
                client.send(req, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));

        String body = resp.body().trim();
        assertEquals(gson.toJson(errorResponse), body);
    }
}
