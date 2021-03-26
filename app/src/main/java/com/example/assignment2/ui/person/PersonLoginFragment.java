package com.example.assignment2.ui.person;

import android.content.Context;
import android.os.Bundle;
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
import androidx.navigation.fragment.NavHostFragment;

import com.example.assignment2.MainActivity;
import com.example.assignment2.R;
import com.example.assignment2.database.PersonDatabase;

public class PersonLoginFragment extends Fragment implements PersonDatabase.onResult{

    private PersonDatabase database;
    private String username, password;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        PersonLoginViewModel personLoginViewModel = new ViewModelProvider(this).get(PersonLoginViewModel.class);
        View root = inflater.inflate(R.layout.fragment_person_login, container, false);

        database = personLoginViewModel.getDb();

        Button loginButton = root.findViewById(R.id.loginButton);
        loginButton.setOnClickListener(view -> {
            username = ((EditText)root.findViewById(R.id.usernameField)).getText().toString();
            password = ((EditText)root.findViewById(R.id.passwordField)).getText().toString();
            database.setCurrentUser(username, this);
        });

        NavHostFragment navHostFragment = (NavHostFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.mainNavHostFragment);
        NavController navController = navHostFragment.getNavController();

        root.findViewById(R.id.toAdminTextView).setOnClickListener(
                view -> navController.navigate(R.id.action_personFragment_to_adminFragment));

        root.findViewById(R.id.toClinicTextView).setOnClickListener(
                view -> navController.navigate(R.id.action_personFragment_to_clinicFragment));

        return root;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(false);
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
                getActivity().getSharedPreferences("logged_in", Context.MODE_PRIVATE).edit().putInt("logged_in", 1).apply();
                getActivity().getSharedPreferences("username", Context.MODE_PRIVATE).edit().putString("username", username).apply();

                NavHostFragment navHostFragment = (NavHostFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.mainNavHostFragment);
                NavController navController = navHostFragment.getNavController();
                Toast.makeText(getContext(), "Welcome!", Toast.LENGTH_LONG).show();

                MainActivity.db = database;

                navController.navigate(R.id.action_personFragment_to_personInfoFragment);
                break;
            default:
                Toast.makeText(getContext(), "Invalid login!", Toast.LENGTH_LONG).show();
                break;
        }
    }
}
