package com.example.kepo.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.kepo.R;
import com.example.kepo.adapter.UserAdapter;
import com.example.kepo.databinding.ActivitySearchUserBinding;
import com.example.kepo.model.SearchUserResponse;
import com.example.kepo.model.SharedPref;
import com.example.kepo.model.User;
import com.example.kepo.network.APIService;
import com.example.kepo.network.ApiUtils;
import com.google.gson.JsonObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchUserActivity extends AppCompatActivity implements UserAdapter.OnClickListener{

    private ActivitySearchUserBinding binding;
    private UserAdapter userAdapter;
    private SharedPref pref;
    private APIService mAPIService;
    private ArrayList<User> listUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_search_user);
        binding.tvResultMsg.setVisibility(View.INVISIBLE);
        binding.setSearchQuery("");

        listUser = new ArrayList<>();

        binding.btnBackSearchUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        binding.btnSearchUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(binding.getSearchQuery().isEmpty()){
                    Toast.makeText(SearchUserActivity.this, "Text field cannot be empty", Toast.LENGTH_SHORT).show();
                    binding.tvResultMsg.setVisibility(View.INVISIBLE);
                    return;
                }
                initAdapter();
                searchUser();
            }
        });

        pref = new SharedPref(this);

    }

    private void initAdapter(){
        userAdapter = new UserAdapter(this, this);
        binding.rvSearchUser.setLayoutManager(new LinearLayoutManager(this));
        binding.rvSearchUser.setAdapter(userAdapter);
    }

    private void searchUser(){
        binding.progressBar.bringToFront();
        binding.progressBar.setVisibility(View.VISIBLE);
        mAPIService = ApiUtils.getAPIService();
        try{
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("user_id", pref.load().getUserID());
            jsonObject.addProperty("searchQuery", binding.getSearchQuery());
            Call<SearchUserResponse> call = mAPIService.searchUser(jsonObject);
            call.enqueue(new Callback<SearchUserResponse>() {
                @Override
                public void onResponse(Call<SearchUserResponse> call, Response<SearchUserResponse> response) {
                    if(response.isSuccessful()){
                        SearchUserResponse resp = response.body();
                        ArrayList<User> users = resp.getData();
                        binding.progressBar.setVisibility(View.GONE);
                        binding.tvResultMsg.setText("Results for \"" + binding.getSearchQuery() + "\"");
                        binding.tvResultMsg.setVisibility(View.VISIBLE);
                        if(users.size() == 0){
                            binding.tvNoDataMsg.setVisibility(View.VISIBLE);
                        }else{
                            binding.tvNoDataMsg.setVisibility(View.GONE);
                        }

                        listUser = users;
                        userAdapter.updateUserListItems(users);
                        binding.setSearchQuery("");
                    }
                }

                @Override
                public void onFailure(Call<SearchUserResponse> call, Throwable t) {

                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onClickListener(int position) {
        Intent intent = new Intent(SearchUserActivity.this, DetailUserActivity.class);
        intent.putExtra("EXTRA_USER_ID", listUser.get(position).getUserID());
        intent.putExtra("EXTRA_USERNAME", listUser.get(position).getUsername());
        intent.putExtra("EXTRA_NAME", listUser.get(position).getName());
        startActivity(intent);
    }
}