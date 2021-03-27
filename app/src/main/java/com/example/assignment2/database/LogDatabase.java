package com.example.assignment2.database;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.assignment2.model.LogEntry;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class LogDatabase {


    DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("logs");
    private ArrayList<LogEntry> ret;

    public void getLogs(String adminName, onResult result){
        ret.clear();
        ChildEventListener listener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                LogEntry log = snapshot.getValue(LogEntry.class);
                if (log.getAdminName().equals(adminName)) ret.add(log);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                result.onResult(ret);
                dbRef.removeEventListener(listener);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        dbRef.orderByChild("adminName").addChildEventListener(listener);
    }

    public void addLogs(LogEntry log){
        DatabaseReference ref = dbRef.push();
        ref.setValue(log);
    }

    public interface onResult{
        public void onResult(ArrayList<LogEntry> logs);
    }

}
