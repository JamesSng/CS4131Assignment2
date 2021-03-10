package com.example.assignment2.ui.person.account;

import androidx.lifecycle.ViewModelProvider;

import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.assignment2.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import static com.example.assignment2.ui.person.account.PersonActivity.navController;

public class PersonInfoFragment extends Fragment {

    private PersonInfoViewModel mViewModel;

    public static PersonInfoFragment newInstance() {
        return new PersonInfoFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.person_info_fragment, container, false);
        FloatingActionButton fab = root.findViewById(R.id.fab);

        fab.setOnClickListener(view -> {
            navController.navigate(R.id.action_personInfoFragment_to_personQrFragment);
        });
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(PersonInfoViewModel.class);
        // TODO: Use the ViewModel
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

}