package com.example.assignment2.ui.person.account;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class PersonQrViewModel extends ViewModel {
    public MutableLiveData<String> icNumber = new MutableLiveData<>();

    public LiveData<String> getIcNumber(){ return icNumber; }

    public void setIcNumber(String icNumber){ this.icNumber.setValue(icNumber); }
}