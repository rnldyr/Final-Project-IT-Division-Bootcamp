package com.example.kepo.model;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.example.kepo.BR;
import com.google.gson.annotations.SerializedName;

public class LoginResponseData extends BaseObservable {

    @SerializedName("user_id")
    private String userID;
    private String name;
    private String username;

    @Bindable
    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
        notifyPropertyChanged(BR.userID);
    }

    @Bindable
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        notifyPropertyChanged(BR.name);
    }

    @Bindable
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
        notifyPropertyChanged(BR.username);
    }
}
