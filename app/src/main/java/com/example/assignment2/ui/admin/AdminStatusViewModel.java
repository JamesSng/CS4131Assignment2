package com.example.assignment2.ui.admin;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

public class AdminStatusViewModel extends ViewModel {
    private MutableLiveData<ArrayList<String>> names = new MutableLiveData<>(new ArrayList<>());

    public LiveData<ArrayList<String>> getNames(){ return names; }

    public void addName(String name){ names.getValue().add(name); }

    public void removeName(String name){ names.getValue().remove(name); }

    public boolean hasName(String name){ return names.getValue().contains(name); }
}