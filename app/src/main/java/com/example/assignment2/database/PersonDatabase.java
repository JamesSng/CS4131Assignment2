package com.example.assignment2.database;

 import android.util.Log;

import androidx.annotation.NonNull;

import com.example.assignment2.model.Person;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class PersonDatabase{

    DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("persons");
    public static final int LOGIN_SUCCESSFUL = 0;
    public static final int INVALID_IC_NUMBER = 1;
    public static final int INVALID_PASSWORD = 2;
    Person currentUser;

    public void setCurrentUser(String icNumber, onResult resultInterface){
        Log.i("PersonDatabase", "Username " + icNumber);
        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                currentUser = snapshot.child(icNumber).getValue(Person.class);
                resultInterface.onResult();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                resultInterface.onResult();
                currentUser = null;
            }
        });
        Log.i("PersonDatabase", "Set current user " + (currentUser == null));
    }

    public Person getCurrentUser(){
        return currentUser;
    }

    public int login(String password){
        if (currentUser == null || currentUser.getPassword() == null) {
            Log.i("PersonDatabase", "Invalid ic number");
            return INVALID_IC_NUMBER;
        }
        else if (!currentUser.getPassword().equals(password)){
            currentUser = null;
            Log.i("PersonDatabase", "Invalid password");
            return INVALID_PASSWORD;
        }
        Log.i("PersonDatabase", "Login successful");
        return LOGIN_SUCCESSFUL;
    }

    public void setUnvaccinated(){
        currentUser.setUnvaccinated();
        dbRef.child(currentUser.getIcNumber()).child("vaccineStatus")
                .setValue(currentUser.getVaccineStatus());
    }

    public void setFirstShot(){
        currentUser.setFirstShot();
        dbRef.child(currentUser.getIcNumber()).child("vaccineStatus")
                .setValue(currentUser.getVaccineStatus());
    }

    public void setVaccinated(){
        currentUser.setVaccinated();
        dbRef.child(currentUser.getIcNumber()).child("vaccineStatus")
                .setValue(currentUser.getVaccineStatus());
    }

    public void setRecovered(){
        currentUser.setRecovered();
        dbRef.child(currentUser.getIcNumber()).child("vaccineStatus")
                .setValue(currentUser.getVaccineStatus());
    }

    public interface onResult{
        void onResult();
    }
}
