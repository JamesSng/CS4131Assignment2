package com.example.assignment2.ui.person.account;

import androidx.lifecycle.ViewModelProvider;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.Image;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.assignment2.AES;
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
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        model = new ViewModelProvider(this).get(PersonQrViewModel.class);
        model.setIcNumber(PersonActivity.db.getCurrentUser().getIcNumber());
        model.getIcNumber().observe(getViewLifecycleOwner(), icNumber -> {
            try {
                generateQRCodeImage(AES.encrypt(icNumber, "secret"), 400, 400, qrImage);
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