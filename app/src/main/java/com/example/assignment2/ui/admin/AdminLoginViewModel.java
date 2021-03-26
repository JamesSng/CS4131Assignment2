package com.example.assignment2.ui.admin;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.assignment2.database.AdminDatabase;
import com.example.assignment2.database.ClinicDatabase;

public class AdminLoginViewModel extends ViewModel {

    private AdminDatabase db = new AdminDatabase();

    public void loadDB(String str){ db.loadDB(str); }

    public AdminDatabase getDB(){
        return db;
    }
}