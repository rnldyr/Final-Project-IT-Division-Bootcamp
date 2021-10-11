package com.example.kepo.model;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPref {

    private SharedPreferences sharedPreferences;

    public SharedPref(Context context) {
        sharedPreferences = context.getSharedPreferences("sharedPref", Context.MODE_PRIVATE);
    }

    public void save(LoginResponseData user){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("userID", user.getUserID());
        editor.putString("name", user.getName());
        editor.putString("username", user.getUsername());
        editor.apply();
    }

    public LoginResponseData load(){
        LoginResponseData user = new LoginResponseData();
        user.setUserID(sharedPreferences.getString("userID", ""));
        user.setName(sharedPreferences.getString("name", ""));
        user.setUsername(sharedPreferences.getString("username", ""));
        return user;
    }

    public void delete(){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }
}
