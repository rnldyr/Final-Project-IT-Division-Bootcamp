package com.example.kepo.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.kepo.R;
import com.example.kepo.databinding.ActivityProfileBinding;
import com.example.kepo.model.LoginResponseData;
import com.example.kepo.model.SharedPref;

public class ProfileActivity extends AppCompatActivity {

    private ActivityProfileBinding binding;
    private SharedPref pref;
    private LoginResponseData currUser;
    private AlertDialog.Builder builder;
    private AlertDialog  alert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_profile);

        builder = new AlertDialog.Builder(this);

        builder.setTitle("Logout");
        builder.setMessage("Are you sure want to logout?");

        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                pref.delete();
                Intent intent = new Intent(ProfileActivity.this, SplashScreenActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });

        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        alert = builder.create();

        pref = new SharedPref(this);
        currUser = pref.load();

        binding.setUser(currUser);

        binding.btnBackProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        binding.btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.show();
            }
        });

    }
}