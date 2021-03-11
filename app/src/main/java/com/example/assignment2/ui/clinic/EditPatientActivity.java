package com.example.assignment2.ui.clinic;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.assignment2.AES;
import com.example.assignment2.QRCodeScannerActivity;
import com.example.assignment2.R;
import com.example.assignment2.database.PersonDatabase;

import java.util.ArrayList;

import static android.Manifest.permission.CAMERA;

public class EditPatientActivity extends AppCompatActivity implements PersonDatabase.onResult{

    private final int requestCode = 1;
    private String icNumber;
    PersonDatabase db = new PersonDatabase();
    private ArrayList<RadioButton> radioButtons = new ArrayList<>();

    private static final int PERMISSION_REQUEST_CODE = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_patient);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Button scanButton = findViewById(R.id.scanPatientButton);
        scanButton.setOnClickListener((view) -> {
            Log.i("Scan QR", "Start activity");
            if (!checkPermission()) requestPermission();
            else {
                Intent intent = new Intent(this, QRCodeScannerActivity.class);
                startActivityForResult(intent, requestCode);
            }
        });

        RadioButton unvaccinated = findViewById(R.id.unvaccinatedRadioButton);
        RadioButton firstShot = findViewById(R.id.firstShotRadioButton);
        RadioButton vaccinated = findViewById(R.id.vaccinatedRadioButton);
        RadioButton recovered = findViewById(R.id.recoveredRadioButton);
        radioButtons.add(unvaccinated);
        radioButtons.add(firstShot);
        radioButtons.add(vaccinated);
        radioButtons.add(recovered);

        RadioGroup radioGroup = findViewById(R.id.radioGroup);
        radioGroup.setOnCheckedChangeListener(this::checkedChanged);
    }

    public void checkedChanged(RadioGroup group, int pos){
        if (db.getCurrentUser() == null) return;
        String text = "Set " + db.getCurrentUser().getIcNumber() + "'s status to ";
        switch (pos){
            case 0: {
                db.setUnvaccinated();
                text += "unvaccinated";
            }
            case 1: {
                db.setFirstShot();
                text += "first shot";
            }
            case 2: {
                db.setVaccinated();
                text += "vaccinated";
            }
            case 3: {
                db.setRecovered();
                text += "recovered";
            }
            default: break;
        }
        Toast.makeText(this, text, Toast.LENGTH_LONG).show();
    }

    private void resetButtons(){
        for (RadioButton button: radioButtons){
            button.setChecked(false);
        }
    }

    private void setRadioButtons(int code){
        resetButtons();
        radioButtons.get(code).setChecked(true);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == requestCode) {
            if (resultCode == RESULT_OK) {
                String returnedResult = data.getData().toString();
                Log.i("Scan QR", "Returned " + returnedResult);
                loadData(returnedResult);
            }
        }
    }

    public void loadData(String encryptedIc){
        icNumber = AES.decrypt(encryptedIc, AES.secret);
        Log.i("Scan QR", "Returned " + icNumber);
        db.setCurrentUser(icNumber, this);
    }

    private boolean checkPermission(){
        int result = ContextCompat.checkSelfPermission(getApplicationContext(), CAMERA);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission(){
        ActivityCompat.requestPermissions(this, new String[]{CAMERA}, PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults){
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0) {
                boolean accepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                if (accepted) {
                    Intent intent = new Intent(this, QRCodeScannerActivity.class);
                    startActivity(intent);
                }
            }
        }
    }

    public void onResult(){
        TextView nameText = findViewById(R.id.nameText);
        nameText.setText(db.getCurrentUser().getName());
        TextView icText = findViewById(R.id.icText);
        icText.setText(icNumber);

        setRadioButtons((int) db.getCurrentUser().getVaccineStatus());
    }
}