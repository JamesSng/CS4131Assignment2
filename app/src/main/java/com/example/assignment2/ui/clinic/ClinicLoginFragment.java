package com.example.assignment2.ui.clinic;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
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
import com.example.assignment2.database.ClinicDatabase;

import java.io.IOException;

public class ClinicLoginFragment extends Fragment {

    private ClinicLoginViewModel clinicLoginViewModel;
    String username = "";

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        clinicLoginViewModel =
                new ViewModelProvider(this).get(ClinicLoginViewModel.class);
        try {
            clinicLoginViewModel.loadDB(((MainActivity) getActivity()).readFile("clinic"));
        }
        catch (IOException ex){
            Toast.makeText(getContext(), "Unable to read clinic file", Toast.LENGTH_LONG).show();
        }

        View view = inflater.inflate(R.layout.fragment_clinic_login, container, false);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_dropdown_item_1line, clinicLoginViewModel.getDB().getNames());
        AutoCompleteTextView clinicName = view.findViewById(R.id.clinicName);
        clinicName.setAdapter(adapter);
        clinicName.setOnItemClickListener((parent, view1, position, id) -> {
            username = (String) parent.getItemAtPosition(position);
        });

        Button loginButton = view.findViewById(R.id.loginButton);
        loginButton.setOnClickListener(this::handleLogin);

        NavHostFragment navHostFragment = (NavHostFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.mainNavHostFragment);
        NavController navController = navHostFragment.getNavController();

        view.findViewById(R.id.toPersonTextView).setOnClickListener(
               v -> navController.navigate(R.id.action_clinicFragment_to_personFragment));

        view.findViewById(R.id.toAdminTextView).setOnClickListener(
                v -> navController.navigate(R.id.action_clinicFragment_to_personFragment));

        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(false);
    }

    public void handleLogin(View view){
        EditText passwordField = getView().findViewById(R.id.passwordField);
        String password = passwordField.getText().toString();
        if (username.length()==0){
            Toast.makeText(getContext(), "Please enter a username!", Toast.LENGTH_LONG).show();
            return;
        }
        if (password.length()==0){
            Toast.makeText(getContext(), "Please enter a password!", Toast.LENGTH_LONG).show();
            return;
        }
        int loginRes = clinicLoginViewModel.getDB().login(username, password);
        if (loginRes == ClinicDatabase.LOGIN_SUCCESSFUL){
            getActivity().getSharedPreferences("logged_in", Context.MODE_PRIVATE).edit().putInt("logged_in", 3).apply();
            getActivity().getSharedPreferences("username", Context.MODE_PRIVATE).edit().putString("username", username).apply();

            Toast.makeText(getContext(), "Welcome!", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(getContext(), EditPatientActivity.class);
            getContext().startActivity(intent);
        }
        else if (loginRes == ClinicDatabase.INVALID_PASSWORD){
            Toast.makeText(getContext(), "Invalid password!", Toast.LENGTH_LONG).show();
        }
    }
}