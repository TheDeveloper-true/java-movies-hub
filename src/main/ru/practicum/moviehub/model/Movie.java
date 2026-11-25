package ru.practicum.moviehub.model;

public class Movie {
    private int id;
    private String title;
    private String director;
    int year;

    public int getYear() {
        return year;
    }

    public Movie(int id, String title, String director, int year) {
        this.id = id;
        this.title = title;
        this.director = director;
        this.year = year;
    }

    public String getTitle() {
        return title;
    }

    public String getDirector() {
        return director;
    }

    public int getId() {
        return id;
    }
}