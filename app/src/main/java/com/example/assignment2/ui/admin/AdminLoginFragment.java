package com.example.assignment2.ui.admin;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.example.assignment2.MainActivity;
import com.example.assignment2.R;
import com.example.assignment2.database.AdminDatabase;

import java.io.IOException;

public class AdminLoginFragment extends Fragment {

    private AdminLoginViewModel adminLoginViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        adminLoginViewModel =
                new ViewModelProvider(this).get(AdminLoginViewModel.class);

        try {
            adminLoginViewModel.loadDB(((MainActivity) getActivity()).readFile("admin"));
        }
        catch (IOException ex){
            Toast.makeText(getContext(), "Unable to read admin file", Toast.LENGTH_LONG).show();
        }

        View root = inflater.inflate(R.layout.fragment_admin_login, container, false);

        NavHostFragment navHostFragment = (NavHostFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.mainNavHostFragment);
        NavController navController = navHostFragment.getNavController();

        root.findViewById(R.id.loginButton).setOnClickListener(this::handleLogin);

        root.findViewById(R.id.toPersonTextView).setOnClickListener(
                view -> navController.navigate(R.id.action_adminFragment_to_personFragment));

        root.findViewById(R.id.toClinicTextView).setOnClickListener(
                view -> navController.navigate(R.id.action_adminFragment_to_clinicFragment));

        return root;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(false);
    }

    public void handleLogin(View view){
        EditText passwordField = getView().findViewById(R.id.passwordField);
        String password = passwordField.getText().toString();
        String username = ((EditText) getView().findViewById(R.id.usernameField)).getText().toString();
        if (username.length()==0){
            Toast.makeText(getContext(), "Please enter a username!", Toast.LENGTH_LONG).show();
            return;
        }
        if (password.length()==0){
            Toast.makeText(getContext(), "Please enter a password!", Toast.LENGTH_LONG).show();
            return;
        }
        int loginRes = adminLoginViewModel.getDB().login(username, password);
        if (loginRes == AdminDatabase.LOGIN_SUCCESSFUL){
            getActivity().getSharedPreferences("logged_in", Context.MODE_PRIVATE).edit().putInt("logged_in", 2).apply();
            getActivity().getSharedPreferences("username", Context.MODE_PRIVATE).edit().putString("username", username).apply();

            Toast.makeText(getContext(), "Welcome!", Toast.LENGTH_LONG).show();
            NavController navController =
                    ((NavHostFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.mainNavHostFragment)).getNavController();

            navController.navigate(R.id.action_adminFragment_to_adminStatusFragment);
        }
        else if (loginRes == AdminDatabase.INVALID_PASSWORD){
            Toast.makeText(getContext(), "Invalid password!", Toast.LENGTH_LONG).show();
        }
    }

}