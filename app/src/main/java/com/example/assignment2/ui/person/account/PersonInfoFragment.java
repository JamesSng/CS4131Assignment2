package com.example.assignment2.ui.person.account;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.birjuvachhani.avatarview.AvatarView;
import com.example.assignment2.MainActivity;
import com.example.assignment2.R;
import com.example.assignment2.model.Person;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Objects;

import static android.Manifest.permission.CAMERA;
import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static com.example.assignment2.ui.person.account.PersonActivity.db;
import static com.example.assignment2.ui.person.account.PersonActivity.navController;

public class PersonInfoFragment extends Fragment {

    private PersonInfoViewModel mViewModel;
    private AvatarView avatar;
    private TextView welcomeTV, vaccineStatusTV, statusInfoTV;

    private static final int PERMISSION_REQUEST_CAMERA = 0;
    private static final int PERMISSION_REQUEST_CODE = 200;


    public static PersonInfoFragment newInstance() {
        return new PersonInfoFragment();
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.person_info_fragment, container, false);

        avatar = root.findViewById(R.id.avatar);
        avatar.setOnClickListener(view -> selectImage(getContext()));

        welcomeTV = root.findViewById(R.id.welcomeTV);
        vaccineStatusTV = root.findViewById(R.id.vaccineStatusTV);
        statusInfoTV = root.findViewById(R.id.statusInfoTV);

        root.findViewById(R.id.button).setOnClickListener(view -> {
            Log.e("switch", "to qr fragment");
            navController.navigate(R.id.personInfo_to_personQR);
        });

        return root;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(requireActivity()).get(PersonInfoViewModel.class);
        new Handler().postDelayed(() -> mViewModel.setPerson(db.getCurrentUser()), 1000);

        mViewModel.getPerson().observe(getViewLifecycleOwner(), person -> {
            welcomeTV.setText("Welcome, " + person.getName());
            avatar.setInitials(person.getName());
            switch(person.getVaccineStatus()){
                case Person.UNVACCINATED:
                    vaccineStatusTV.setText("Unvaccinated");
                    vaccineStatusTV.setTextColor(Color.rgb(255,0,0));
                    statusInfoTV.setText(R.string.unvaccinated_info);
                    break;
                case Person.FIRST_SHOT:
                    vaccineStatusTV.setText("First Shot");
                    vaccineStatusTV.setTextColor(Color.rgb(250,218,94));
                    statusInfoTV.setText(R.string.first_shot_info);
                    break;
                case Person.VACCINATED:
                    vaccineStatusTV.setText("Vaccinated");
                    vaccineStatusTV.setTextColor(Color.rgb(152,251,152));
                    statusInfoTV.setText(R.string.vaccinated_info);
                    break;
                case Person.RECOVERED:
                    vaccineStatusTV.setText("Recovered");
                    vaccineStatusTV.setTextColor(Color.rgb(80,220,100));
                    statusInfoTV.setText(R.string.recovered_info);
                    break;
            }
        });

        mViewModel.getBitmap().observe(getViewLifecycleOwner(), bitmap -> {
            if(bitmap != null){
                avatar.setImageBitmap(bitmap);
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode != RESULT_CANCELED) {
            switch (requestCode) {
                case 0:
                    if (resultCode == RESULT_OK && data != null) {
                        try{
                            Intent cropIntent = new Intent("com.android.camera.action.CROP");
                            Uri uri = getImageUri(getContext(), data.getExtras().getParcelable("data"));
                            cropIntent.setDataAndType(uri, "image/*");

                            cropIntent.putExtra("crop", "true");
                            cropIntent.putExtra("aspectX", 1);
                            cropIntent.putExtra("aspectY", 1);
                            cropIntent.putExtra("outputX", 400);
                            cropIntent.putExtra("outputY", 400);
                            cropIntent.putExtra("return-data", true);
                            cropIntent.putExtra(MediaStore.EXTRA_OUTPUT, data.getData());
                            startActivityForResult(cropIntent, 1);
                        } catch (ActivityNotFoundException e){
                            String errorMessage = "Whoops - your device doesn't support the crop action!";
                            Toast toast = Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT);
                            toast.show();
                        }
                    }

                    break;
                case 1:
                    if (resultCode == RESULT_OK && data != null) {
                        Bitmap bitmap = data.getExtras().getParcelable("data");
                        mViewModel.setBitmap(bitmap);
                    }
                    break;
            }
        }
    }

    private boolean checkPermission(){
        int result = ContextCompat.checkSelfPermission(getContext().getApplicationContext(), CAMERA);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission(){
        ActivityCompat.requestPermissions(getActivity(), new String[]{CAMERA}, PERMISSION_REQUEST_CODE);
    }

    private void selectImage(Context context) {
        final CharSequence[] options = { "Take Photo", "Choose from Gallery","Cancel" };

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Choose your profile picture");

        builder.setItems(options, (dialog, item) -> {

            if (options[item].equals("Take Photo")) {
                if (checkPermission()) requestPermission();
                Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                try{
                    startActivityForResult(takePicture, 0);
                } catch(ActivityNotFoundException e){
                    //
                }
            } else if (options[item].equals("Choose from Gallery")) {
                Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(pickPhoto , 0);

            } else if (options[item].equals("Cancel")) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

}