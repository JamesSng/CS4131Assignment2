package com.example.assignment2.ui.clinic;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.assignment2.database.ClinicDatabase;

public class ClinicViewModel extends ViewModel {

    private ClinicDatabase clinicDB = new ClinicDatabase();

    public void loadDB(String str){
        clinicDB.loadDB(str);
    }
}