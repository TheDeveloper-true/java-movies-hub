package ru.practicum.moviehub;

import ru.practicum.moviehub.http.MoviesServer;
import ru.practicum.moviehub.model.Movie;
import ru.practicum.moviehub.store.MoviesStore;

public class MovieHubApp {
    public static void main(String[] args) {
        MoviesStore store = new MoviesStore();
        final MoviesServer server = new MoviesServer(store, 8080);
        Runtime.getRuntime().addShutdownHook(new Thread(server::stop));
        server.start();
        Movie firstFilm = new Movie(1, "Film", "Director", 2000);
        Movie secondFilm = new Movie(2, "Film", "Director", 2000);
        Movie thirdFilm = new Movie(3, "Film", "Director", 1900);
        store.addMovie(firstFilm);
        store.addMovie(secondFilm);
        store.addMovie(thirdFilm);
    }
}