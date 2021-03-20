package com.example.assignment2.ui.person.account;

import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.provider.MediaStore;
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


import java.util.Objects;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static com.example.assignment2.ui.person.account.PersonActivity.db;
import static com.example.assignment2.ui.person.account.PersonActivity.navController;

public class PersonInfoFragment extends Fragment {

    private PersonInfoViewModel mViewModel;
    private AvatarView avatar;
    private TextView welcomeTV, vaccineStatusTV, statusInfoTV;


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

        //root.findViewById(R.id.fab).setOnClickListener(view ->
        //        navController.navigate(R.id.action_personInfoFragment_to_personQrFragment));

        return root;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(requireActivity()).get(PersonInfoViewModel.class);
        new Handler().postDelayed(() -> mViewModel.setPerson(db.getCurrentUser()), 300);
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
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode != RESULT_CANCELED) {
            switch (requestCode) {
                case 0:
                    if (resultCode == RESULT_OK && data != null) {
                        Bitmap selectedImage = (Bitmap) data.getExtras().get("data");
                        mViewModel.setImageBitmap(selectedImage);
                    }

                    break;
                case 1:
                    if (resultCode == RESULT_OK && data != null) {
                        Uri selectedImage =  data.getData();
                        String[] filePathColumn = {MediaStore.Images.Media.DATA};
                        if (selectedImage != null) {
                            Cursor cursor = requireActivity().getContentResolver().query(selectedImage,
                                    filePathColumn, null, null, null);
                            if (cursor != null) {
                                cursor.moveToFirst();

                                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                                String picturePath = cursor.getString(columnIndex);
                                mViewModel.setImageBitmap(BitmapFactory.decodeFile(picturePath));
                                cursor.close();
                            }
                        }

                    }
                    break;
            }
        }
    }

    private void selectImage(Context context) {
        final CharSequence[] options = { "Take Photo", "Choose from Gallery","Cancel" };

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Choose your profile picture");

        builder.setItems(options, (dialog, item) -> {

            if (options[item].equals("Take Photo")) {
                Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                try{
                    startActivityForResult(takePicture, 0);
                } catch(ActivityNotFoundException e){
                    //
                }

            } else if (options[item].equals("Choose from Gallery")) {
                Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(pickPhoto , 1);

            } else if (options[item].equals("Cancel")) {
                dialog.dismiss();
            }
        });
        builder.show();
    }
}