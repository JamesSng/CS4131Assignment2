package com.example.assignment2.ui.person;

import androidx.lifecycle.ViewModelProvider;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.assignment2.AES;
import com.example.assignment2.MainActivity;
import com.example.assignment2.R;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;


public class PersonQrFragment extends Fragment {

    private PersonQrViewModel model;
    private ImageView qrImage;

    public static PersonQrFragment newInstance() {
        return new PersonQrFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.person_qr_fragment, container, false);
        qrImage = root.findViewById(R.id.qrImage);

        root.findViewById(R.id.toInfoButton).setOnClickListener(view -> {
            NavHostFragment navHostFragment = (NavHostFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.mainNavHostFragment);
            NavController navController = navHostFragment.getNavController();

            navController.navigate(R.id.action_personQrFragment_to_personInfoFragment);
        });

        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        model = new ViewModelProvider(requireActivity()).get(PersonQrViewModel.class);
        new Handler().postDelayed(() -> model.setIcNumber(MainActivity.db.getCurrentUser().getIcNumber()), 300);
        model.getIcNumber().observe(getViewLifecycleOwner(), icNumber -> {
            try {
                if(icNumber != null) generateQRCodeImage(AES.encrypt(icNumber, AES.secret), 400, 400, qrImage);
            } catch (WriterException e) {
                e.printStackTrace();
            }
        });
    }

    private void generateQRCodeImage(String text, int width, int height, ImageView qrCode)
            throws WriterException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height);
        Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        for (int x = 0; x < width; x++){
            for (int y = 0; y < height; y++){
                bmp.setPixel(x, y, bitMatrix.get(x,y) ? Color.BLACK : Color.WHITE);
            }
        }
        qrCode.setImageBitmap(bmp);
    }


}