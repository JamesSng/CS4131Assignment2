package com.example.assignment2.ui.person;

import android.graphics.Bitmap;
import android.net.Uri;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.birjuvachhani.avatarview.AvatarView;
import com.example.assignment2.model.Person;

import java.util.Objects;

public class PersonInfoViewModel extends ViewModel {
    private MutableLiveData<Bitmap> bitmap = new MutableLiveData<>();
    private MutableLiveData<Person> person = new MutableLiveData<>();

    public LiveData<Bitmap> getBitmap(){ return bitmap; }

    public void setBitmap(Bitmap bitmap){ this.bitmap.setValue(bitmap); }

    public void setPerson(Person person){ this.person.setValue(person); }

    public LiveData<Person> getPerson(){ return person; }

}