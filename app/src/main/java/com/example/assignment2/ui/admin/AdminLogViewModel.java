package com.example.assignment2.ui.admin;

import android.widget.LinearLayout;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

public class AdminLogViewModel extends ViewModel {

    private MutableLiveData<ArrayList<String>>
            names = new MutableLiveData<>(new ArrayList<>()),
            times = new MutableLiveData<>(new ArrayList<>()),
            enter = new MutableLiveData<>(new ArrayList<>()),
            vaccineStatuses = new MutableLiveData<>(new ArrayList<>());

    private MutableLiveData<RecyclerAdapter> recyclerAdapter = new MutableLiveData<>(null);

    public LiveData<RecyclerAdapter> getRecyclerAdapter(){ return recyclerAdapter; }

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