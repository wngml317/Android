package com.wngml.postingapp.model;

import java.io.Serializable;

public class Posting implements Serializable {

    public int id;
    public String title;
    public String body;

    public Posting(int id, String title, String body) {
        this.id = id;
        this.title = title;
        this.body = body;
    }
}
