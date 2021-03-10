package com.example.assignment2.ui.person.account;

import android.os.Bundle;

import com.example.assignment2.database.PersonDatabase;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import android.view.View;

import com.example.assignment2.R;

public class PersonActivity extends AppCompatActivity implements PersonDatabase.onResult{

    public static NavController navController;
    public static PersonDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        db = new PersonDatabase();
        db.setCurrentUser(getIntent().getExtras().getString("icNumber"), this);

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.personNavHostFragment);
        navController = navHostFragment.getNavController();
    }

    @Override
    public void onResult() {

    }
}