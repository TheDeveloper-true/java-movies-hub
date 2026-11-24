package ru.practicum.moviehub.store;

import ru.practicum.moviehub.model.Movie;

import java.util.*;

public class MoviesStore {
    private final Map<Integer, Movie> storage = new HashMap<>();

    public void addMovie(Movie movie) {
        Movie stored = new Movie(movie.getId(), movie.getTitle(), movie.getDirector(), movie.getYear());
        storage.put(stored.getId(), stored);
    }

    public void clearStorage() {
        storage.clear();
    }

    public Collection<Movie> getAll() {
        return storage.values();
    }

    public Movie getMovieById(int id) {
        return storage.get(id);
    }

    public Collection<Movie> getMovieByYear(int year) {
        List<Movie> movies = new ArrayList<>();
        for (Movie movie : storage.values()) {
            if (movie.getYear() == year)
                movies.add(movie);
        }
        return movies;
    }

    public boolean deleteMovieById(int id) {
        return storage.remove(id) != null;
    }
}