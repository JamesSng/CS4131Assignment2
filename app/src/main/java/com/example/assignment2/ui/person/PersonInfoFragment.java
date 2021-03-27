package com.example.assignment2.ui.person;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.birjuvachhani.avatarview.AvatarView;
import com.example.assignment2.MainActivity;
import com.example.assignment2.R;
import com.example.assignment2.database.PersonDatabase;
import com.example.assignment2.model.Person;

import java.io.ByteArrayOutputStream;

import static android.Manifest.permission.CAMERA;
import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;


public class PersonInfoFragment extends Fragment implements PersonDatabase.onResult{

    private PersonInfoViewModel mViewModel;
    private AvatarView avatar;
    private TextView welcomeTV, vaccineStatusTV, statusInfoTV;

    private static final int PERMISSION_REQUEST_CAMERA = 0;
    private static final int PERMISSION_REQUEST_CODE = 200;


    public static PersonInfoFragment newInstance() {
        return new PersonInfoFragment();
    }

    private NavController navController;

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.person_info_fragment, container, false);

        PersonDatabase db = new PersonDatabase();
        db.setCurrentUser(getActivity().getSharedPreferences("username", Context.MODE_PRIVATE).getString("username", null), this);

        mViewModel = new ViewModelProvider(requireActivity()).get(PersonInfoViewModel.class);

        avatar = root.findViewById(R.id.avatar);
        avatar.setOnClickListener(view -> selectImage(getContext()));

        welcomeTV = root.findViewById(R.id.welcomeTV);
        vaccineStatusTV = root.findViewById(R.id.vaccineStatusTV);
        statusInfoTV = root.findViewById(R.id.statusInfoTV);

        NavHostFragment navHostFragment = (NavHostFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.mainNavHostFragment);
        navController = navHostFragment.getNavController();

        root.findViewById(R.id.button).setOnClickListener(view -> {
            Log.e("switch", "to qr fragment");
            navController.navigate(R.id.action_personInfoFragment_to_personQrFragment);
        });

        return root;
    }

    public void onResult(){
        mViewModel.setPerson(MainActivity.db.getCurrentUser());
        mViewModel.getPerson().observe(getViewLifecycleOwner(), person -> {
            if(person != null){
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
                if(!checkPermission()) requestPermission();
                if(checkPermission()){
                    Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    try{
                        startActivityForResult(takePicture, 0);
                    } catch(ActivityNotFoundException e){
                        //
                    }
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
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_person, menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.person_logout:
                getActivity().getSharedPreferences("logged_in", Context.MODE_PRIVATE).edit().putInt("logged_in", 0).apply();
                Toast.makeText(getContext(),"See you next time!", Toast.LENGTH_SHORT).show();
                navController.navigate(R.id.personFragment);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}