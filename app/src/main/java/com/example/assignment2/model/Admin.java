package com.example.assignment2.model;

public class Admin {

    private String username, password;

    public Admin(String username, String password){
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
