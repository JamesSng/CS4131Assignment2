package com.example.assignment2.database;

import com.example.assignment2.model.Clinic;

import java.util.ArrayList;

public class ClinicDatabase {

    ArrayList<Clinic> clinics;
    Clinic currentClinic;

    public static int LOGIN_SUCCESSFUL = 0;
    public static int INVALID_NAME = 1;
    public static int INVALID_PASSWORD = 2;

    public int login(String name, String password){
        for (Clinic clinic: clinics){
            if (clinic.getName().equals(name)){
                if (clinic.getPassword().equals(password)){
                    currentClinic = clinic;
                    return LOGIN_SUCCESSFUL;
                }
                else return INVALID_PASSWORD;
            }
        }
        return INVALID_NAME;
    }

    public void logout(){
        currentClinic = null;
    }

    public void loadDB(String str){
        String[] clinicArr = str.split("\n");
        for (String clinic: clinicArr){
            String[] details = clinic.split(",");
            Clinic c = new Clinic(details[0], details[1]);
            clinics.add(c);
        }
    }
}
