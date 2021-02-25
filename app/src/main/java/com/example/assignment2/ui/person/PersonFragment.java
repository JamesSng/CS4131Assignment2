package com.example.assignment2.ui.person;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.assignment2.R;

public class PersonFragment extends Fragment {

    private PersonViewModel personViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        personViewModel =
                new ViewModelProvider(this).get(PersonViewModel.class);
        View root = inflater.inflate(R.layout.fragment_person, container, false);

        root.findViewById(R.id.loginButton).setOnClickListener(view -> {
            String username = ((EditText)root.findViewById(R.id.usernameField)).getText().toString();
            String password = ((EditText)root.findViewById(R.id.passwordField)).getText().toString();
            Pair<Boolean, String> p = validateLogin(username, password);
            if(p.t){
                System.out.println(p.u);
                System.out.println("Logging you in...");
            } else {
                System.out.println(p.u);
            }
        });

        root.findViewById(R.id.registerButton).setOnClickListener(view -> {
            String username = ((EditText)root.findViewById(R.id.usernameField)).getText().toString();
            String password = ((EditText)root.findViewById(R.id.passwordField)).getText().toString();
            Pair<Boolean, String> p = validateRegistration(username, password);
            if(p.t){
                System.out.println(p.u);
                System.out.println("Registering your account...");
            } else {
                System.out.println(p.u);
            }
        });
        /*final TextView textView = root.findViewById(R.id.text_home);
        personViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });*/
        return root;
    }

    private Pair<Boolean, String> validateLogin(String username, String password){
        /**
         * If username/IC is not found in the database, return false
         * If the username/IC has not been registered, return false
         * return true
         * */
        return new Pair<>(true, "Welcome!");
    }

    private Pair<Boolean, String> validateRegistration(String username, String password){
        /**
         * If username/IC is not found in the database, return false
         * If the username/IC has been registered, return false
         * return true
         */
        return new Pair<>(true, "Welcome!");
    }
}

class Pair<T, U> {
    public final T t;
    public final U u;

    public Pair(T t, U u) {
        this.t= t;
        this.u= u;
    }
}