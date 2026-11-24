package ru.practicum.moviehub.store;

import ru.practicum.moviehub.model.Movie;

import java.util.*;

public class MoviesStore {
    private final Map<Integer, Movie> storage = new HashMap<>();
    private static int nextId = 1;

    public void addMovie(Movie movie) {
        Movie stored = new Movie(nextId, movie.getTitle(), movie.getDirector(), movie.getYear());
        storage.put(nextId, stored);
        nextId++;
    }

    public void clearStorage() {
        storage.clear();
    }

    public Collection<Movie> getAll() {
        return storage.values();
    }

    public Movie getMovieById(int nextId) {
        return storage.get(nextId);
    }

    public Collection<Movie> getMovieByYear(int year) {
        List<Movie> movies = new ArrayList<>();
        for (Movie movie : storage.values()) {
            if (movie.getYear() == year)
                movies.add(movie);
        }
        return movies;
    }

    public boolean deleteMovieById(int nextId) {
        return storage.remove(nextId) != null;
    }
}