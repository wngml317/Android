package com.wngml.movieapp.model;

public class ReviewAdd {
    private int movieId;
    private float rating;

    public ReviewAdd() {

    }

    public ReviewAdd(int movieId, float rating) {
        this.movieId = movieId;
        this.rating = rating;
    }

    public int getMovieId() {
        return movieId;
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }
}
