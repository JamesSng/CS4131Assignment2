package com.example.assignment2.model;

public class LogEntry {

    public String adminName, enter, name, time, vaccineStatus;

    public LogEntry(){

    }

    public LogEntry(String adminName, String enter, String name, String time, String vaccineStatus){
        this.adminName = adminName;
        this.enter = enter;
        this.name = name;
        this.time = time;
        this.vaccineStatus = vaccineStatus;
    }

    public String getAdminName() {
        return adminName;
    }

    public void setAdminName(String adminName) {
        this.adminName = adminName;
    }

    public String getEnter() {
        return enter;
    }

    public void setEnter(String enter) {
        this.enter = enter;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getVaccineStatus() {
        return vaccineStatus;
    }

    public void setVaccineStatus(String vaccineStatus) {
        this.vaccineStatus = vaccineStatus;
    }
}
