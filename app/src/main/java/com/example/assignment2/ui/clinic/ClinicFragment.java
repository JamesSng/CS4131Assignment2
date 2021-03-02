package com.example.assignment2.ui.clinic;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.assignment2.MainActivity;
import com.example.assignment2.R;
import com.example.assignment2.database.ClinicDatabase;
import com.example.assignment2.model.Clinic;

import java.io.IOException;

public class ClinicFragment extends Fragment {

    private ClinicViewModel clinicViewModel;
    String username = "";

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        clinicViewModel =
                new ViewModelProvider(this).get(ClinicViewModel.class);
        try {
            clinicViewModel.loadDB(((MainActivity) getActivity()).readFile("clinic"));
        }
        catch (IOException ex){
            Toast.makeText(getContext(), "Unable to read clinic file", Toast.LENGTH_LONG).show();
        }

        View view = inflater.inflate(R.layout.fragment_clinic, container, false);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_dropdown_item_1line, clinicViewModel.getDB().getNames());
        AutoCompleteTextView clinicName = view.findViewById(R.id.clinicName);
        clinicName.setAdapter(adapter);
        clinicName.setOnItemClickListener((parent, view1, position, id) -> {
            username = (String) parent.getItemAtPosition(position);
        });

        Button loginButton = view.findViewById(R.id.loginButton);
        loginButton.setOnClickListener(this::handleLogin);

        return view;
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
        int loginRes = clinicViewModel.getDB().login(username, password);
        if (loginRes == ClinicDatabase.LOGIN_SUCCESSFUL){
            Toast.makeText(getContext(), "Welcome!", Toast.LENGTH_LONG).show();
            //start next fragment
        }
        else if (loginRes == ClinicDatabase.INVALID_PASSWORD){
            Toast.makeText(getContext(), "Invalid password!", Toast.LENGTH_LONG).show();
        }
    }
}