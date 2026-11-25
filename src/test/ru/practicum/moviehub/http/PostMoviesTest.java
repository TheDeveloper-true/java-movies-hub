package ru.practicum.moviehub.http;

import org.junit.jupiter.api.Test;
import ru.practicum.moviehub.api.ErrorResponse;
import ru.practicum.moviehub.model.Movie;

import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PostMoviesTest extends TestUtils {

    @Test
    void postMovies_withCorrectUri() throws Exception {
        Movie movie = new Movie(1, "SomeTitle", "director", 2000);
        String jsonString = gson.toJson(movie);
        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(BASE + "/movies"))
                .POST(HttpRequest.BodyPublishers.ofString(jsonString))
                .header("Content-Type", "application/json; charset=UTF-8")
                .build();

        HttpResponse<String> resp =
                TestUtils.client.send(req, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
        assertEquals(201, resp.statusCode());
    }

    @Test
    void postMovies_withEmptyTitle_returnErr() throws Exception {
        Movie movie = new Movie(1, "", "director", 2000);
        String jsonStr = gson.toJson(movie);
        ErrorResponse errorResponse = new ErrorResponse("Ошибка валидации",
                "Название не должно быть пустым");
        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(BASE + "/movies"))
                .POST(HttpRequest.BodyPublishers.ofString(jsonStr))
                .header("Content-Type", "application/json; charset=UTF-8")
                .build();

        HttpResponse<String> resp =
                TestUtils.client.send(req, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));

        String body = resp.body().trim();
        assertEquals(gson.toJson(errorResponse), body);
    }

    @Test
    void postMovies_withWrongContentType_returnErr() throws Exception {
        Movie movie = new Movie(1, "SomeTitle", "director", 2000);
        String jsonString = gson.toJson(movie);
        ErrorResponse errorResponse = new ErrorResponse("Ошибка валидации",
                "Content-Type должен быть application/json");
        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(BASE + "/movies"))
                .POST(HttpRequest.BodyPublishers.ofString(jsonString))
                .header("Content-Type", "text/plate; charset=UTF-8")
                .build();

        HttpResponse<String> resp =
                TestUtils.client.send(req, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
        assertEquals(gson.toJson(errorResponse), resp.body());
    }

    @Test
    void postMovies_withWrongJson_returnErr() throws Exception {
        String jsonStr = gson.toJson("{" +
                "ttl:" +
                "drt:" +
                "}"
        );
        ErrorResponse errorResponse = new ErrorResponse("Ошибка Json",
                "Некорректный запрос");
        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(BASE + "/movies"))
                .POST(HttpRequest.BodyPublishers.ofString(jsonStr))
                .header("Content-Type", "application/json; charset=UTF-8")
                .build();

        HttpResponse<String> resp =
                TestUtils.client.send(req, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
        assertEquals(gson.toJson(errorResponse), resp.body());
    }

    @Test
    void postMovies_withWrongYear_returnErr() throws Exception {
        Movie movie = new Movie(1, "SomeTitle", "director", 1777);
        String jsonStr = gson.toJson(movie);
        ErrorResponse errorResponse = new ErrorResponse("Ошибка валидации",
                "Год должен быть между 1888 и 2026");
        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(BASE + "/movies"))
                .POST(HttpRequest.BodyPublishers.ofString(jsonStr))
                .header("Content-Type", "application/json; charset=UTF-8")
                .build();

        HttpResponse<String> resp =
                client.send(req, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
        String body = resp.body().trim();
        assertEquals(gson.toJson(errorResponse), body);
    }

    @Test
    void postMovies_withLongTitle_returnErr() throws Exception {
        Movie movie = new Movie(1,
                "SomeLooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooo" +
                        "ooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooo" +
                        "oongTitle",
                "director", 1900);
        String jsonStr = gson.toJson(movie);
        ErrorResponse errorResponse = new ErrorResponse("Ошибка валидации",
                "Название слишком длинное");
        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(BASE + "/movies"))
                .POST(HttpRequest.BodyPublishers.ofString(jsonStr))
                .header("Content-Type", "application/json; charset=UTF-8")
                .build();

        HttpResponse<String> resp =
                client.send(req, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));

        String body = resp.body().trim();
        assertEquals(gson.toJson(errorResponse), body);
    }
}
