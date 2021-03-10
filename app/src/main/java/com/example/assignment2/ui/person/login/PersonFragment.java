package com.example.assignment2.ui.person.login;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.assignment2.R;
import com.example.assignment2.database.PersonDatabase;
import com.example.assignment2.ui.person.account.PersonActivity;
import com.google.android.material.snackbar.Snackbar;

public class PersonFragment extends Fragment implements PersonDatabase.onResult{

    private PersonViewModel personViewModel;
    private PersonDatabase database;
    private String username, password;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        personViewModel =
                new ViewModelProvider(this).get(PersonViewModel.class);
        View root = inflater.inflate(R.layout.fragment_person_login, container, false);

        database = new PersonDatabase();

        Button loginButton = root.findViewById(R.id.loginButton);
        loginButton.setOnClickListener(view -> {
            username = ((EditText)root.findViewById(R.id.usernameField)).getText().toString();
            password = ((EditText)root.findViewById(R.id.passwordField)).getText().toString();
            database.setCurrentUser(username, this);
        });

        return root;
    }

    public void onResult(){
        int result = database.login(password);
        switch (result){
            case PersonDatabase.INVALID_IC_NUMBER:
                Toast.makeText(getContext(), "Invalid IC number!", Toast.LENGTH_LONG).show();
                break;
            case PersonDatabase.INVALID_PASSWORD:
                Toast.makeText(getContext(), "Invalid password!", Toast.LENGTH_LONG).show();
                break;
            case PersonDatabase.LOGIN_SUCCESSFUL:
                Intent intent = new Intent(getContext(), PersonActivity.class);
                Toast.makeText(getContext(), "Welcome!", Toast.LENGTH_LONG).show();
                startActivity(intent);
                break;
            default:
                Toast.makeText(getContext(), "Invalid login!", Toast.LENGTH_LONG).show();
                break;
        }
    }
}
