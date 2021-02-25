package com.example.assignment2.model;

public class Clinic {

    private String name, password;

    public Clinic(String name, String password){
        this.name = name;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }
}
