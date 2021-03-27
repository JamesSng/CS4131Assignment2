package com.example.assignment2;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.assignment2.database.PersonDatabase;
import com.example.assignment2.onboarding.OnboardingActivity;
import com.example.assignment2.ui.clinic.EditPatientActivity;
import com.example.assignment2.ui.person.PersonInfoViewModel;
import com.example.assignment2.ui.person.PersonQrViewModel;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity {

    public static PersonDatabase db;
    public static String username;

    private PersonInfoViewModel personInfoViewModel;
    private PersonQrViewModel personQrViewModel;

    private NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.mainNavHostFragment);
        navController = navHostFragment.getNavController();

        final int defaultVal = getResources().getInteger(R.integer.DEFAULT);
        int value = getSharedPreferences("started_before", Context.MODE_PRIVATE).getInt("started_before", defaultVal);
        if (value == 0) {
            startActivity(new Intent(this, OnboardingActivity.class));
        } else {
            value = getSharedPreferences("logged_in", Context.MODE_PRIVATE).getInt("logged_in", defaultVal);
            username = getSharedPreferences("username", Context.MODE_PRIVATE).getString("username", null);
            Intent intent;
            switch (value) {
                case 0:
                    break;
                case 1:
                    // login to user
                    personInfoViewModel = new ViewModelProvider(this).get(PersonInfoViewModel.class);
                    personQrViewModel = new ViewModelProvider(this).get(PersonQrViewModel.class);

                    db = new PersonDatabase();
                    db.setCurrentUser(username, this::onResult);
                    personInfoViewModel.setPerson(db.getCurrentUser());
                    personQrViewModel.setIcNumber(username);

                    navController.navigate(R.id.action_personFragment_to_personInfoFragment);
                    break;
                case 2:
                    // login to admin
                    getSharedPreferences("updated", Context.MODE_PRIVATE).edit().putString("updated", "no").apply();
                    navController.navigate(R.id.action_personFragment_to_adminFragment);
                    navController.navigate(R.id.action_adminFragment_to_adminStatusFragment);
                    break;
                case 3:
                    // login to clinic
                    intent = new Intent(this, EditPatientActivity.class);
                    intent.putExtra("username", username);
                    startActivity(intent);
                    break;
            }
        }
    }

    public String readFile(String filename) throws IOException {
        InputStreamReader inputStreamReader = new InputStreamReader(getResources().getAssets().open(filename));
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        StringBuilder stringBuffer = new StringBuilder();
        String lines;
        while ((lines = bufferedReader.readLine()) != null) {
            stringBuffer.append(lines).append("\n");
        }
        inputStreamReader.close();
        bufferedReader.close();
        return stringBuffer.toString().trim();
    }

    @Override
    public void onBackPressed() {

    }

    public void onResult() {

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