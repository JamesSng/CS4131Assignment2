package com.example.assignment2.ui.person.login;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.assignment2.R;
import com.example.assignment2.database.PersonDatabase;
import com.example.assignment2.ui.person.account.PersonActivity;
import com.google.android.material.snackbar.Snackbar;

public class PersonFragment extends Fragment {

    private PersonViewModel personViewModel;
    private PersonDatabase database;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        personViewModel =
                new ViewModelProvider(this).get(PersonViewModel.class);
        View root = inflater.inflate(R.layout.fragment_person_login, container, false);

        database = new PersonDatabase();

        NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);

        Button loginButton = root.findViewById(R.id.loginButton);
        loginButton.setOnClickListener(view -> {
            String username = ((EditText)root.findViewById(R.id.usernameField)).getText().toString();
            String password = ((EditText)root.findViewById(R.id.passwordField)).getText().toString();
            int result = database.login(username, password);
            Log.i("PersonFragment", "Result returned was " + result);
            switch (result){
                case PersonDatabase.INVALID_IC_NUMBER:
                    Snackbar.make(view, "Invalid IC number!", Snackbar.LENGTH_SHORT);
                    break;
                case PersonDatabase.INVALID_PASSWORD:
                    Snackbar.make(view, "Invalid password!", Snackbar.LENGTH_SHORT);
                    break;
                case PersonDatabase.LOGIN_SUCCESSFUL:
                    Intent intent = new Intent(getContext(), PersonActivity.class);
                    startActivity(intent);
                    break;
                default:
                    Snackbar.make(view, "Invalid login!", Snackbar.LENGTH_SHORT);
                    break;
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


}
