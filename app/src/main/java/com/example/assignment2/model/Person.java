package com.example.assignment2.model;

public class Person {

    private String icNumber, name;
    private String password;
    private int vaccineStatus;
    public static final int UNVACCINATED = 0;
    public static final int FIRST_SHOT = 1;
    public static final int VACCINATED = 2;
    public static final int RECOVERED = 3;

    public Person(){

    }

    public Person(String icNumber, String name){
        this.icNumber = icNumber;
        this.name = name;
        this.vaccineStatus = UNVACCINATED;
    }

    public Person(String icNumber, String name, int vaccineStatus, String password){
        this.icNumber = icNumber;
        this.name = name;
        this.vaccineStatus = vaccineStatus;
        this.password = password;
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
    }

    public String getPassword(){
        return password;
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

    public String toString(){ return "[icNumber="+icNumber+",name="+name+",password="+password+",vaccineStatus="+vaccineStatus+"]"; }
}
