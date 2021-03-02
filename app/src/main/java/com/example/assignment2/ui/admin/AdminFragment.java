package com.example.assignment2.ui.admin;

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
import com.google.android.material.snackbar.Snackbar;

public class AdminFragment extends Fragment {

    private AdminViewModel adminViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        adminViewModel =
                new ViewModelProvider(this).get(AdminViewModel.class);
        View root = inflater.inflate(R.layout.fragment_admin, container, false);

        /*adminViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });*/

        root.findViewById(R.id.loginButton).setOnClickListener(view -> {
            String username = ((EditText)root.findViewById(R.id.usernameField)).getText().toString();
            String password = ((EditText)root.findViewById(R.id.passwordField)).getText().toString();

        });

        return root;
    }

}