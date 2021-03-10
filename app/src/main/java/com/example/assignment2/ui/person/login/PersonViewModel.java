package com.example.assignment2.ui.person.login;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.assignment2.database.PersonDatabase;

public class PersonViewModel extends ViewModel {

    private PersonDatabase db = new PersonDatabase();

    public PersonDatabase getDb(){ return db; }

}