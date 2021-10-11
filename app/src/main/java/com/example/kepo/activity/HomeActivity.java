package com.example.kepo.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.kepo.R;
import com.example.kepo.databinding.ActivityHomeBinding;
import com.example.kepo.model.LoginResponseData;
import com.example.kepo.model.SharedPref;

public class HomeActivity extends AppCompatActivity {

    private SharedPref pref;
    private ActivityHomeBinding binding;
    private final String BASE_WELCOME_TEXT = "Welcome, ";
    private LoginResponseData currUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        pref = new SharedPref(this);
        currUser = pref.load();

        binding = DataBindingUtil.setContentView(this, R.layout.activity_home);
        binding.tvWelcome.setText(BASE_WELCOME_TEXT + currUser.getName());

        binding.btnMyTodo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, MyTodoActivity.class);
                startActivity(intent);
            }
        });

        binding.btnSearchTodo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, SearchTodoActivity.class);
                startActivity(intent);
            }
        });

        binding.btnSearchUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, SearchUserActivity.class);
                startActivity(intent);
            }
        });

        binding.btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, ProfileActivity.class);
                startActivity(intent);
            }
        });

    }
}