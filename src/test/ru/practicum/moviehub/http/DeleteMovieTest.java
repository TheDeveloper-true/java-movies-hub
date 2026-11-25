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

public class DeleteMovieTest extends TestUtils {

    @BeforeEach
    void beforeEach() {
        Movie firstFilm = new Movie(1, "Film", "Director", 2000);
        Movie secondFilm = new Movie(2, "Film", "Director", 2000);
        store.addMovie(firstFilm);
        store.addMovie(secondFilm);
    }

    @Test
    void deleteMovies_whenIdIs1_return204Code() throws Exception {

        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(BASE + "/movies/1"))
                .DELETE()
                .header("Content-Type", "application/json; charset=UTF-8")
                .build();

        HttpResponse<String> resp =
                client.send(req, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));

        assertEquals(204, resp.statusCode());
    }

    @Test
    void deleteMovies_whenIdIsNotNumber_returnError() throws Exception {

        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(BASE + "/movies/1st"))
                .DELETE()
                .header("Content-Type", "application/json; charset=UTF-8")
                .build();

        HttpResponse<String> resp =
                client.send(req, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
        ErrorResponse errorResponse = new ErrorResponse("Ошибка ID",
                "Некорректный ID");

        assertEquals(gson.toJson(errorResponse), resp.body());
    }

    @Test
    void deleteMovies_whenIdIsNotFound_return404Code() throws Exception {

        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(BASE + "/movies/14"))
                .DELETE()
                .header("Content-Type", "application/json; charset=UTF-8")
                .build();

        HttpResponse<String> resp =
                client.send(req, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));

        assertEquals(404, resp.statusCode());
    }
}
