package com.wngml.employeeapp.data;

import com.wngml.employeeapp.model.Employee;

import java.util.ArrayList;

public class Share {

    public ArrayList<Employee> employeeList;

    private  static Share share;

    public static Share getInstance() {
        if (share == null) {
            share = new Share();
        }

        return share;
    }

    private Share() {
        employeeList = new ArrayList<>();
    }

}
