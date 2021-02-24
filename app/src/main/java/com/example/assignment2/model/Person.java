package com.example.assignment2.model;

public class Person {

    private String icNumber, name;
    private String password;
    private boolean passwordSet;
    private int vaccineStatus;
    public static final int UNVACCINATED = 0;
    public static final int FIRST_SHOT = 1;
    public static final int VACCINATED = 2;
    public static final int RECOVERED = 3;

    public Person(String icNumber, String name){
        this.icNumber = icNumber;
        this.name = name;
        this.vaccineStatus = UNVACCINATED;
        this.passwordSet = false;
    }

    public Person(String icNumber, String name, int vaccineStatus, String password){
        this.icNumber = icNumber;
        this.name = name;
        this.vaccineStatus = vaccineStatus;
        this.password = password;
        this.passwordSet = true;
    }

    public String getIcNumber() {
        return icNumber;
    }

    public String getName() {
        return name;
    }

    public void setPassword(String password){
        //to implement password checking
        this.password = password;
        passwordSet = true;
    }

    public String getPassword(){
        return password;
    }

    public boolean isPasswordSet(){
        return passwordSet;
    }

    public int getVaccineStatus() {
        return vaccineStatus;
    }

    public void setUnvaccinated(){
        vaccineStatus = UNVACCINATED;
    }

    public void setFirstShot(){
        vaccineStatus = FIRST_SHOT;
    }

    public void setVaccinated(){
        vaccineStatus = VACCINATED;
    }

    public void setRecovered(){
        vaccineStatus = RECOVERED;
    }
}
