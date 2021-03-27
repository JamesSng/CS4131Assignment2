package com.example.assignment2.ui.admin;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.assignment2.AES;
import com.example.assignment2.MainActivity;
import com.example.assignment2.QRCodeScannerActivity;
import com.example.assignment2.R;
import com.example.assignment2.database.LogDatabase;
import com.example.assignment2.database.PersonDatabase;
import com.example.assignment2.model.LogEntry;
import com.example.assignment2.model.Person;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import static android.Manifest.permission.CAMERA;
import static android.app.Activity.RESULT_OK;

public class AdminStatusFragment extends Fragment implements PersonDatabase.onResult, LogDatabase.onResult {

    private AdminStatusViewModel statusViewModel;
    private AdminLogViewModel logViewModel;
    private PersonDatabase db;
    private NavController navController;

    private String IC, username;

    private final int requestCode = 1;

    private static final int PERMISSION_REQUEST_CODE = 200;

    private LogDatabase logDatabase;

    private View root;

    public static AdminStatusFragment newInstance() {
        return new AdminStatusFragment();
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.admin_status_fragment, container, false);

        TextView textView = root.findViewById(R.id.welcomeTextView);

        username = getActivity().getSharedPreferences("username", Context.MODE_PRIVATE).getString("username", null);
        textView.setText("Welcome, " + username);

        logDatabase = new LogDatabase();

        logDatabase.getLogs(username, this);

        navController =
                ((NavHostFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.mainNavHostFragment)).getNavController();

        root.findViewById(R.id.viewLogButton).setOnClickListener(v -> navController.navigate(R.id.action_adminStatusFragment_to_adminLogFragment));

        root.findViewById(R.id.scanQRButton).setOnClickListener(v -> {
            if (!checkPermission()) requestPermission();
            else {
                Intent intent = new Intent(getContext(), QRCodeScannerActivity.class);
                startActivityForResult(intent, requestCode);
            }
        });

        return root;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        statusViewModel = new ViewModelProvider(this).get(AdminStatusViewModel.class);
        logViewModel = new ViewModelProvider(requireActivity()).get(AdminLogViewModel.class);

        statusViewModel.getNames().observe(getViewLifecycleOwner(), names -> {
            TextView textView = root.findViewById(R.id.personCountTV);
            textView.setText(""+names.size());
        });

        statusViewModel.getUnvaccinated().observe(getViewLifecycleOwner(), unvaccinated -> {
            TextView textView = root.findViewById(R.id.unvaccinatedCountTV);
            textView.setText(""+unvaccinated);
        });
    }

    private boolean checkPermission(){
        int result = ContextCompat.checkSelfPermission(getActivity().getApplicationContext(), CAMERA);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission(){
        ActivityCompat.requestPermissions(getActivity(), new String[]{CAMERA}, PERMISSION_REQUEST_CODE);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data){
        Log.i("Scan QR", "onActivityResult " + requestCode + " " + resultCode);
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == this.requestCode) {
            if (resultCode == RESULT_OK) {
                String returnedResult = data.getStringExtra("result");
                Log.i("Scan QR", "Returned " + returnedResult);
                processIC(AES.decrypt(returnedResult, "secret"));
            }
        }
    }

    private void processIC(String IC){
        this.IC = IC;
        Log.e("check ic", IC);
        db = new PersonDatabase();
        if(IC != null) db.setCurrentUser(IC, this);
    }

    @SuppressLint("SetTextI18n")
    public void onResult(){
        ZonedDateTime now = ZonedDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy - HH:mm:ss");
        String date = now.format(formatter);

        if(db.getCurrentUser() == null) return;

        String enter = "", vaccineStatus = "";

        if(statusViewModel.hasName(IC)){
            statusViewModel.removeName(IC);
            Toast.makeText(getContext(), "Checked out!", Toast.LENGTH_SHORT).show();
            Person person = db.getCurrentUser();
            switch(person.getVaccineStatus()){
                case 0: case 1:
                    enter = "exit";
                    vaccineStatus = "Unvaccinated";
                    statusViewModel.removeUnvaccinated();
                    break;
                case 2: case 3:
                    enter = "exit";
                    vaccineStatus = "Vaccinated";
                    break;
            }
        } else {
            statusViewModel.addName(IC);
            Toast.makeText(getContext(), "Checked in!", Toast.LENGTH_SHORT).show();
            Person person = db.getCurrentUser();
            switch(person.getVaccineStatus()){
                case 0: case 1:
                    enter = "entry";
                    vaccineStatus = "Unvaccinated";
                    statusViewModel.addUnvaccinated();
                    break;
                case 2: case 3:
                    enter = "entry";
                    vaccineStatus = "Vaccinated";
                    break;
            }
        }

        logViewModel.addLog(enter, IC, date, vaccineStatus);
        logDatabase.addLogs(new LogEntry(username, enter, IC, date, vaccineStatus));

        Log.e("add log", username+" "+enter+" "+IC+" "+date+" "+vaccineStatus);
        TextView textView = root.findViewById(R.id.personCountTV);
        textView.setText(""+statusViewModel.getNames().getValue().size());

        textView = root.findViewById(R.id.unvaccinatedCountTV);
        textView.setText(""+statusViewModel.getUnvaccinated().getValue());
    }

    @SuppressLint("SetTextI18n")
    public void onResult(ArrayList<LogEntry> logs){
        for(int i=0;i<logs.size();++i){
            if(logs.get(i).getEnter().equals("entry")){
                statusViewModel.addName(logs.get(i).getName());
                Log.e("entry",logs.get(i).getName());
                if(logs.get(i).getVaccineStatus().equals("Unvaccinated")) statusViewModel.addUnvaccinated();
            } else {
                statusViewModel.removeName(logs.get(i).getName());
                Log.e("exit",logs.get(i).getName());
                if(logs.get(i).getVaccineStatus().equals("Unvaccinated")) statusViewModel.removeUnvaccinated();
            }

            Log.e("person count", ""+statusViewModel.getNames().getValue().size());

            TextView textView = root.findViewById(R.id.personCountTV);
            textView.setText(""+statusViewModel.getNames().getValue().size());

            textView = root.findViewById(R.id.unvaccinatedCountTV);
            textView.setText(""+statusViewModel.getUnvaccinated().getValue());
        }
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
}