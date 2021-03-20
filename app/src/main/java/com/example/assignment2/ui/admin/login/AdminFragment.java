package com.example.assignment2.ui.admin.login;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.assignment2.MainActivity;
import com.example.assignment2.R;
import com.example.assignment2.database.AdminDatabase;

import java.io.IOException;

public class AdminFragment extends Fragment {

    private AdminViewModel adminViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        adminViewModel =
                new ViewModelProvider(this).get(AdminViewModel.class);

        try {
            adminViewModel.loadDB(((MainActivity) getActivity()).readFile("admin"));
        }
        catch (IOException ex){
            Toast.makeText(getContext(), "Unable to read admin file", Toast.LENGTH_LONG).show();
        }

        View root = inflater.inflate(R.layout.fragment_admin, container, false);

        root.findViewById(R.id.loginButton).setOnClickListener(this::handleLogin);

        return root;
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
        int loginRes = adminViewModel.getDB().login(username, password);
        if (loginRes == AdminDatabase.LOGIN_SUCCESSFUL){
            Toast.makeText(getContext(), "Welcome!", Toast.LENGTH_LONG).show();
            //Intent intent = new Intent(getContext(), EditPatientActivity.class);
            //getContext().startActivity(intent);
        }
        else if (loginRes == AdminDatabase.INVALID_PASSWORD){
            Toast.makeText(getContext(), "Invalid password!", Toast.LENGTH_LONG).show();
        }
    }

}