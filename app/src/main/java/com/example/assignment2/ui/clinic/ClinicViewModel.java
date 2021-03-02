package com.example.assignment2.ui.clinic;

import androidx.lifecycle.ViewModel;

import com.example.assignment2.database.ClinicDatabase;

import java.util.ArrayList;

public class ClinicViewModel extends ViewModel {

    private ClinicDatabase clinicDB = new ClinicDatabase();

    public void loadDB(String str){
        clinicDB.loadDB(str);
    }

    public ClinicDatabase getDB(){
        return clinicDB;
    }
}