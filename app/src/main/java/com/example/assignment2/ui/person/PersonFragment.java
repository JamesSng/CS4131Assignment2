package com.example.assignment2.ui.person;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.assignment2.R;
import com.example.assignment2.model.Pair;
import com.example.assignment2.ui.person.account.PersonActivity;
import com.google.android.material.snackbar.Snackbar;

public class PersonFragment extends Fragment {

    private PersonViewModel personViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        personViewModel =
                new ViewModelProvider(this).get(PersonViewModel.class);
        View root = inflater.inflate(R.layout.fragment_person_login, container, false);

        root.findViewById(R.id.loginButton).setOnClickListener(view -> {
            String username = ((EditText)root.findViewById(R.id.usernameField)).getText().toString();
            String password = ((EditText)root.findViewById(R.id.passwordField)).getText().toString();
            Pair<Boolean, String> p = validateLogin(username, password);
            if(p.t){
                Intent intent = new Intent(getContext(), PersonActivity.class);
                intent.putExtra("username", username);
                startActivity(intent);
            } else {
                Snackbar.make(root, p.u, Snackbar.LENGTH_SHORT);
            }
        });

        root.findViewById(R.id.registerButton).setOnClickListener(view -> {
            String username = ((EditText)root.findViewById(R.id.usernameField)).getText().toString();
            String password = ((EditText)root.findViewById(R.id.passwordField)).getText().toString();
            Pair<Boolean, String> p = validateRegistration(username, password);
            if(p.t){
                addToFirebase(username, password);
                Intent intent = new Intent(getContext(), PersonActivity.class);
                intent.putExtra("username", username);
                startActivity(intent);
            } else {
                Snackbar.make(root, p.u, Snackbar.LENGTH_SHORT);
            }
        });
        /*
        personViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });*/
        return root;
    }

    private void addToFirebase(String username, String password){
        /**
         * TODO
         * */
    }

    private Pair<Boolean, String> validateLogin(String username, String password){
        /**
         * TODO
         * If username/IC is not found in the database, return false
         * If the username/IC has not been registered, return false
         * return true
         * */
        return new Pair<>(true, "Welcome!");
    }

    private Pair<Boolean, String> validateRegistration(String username, String password){
        /**
         * TODO
         * If username/IC is not found in the database, return false
         * If the username/IC has been registered, return false
         * return true
         */
        return new Pair<>(true, "Welcome!");
    }
}
