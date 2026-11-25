package ru.practicum.moviehub.http;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.net.httpserver.HttpExchange;
import ru.practicum.moviehub.api.ErrorResponse;
import ru.practicum.moviehub.model.Movie;
import ru.practicum.moviehub.store.MoviesStore;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Year;
import java.util.Collection;

public class MoviesHandler extends BaseHttpHandler {
    private final MoviesStore store;
    Gson gson = new Gson();
    ErrorResponse errorResponse;

    public MoviesHandler(MoviesStore store) {
        this.store = store;
    }

    @Override
    public void handle(HttpExchange ex) throws IOException {
        String method = ex.getRequestMethod();
        String path = ex.getRequestURI().getPath();
        if (method.equalsIgnoreCase("GET")) {
            if (path.equals("/movies")) {
                handleGetAll(ex);
            } else if (path.matches("/movies/\\d+/?")) {
                handleGetById(ex);
            } else {
                errorResponse = new ErrorResponse("Ошибка ID",
                        "Некорректный ID");
                sendJson(ex, 400, gson.toJson(errorResponse));
            }
        } else if (method.equalsIgnoreCase("POST")) {
            handlePost(ex);
        } else if (method.equalsIgnoreCase("DELETE")) {
            handleDelete(ex);
        } else {
            errorResponse = new ErrorResponse("Ошибка метода",
                    "Метод не поддерживается");
            sendJson(ex, 405, gson.toJson(errorResponse));
        }
    }

    void handleGetAll(HttpExchange ex) throws IOException {
        String query = ex.getRequestURI().getQuery();
        if (query == null || query.isEmpty()) {
            Collection<Movie> allFilms = store.getAll();
            sendJson(ex, 200, gson.toJson(allFilms));
            return;
        }
        try {
            String yearStr = query.substring("year=".length());
            int year = Integer.parseInt(yearStr);
            Collection<Movie> movie = store.getMovieByYear(year);
            String json = gson.toJson(movie);
            sendJson(ex, 200, json);
        } catch (Exception e) {
            errorResponse = new ErrorResponse("Ошибка поиска",
                    "Некорректный параметр запроса — 'year'");
            sendJson(ex, 404, gson.toJson(errorResponse));
        }
    }

    void handleGetById(HttpExchange ex) throws IOException {
        String path = ex.getRequestURI().getPath();
        String idStr = path.substring("/movies/".length());
        int id = Integer.parseInt(idStr);
        Movie movie = store.getMovieById(id);
        if (movie == null) {
            errorResponse = new ErrorResponse("Ошибка поиска",
                    "Фильм не найден");
            sendJson(ex, 404, gson.toJson(errorResponse));
            return;
        }
        String json = gson.toJson(movie);
        sendJson(ex, 200, json);
    }

    void handleDelete(HttpExchange ex) throws IOException {
        String path = ex.getRequestURI().getPath();
        String idStr = path.substring("/movies/".length());
        try {
            int id = Integer.parseInt(idStr);
            boolean isDeleted = store.deleteMovieById(id);
            if (isDeleted)
                ex.sendResponseHeaders(204, -1);
            else
                ex.sendResponseHeaders(404, -1);
        } catch (NumberFormatException e) {
            ErrorResponse errorResponse = new ErrorResponse("Ошибка ID",
                    "Некорректный ID");
            sendJson(ex, 400, gson.toJson(errorResponse));
        }
    }

    void handlePost(HttpExchange ex) throws IOException {
        try {
            String body = new String(ex.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
            JsonElement element = JsonParser.parseString(body);
            JsonObject object = element.getAsJsonObject();
            int maxYear = Year.now().getValue() + 1;
            int userYear = object.get("year").getAsInt();
            String contentType = ex.getRequestHeaders()
                    .getFirst("Content-Type");
            JsonElement jsonElement = object.get("title");
            if (!contentType.startsWith("application/json; charset=UTF-8")) {
                errorResponse = new ErrorResponse("Ошибка валидации",
                        "Content-Type должен быть application/json");
                sendJson(ex, 415, gson.toJson(errorResponse));
                return;
            }
            if (jsonElement == null || jsonElement.isJsonNull() || !jsonElement.isJsonPrimitive() ||
                    jsonElement.getAsString().isEmpty()) {
                errorResponse = new ErrorResponse("Ошибка валидации", "Название не должно быть пустым");
                sendJson(ex, 422, gson.toJson(errorResponse));
                return;
            }
            if (jsonElement.getAsString().length() > 100) {
                errorResponse = new ErrorResponse("Ошибка валидации", "Название слишком длинное");
                sendJson(ex, 422, gson.toJson(errorResponse));
                return;
            }
            if (userYear >= 1888 && userYear <= maxYear) {
                Movie movie = gson.fromJson(body, Movie.class);
                Movie movieToAdd = new Movie(movie.getId() + 1, movie.getTitle(), movie.getDirector(),
                        movie.getYear());
                store.addMovie(movieToAdd);
                String json = gson.toJson(movieToAdd);
                sendJson(ex, 201, json);
            } else {
                errorResponse = new ErrorResponse("Ошибка валидации",
                        "Год должен быть между 1888 и 2026");
                sendJson(ex, 422, gson.toJson(errorResponse));
            }
        } catch (Exception e) {
            errorResponse = new ErrorResponse("Ошибка Json",
                    "Некорректный запрос");
            sendJson(ex, 400, gson.toJson(errorResponse));
        }
    }
}