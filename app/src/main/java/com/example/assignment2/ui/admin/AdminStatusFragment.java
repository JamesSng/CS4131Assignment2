package com.example.assignment2.ui.admin;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

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
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.assignment2.AES;
import com.example.assignment2.QRCodeScannerActivity;
import com.example.assignment2.R;
import com.example.assignment2.database.PersonDatabase;
import com.example.assignment2.model.Person;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import static android.Manifest.permission.CAMERA;
import static android.app.Activity.RESULT_OK;

public class AdminStatusFragment extends Fragment implements PersonDatabase.onResult {

    private AdminStatusViewModel statusViewModel;
    private AdminLogViewModel logViewModel;
    private PersonDatabase db;

    private String IC;

    private final int requestCode = 1;

    private static final int PERMISSION_REQUEST_CODE = 200;

    public static AdminStatusFragment newInstance() {
        return new AdminStatusFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.admin_status_fragment, container, false);

        TextView textView = root.findViewById(R.id.welcomeTextView);


        NavController navController =
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

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        statusViewModel = new ViewModelProvider(requireActivity()).get(AdminStatusViewModel.class);
        logViewModel = new ViewModelProvider(requireActivity()).get(AdminLogViewModel.class);
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

    public void onResult(){
        ZonedDateTime now = ZonedDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy - HH:mm:ss");
        String date = now.format(formatter);

        if(db.getCurrentUser() == null) return;

        if(statusViewModel.hasName(IC)){
            statusViewModel.removeName(IC);
            Toast.makeText(getContext(), "Checked out!", Toast.LENGTH_SHORT).show();
            Person person = db.getCurrentUser();
            switch(person.getVaccineStatus()){
                case 0: case 1:
                    logViewModel.addLog("exit", IC, date, "Unvaccinated");
                    break;
                case 2: case 3:
                    logViewModel.addLog("exit", IC, date, "Vaccinated");
                    break;
            }
        } else {
            statusViewModel.addName(IC);
            Toast.makeText(getContext(), "Checked in!", Toast.LENGTH_SHORT).show();
            Person person = db.getCurrentUser();
            switch(person.getVaccineStatus()){
                case 0: case 1:
                    logViewModel.addLog("entry", IC, date, "Unvaccinated");
                    break;
                case 2: case 3:
                    logViewModel.addLog("entry", IC, date, "Vaccinated");
                    break;
            }
        }
    }

}