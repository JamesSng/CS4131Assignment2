package com.example.assignment2.ui.clinic;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.assignment2.MainActivity;
import com.example.assignment2.R;

import java.io.IOException;

public class ClinicFragment extends Fragment {

    private ClinicViewModel clinicViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        clinicViewModel =
                new ViewModelProvider(this).get(ClinicViewModel.class);
        try {
            clinicViewModel.loadDB(((MainActivity) getActivity()).readFile("clinic.txt"));
        }
        catch (IOException ex){
            Toast.makeText(getContext(), "Unable to read clinic file", Toast.LENGTH_LONG).show();
        }
        return inflater.inflate(R.layout.fragment_clinic, container, false);
    }
}