package ru.practicum.moviehub.http;

import com.google.gson.Gson;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import ru.practicum.moviehub.store.MoviesStore;

import java.net.http.HttpClient;

public class TestUtils {
    protected static final String BASE = "http://localhost:8080"; //
    protected static MoviesServer server;
    protected static HttpClient client;
    protected static MoviesStore store;
    protected static Gson gson = new Gson();
    protected static final int PORT = 8080;

    @BeforeAll
    static void beforeAll() {
        store = new MoviesStore();
        server = new MoviesServer(store, PORT);
        client = HttpClient.newHttpClient();
        server.start();
    }

    @BeforeEach
    void beforeEach() {
        store.clearStorage();
    }

    @AfterAll
    static void afterAll() {
        if (server != null) {
            server.stop();
        }
    }
}
