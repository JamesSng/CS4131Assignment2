package com.example.assignment2.ui.clinic;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.assignment2.R;

public class ClinicFragment extends Fragment {

    private ClinicViewModel clinicViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        clinicViewModel =
                new ViewModelProvider(this).get(ClinicViewModel.class);
        View root = inflater.inflate(R.layout.fragment_clinic, container, false);
        final TextView textView = root.findViewById(R.id.text_notifications);
        clinicViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}