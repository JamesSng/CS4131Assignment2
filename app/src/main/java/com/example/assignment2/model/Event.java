package com.example.assignment2.model;

public class Event {

    private String name;
    private boolean vaccinatedOnly;

    public Event(String name){
        this(name, false);
    }

    public Event(String name, boolean vaccinatedOnly){
        this.name = name;
        this.vaccinatedOnly = vaccinatedOnly;
    }

    public String getName(){
        return name;
    }

    public boolean isVaccinatedOnly(){
        return vaccinatedOnly;
    }
}
