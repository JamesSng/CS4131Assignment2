package com.example.assignment2.ui.person.account;

import android.graphics.Bitmap;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.birjuvachhani.avatarview.AvatarView;
import com.example.assignment2.model.Person;

public class PersonInfoViewModel extends ViewModel {
    private MutableLiveData<AvatarView> avatarView = new MutableLiveData<>();
    private MutableLiveData<Person> person = new MutableLiveData<>();

    public LiveData<AvatarView> getAvatarView(){ return avatarView; }

    public void setAvatarView(AvatarView avatarView){ this.avatarView.setValue(avatarView);}

    public void setImageBitmap(Bitmap bitmap){ avatarView.getValue().setImageBitmap(bitmap); }

    public void setPerson(Person person){ this.person.setValue(person); }

    public LiveData<Person> getPerson(){ return person; }

}