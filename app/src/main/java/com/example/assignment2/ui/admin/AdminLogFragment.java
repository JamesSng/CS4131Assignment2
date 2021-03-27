package com.example.assignment2.ui.admin;

import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.assignment2.R;
import com.example.assignment2.database.LogDatabase;
import com.example.assignment2.model.LogEntry;

import java.util.ArrayList;

public class AdminLogFragment extends Fragment implements LogDatabase.onResult{

    private AdminLogViewModel mViewModel;
    private RecyclerView recyclerView;
    private NavController navController;
    private String username;
    private LogDatabase logDatabase;

    public static AdminLogFragment newInstance() {
        return new AdminLogFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.admin_log_fragment, container, false);

        username = getActivity().getSharedPreferences("username", Context.MODE_PRIVATE).getString("username", null);

        logDatabase = new LogDatabase();

        navController =
                ((NavHostFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.mainNavHostFragment)).getNavController();


        root.findViewById(R.id.toStatusButton).setOnClickListener(v -> navController.navigate(R.id.action_adminLogFragment_to_adminStatusFragment));

        recyclerView = root.findViewById(R.id.recyclerView);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        if(getActivity().getSharedPreferences("updated", Context.MODE_PRIVATE).getString("updated", null).equals("no")){
            logDatabase.getLogs(username, this);
            getActivity().getSharedPreferences("updated", Context.MODE_PRIVATE).edit().putString("updated", "yes").apply();
        }

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

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_admin, menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.admin_logout:
                getActivity().getSharedPreferences("logged_in", Context.MODE_PRIVATE).edit().putInt("logged_in", 0).apply();
                Toast.makeText(getContext(),"See you next time!", Toast.LENGTH_SHORT).show();
                getActivity().getSharedPreferences("updated", Context.MODE_PRIVATE).edit().putString("updated", "no").apply();
                navController.navigate(R.id.adminFragment);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onResult(ArrayList<LogEntry> logs){
        for(LogEntry log: logs){
            mViewModel.addLog(log.enter, log.name, log.time, log.vaccineStatus);
        }
    }

}