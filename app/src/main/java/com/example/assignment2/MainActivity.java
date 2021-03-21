package com.example.assignment2;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.example.assignment2.database.PersonDatabase;
import com.example.assignment2.onboarding.OnboardingActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity {

    PersonDatabase personDatabase = new PersonDatabase();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        SharedPreferences sharedPreferences = getSharedPreferences("started_before", Context.MODE_PRIVATE);
        int defaultVal = getResources().getInteger(R.integer.DEFAULT);
        int value = sharedPreferences.getInt("started_before", defaultVal);
        Log.e("Shared Preferences", value+"");
        if(value == 0){
            startActivity(new Intent(this, OnboardingActivity.class));
        } else {
            BottomNavigationView navView = findViewById(R.id.nav_view);
            // Passing each menu ID as a set of Ids because each
            // menu should be considered as top level destinations.
            AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                    R.id.navigation_person, R.id.navigation_admin, R.id.navigation_clinic)
                    .build();
            NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
            NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
            NavigationUI.setupWithNavController(navView, navController);
        }
    }

    public String readFile(String filename) throws IOException{
        InputStreamReader inputStreamReader = new InputStreamReader(getResources().getAssets().open(filename));
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        StringBuilder stringBuffer = new StringBuilder();
        String lines;
        while ((lines = bufferedReader.readLine()) != null){
            stringBuffer.append(lines).append("\n");
        }
        inputStreamReader.close();
        bufferedReader.close();
        return stringBuffer.toString().trim();
    }
}

/**
 * We're no strangers to love
 * You know the rules and so do I
 * A full commitment's what I'm thinking of
 * You wouldn't get this from any other guy
 * I just wanna tell you how I'm feeling
 * Gotta make you understand
 *
 * Never gonna give you up
 * Never gonna let you down
 * Never gonna run around and desert you
 * Never gonna make you cry
 * Never gonna say goodbye
 * Never gonna tell a lie and hurt you
 *
 * We know each other, for so long
 * Your heart's been aching but
 * You're too shy to say it
 * Inside we both know what's been going on
 * You know the game and we're gonna play it
 *
 * And if you ask me how I'm feeling
 * Don't tell me you're too blind to see
 *
 * Never gonna give you up
 * Never gonna let you down
 * Never gonna run around and desert you
 * Never gonna make you cry
 * Never gonna say goodbye
 * Never gonna tell a lie and hurt you
 *
 * Ooooh, give you up
 * Ooooh, give you up
 * Never gonna give
 * Never gonna give, give you up
 * Ooooh
 * Never gonna give
 * Never gonna give
 */