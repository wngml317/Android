package com.wngml.movieapp.model;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MovieList implements Serializable {
    private String result;
    private int count;
    private ArrayList<Movie> items;
    private ArrayList<Movie> result_list;
    private Object item;

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

    public ArrayList<Movie> getResult_list() {
        return result_list;
    }

    public void setResult_list(ArrayList<Movie> result_list) {
        this.result_list = result_list;
    }

    public Object getItem() {
        return item;
    }

    public void setItem(Object item) {
        this.item = item;
    }
}

