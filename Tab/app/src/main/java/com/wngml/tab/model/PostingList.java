package com.wngml.tab.model;

import java.util.ArrayList;

public class PostingList {

    private String result;
    private int count;
    private ArrayList<Posting> items;

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

    public ArrayList<Posting> getItems() {
        return items;
    }

    public void setItems(ArrayList<Posting> items) {
        this.items = items;
    }
}
