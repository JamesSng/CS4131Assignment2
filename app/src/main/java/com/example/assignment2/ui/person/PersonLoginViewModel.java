package com.example.assignment2.ui.person;

import androidx.lifecycle.ViewModel;

import com.example.assignment2.database.PersonDatabase;

public class PersonLoginViewModel extends ViewModel {

    private PersonDatabase db = new PersonDatabase();

    public PersonDatabase getDb(){ return db; }

}