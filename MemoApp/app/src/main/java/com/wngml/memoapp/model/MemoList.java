package com.wngml.memoapp.model;

import java.util.ArrayList;

public class MemoList {

    private String result;
    private int count;
    private ArrayList<Memo> result_list;

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

    public ArrayList<Memo> getresult_list() {
        return result_list;
    }

    public void setresult_list(ArrayList<Memo> result_list) {
        this.result_list = result_list;
    }
}
