package com.example.assignment2.ui.admin;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.assignment2.R;

public class AdminLogFragment extends Fragment {

    private AdminLogViewModel mViewModel;
    private RecyclerView recyclerView;

    public static AdminLogFragment newInstance() {
        return new AdminLogFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.admin_log_fragment, container, false);

        NavController navController =
                ((NavHostFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.mainNavHostFragment)).getNavController();


        root.findViewById(R.id.toStatusButton).setOnClickListener(v -> navController.navigate(R.id.action_adminLogFragment_to_adminStatusFragment));

        recyclerView = root.findViewById(R.id.recyclerView);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(requireActivity()).get(AdminLogViewModel.class);
        mViewModel.getRecyclerAdapter().observe(getViewLifecycleOwner(), recyclerAdapter -> {
            if(recyclerAdapter != null) recyclerView.setAdapter(recyclerAdapter);
        });

    }

}