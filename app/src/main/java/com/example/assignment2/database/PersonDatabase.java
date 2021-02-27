
package com.example.assignment2.database;

import androidx.annotation.NonNull;

import com.example.assignment2.model.Person;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class PersonDatabase {

    DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("persons");
    public static int LOGIN_SUCCESSFUL = 0;
    public static int INVALID_IC_NUMBER = 1;
    public static int INVALID_PASSWORD = 2;
    Person currentUser;

    public boolean getUser(String icNumber){
        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                currentUser = snapshot.child("icNumber").getValue(Person.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                currentUser = null;
            }
        });
        return (currentUser == null);
    }

    public int login(String icNumber, String password){
        if (!getUser(icNumber)) return INVALID_IC_NUMBER;
        //TODO
        return LOGIN_SUCCESSFUL;
    }


}
