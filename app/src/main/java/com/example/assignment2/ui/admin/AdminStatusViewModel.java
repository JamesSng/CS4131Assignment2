package com.example.assignment2.ui.admin;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

public class AdminStatusViewModel extends ViewModel {
    private MutableLiveData<ArrayList<String>> names = new MutableLiveData<>(new ArrayList<>());
    private MutableLiveData<Integer> unvaccinated = new MutableLiveData<>(0);

    public LiveData<ArrayList<String>> getNames(){ return names; }

    public void addName(String name){ names.getValue().add(name); }

    public void removeName(String name){ names.getValue().remove(name); }

    public boolean hasName(String name){ return names.getValue().contains(name); }

    public LiveData<Integer> getUnvaccinated(){ return unvaccinated; }

    public void addUnvaccinated(){ unvaccinated.setValue(unvaccinated.getValue()+1); }
    public void removeUnvaccinated(){ unvaccinated.setValue(unvaccinated.getValue()-1); }
}