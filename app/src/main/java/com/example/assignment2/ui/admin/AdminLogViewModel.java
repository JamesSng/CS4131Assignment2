package com.example.assignment2.ui.admin;

import android.widget.LinearLayout;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

public class AdminLogViewModel extends ViewModel {

    private final MutableLiveData<ArrayList<String>>
            names = new MutableLiveData<>(new ArrayList<>());
    private final MutableLiveData<ArrayList<String>> times = new MutableLiveData<>(new ArrayList<>());
    private final MutableLiveData<ArrayList<String>> enter = new MutableLiveData<>(new ArrayList<>());
    private final MutableLiveData<ArrayList<String>> vaccineStatuses = new MutableLiveData<>(new ArrayList<>());

    private final MutableLiveData<RecyclerAdapter> recyclerAdapter = new MutableLiveData<>(null);

    private String admin;

    public LiveData<RecyclerAdapter> getRecyclerAdapter(){ return recyclerAdapter; }

    public void setAdmin(String admin){ this.admin = admin; }

    public void addLog(String enter_, String name, String time, String vaccineStatus){
        enter.getValue().add(0, enter_);
        names.getValue().add(0, name);
        times.getValue().add(0, time);
        vaccineStatuses.getValue().add(0, vaccineStatus);

        recyclerAdapter.setValue(new RecyclerAdapter(
                enter.getValue().toArray(new String[0]),
                names.getValue().toArray(new String[0]),
                times.getValue().toArray(new String[0]),
                vaccineStatuses.getValue().toArray(new String[0])
        ));

        recyclerAdapter.getValue().notifyDataSetChanged();
    }


}