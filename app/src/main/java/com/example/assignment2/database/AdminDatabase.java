package com.example.assignment2.database;

import com.example.assignment2.model.Admin;

import java.util.ArrayList;

public class AdminDatabase {

    ArrayList<Admin> admins = new ArrayList<>();
    Admin currentAdmin;

    public static final int LOGIN_SUCCESSFUL = 0;
    public static final int INVALID_NAME = 1;
    public static final int INVALID_PASSWORD = 2;

    public int login(String username, String password){
        for (Admin admin: admins){
            if (admin.getUsername().equals(username)){
                if (admin.getPassword().equals(password)){
                    currentAdmin = admin;
                    return LOGIN_SUCCESSFUL;
                }
                else return INVALID_PASSWORD;
            }
        }
        return INVALID_NAME;
    }

    public void logout(){
        currentAdmin = null;
    }

    public void loadDB(String str){
        String[] adminArr = str.split("\n");
        for (String admin : adminArr){
            String[] details = admin.split(",");
            Admin a = new Admin(details[0], details[1]);
            admins.add(a);
        }
    }
}
