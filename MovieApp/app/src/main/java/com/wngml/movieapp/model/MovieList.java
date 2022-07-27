package com.wngml.movieapp.model;

import java.io.Serializable;
import java.util.ArrayList;

public class MovieList implements Serializable {
    private String result;
    private int count;
    private ArrayList<Movie> items;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public ArrayList<Movie> getItems() {
        return items;
    }

    public void setItems(ArrayList<Movie> items) {
        this.items = items;
    }
}
