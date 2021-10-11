package com.example.kepo.activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.res.ColorStateList;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Toast;

import com.example.kepo.R;
import com.example.kepo.databinding.ActivityCreateTodoBinding;
import com.example.kepo.model.CreateTodoData;
import com.example.kepo.model.CreateTodoResponse;
import com.example.kepo.model.SharedPref;
import com.example.kepo.model.Todo;
import com.example.kepo.network.APIService;
import com.example.kepo.network.ApiUtils;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateTodoActivity extends AppCompatActivity {

    private ActivityCreateTodoBinding binding;
    private TextWatcher mTextEditorWatcher;
    private APIService mAPIService;
    private SharedPref pref;
    private String createOrUpdate;
    private String todoID;
    private String todoTitle;
    private String todoDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_create_todo);
        binding.setNewTodo(new CreateTodoData());

        createOrUpdate = getIntent().getStringExtra("EXTRA_CREATE_OR_UPDATE");
        binding.progressBar.bringToFront();

        if(createOrUpdate.equals("Create")){
            binding.tvTitle.setText("Create Todo");
        }else if(createOrUpdate.equals("Update")){
            binding.tvTitle.setText("Update Todo");
            todoID = getIntent().getStringExtra("EXTRA_TODO_ID");
            todoTitle = getIntent().getStringExtra("EXTRA_TODO_TITLE");
            todoDescription = getIntent().getStringExtra("EXTRA_TODO_DESCRIPTION");

            CreateTodoData temp = new CreateTodoData();
            temp.setTitle(todoTitle);
            temp.setDescription(todoDescription);
            binding.setNewTodo(temp);
        }

        pref = new SharedPref(this);

        binding.btnBackCreateTodo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(createOrUpdate.equals("Create")){
                    createTodo();
                }else if(createOrUpdate.equals("Update")){
                    updateTodo();
                }
            }
        });

        ColorStateList oldColor =  binding.textCount.getTextColors();

        mTextEditorWatcher = new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @RequiresApi(api = Build.VERSION_CODES.M)
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() > 0){
                    binding.errorMsg.setVisibility(View.GONE);
                }
                if(s.length() > 100) {
                    binding.textCount.setTextColor(getColor(R.color.red));
                    binding.errorMsg.setText("Your description exceeded the maximum words");
                    binding.errorMsg.setVisibility(View.VISIBLE);
                    binding.fab.setClickable(false);
                } else {
                    binding.textCount.setTextColor(oldColor);
                    binding.errorMsg.setVisibility(View.GONE);
                    binding.fab.setClickable(true);
                }
                binding.textCount.setText(String.valueOf(s.length()) + "/100");
            }

            public void afterTextChanged(Editable s) {
            }
        };

        binding.etTodoDescription.addTextChangedListener(mTextEditorWatcher);
        binding.textCount.setText(String.valueOf(binding.etTodoDescription.length()) + "/100");
    }

    private void createTodo(){

        CreateTodoData temp = binding.getNewTodo();
        mAPIService = ApiUtils.getAPIService();
        if(temp.getTitle() == null || temp.getDescription() == null){
            binding.errorMsg.setText("Text field cannot be empty");
            binding.errorMsg.setVisibility(View.VISIBLE);
            return;
        }
        binding.errorMsg.setVisibility(View.GONE);

        binding.progressBar.setVisibility(View.VISIBLE);
        try {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("title", temp.getTitle());
            jsonObject.addProperty("description", temp.getDescription());
            Call<CreateTodoResponse> call = mAPIService.createTodo(jsonObject, pref.load().getUserID());
            call.enqueue(new Callback<CreateTodoResponse>() {
                @Override
                public void onResponse(Call<CreateTodoResponse> call, Response<CreateTodoResponse> response) {
                    if(response.isSuccessful()){
                        binding.progressBar.setVisibility(View.GONE);
                        CreateTodoResponse resp = response.body();
                        Toast.makeText(CreateTodoActivity.this, resp.getMessage(), Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }

                @Override
                public void onFailure(Call<CreateTodoResponse> call, Throwable t) {
                    binding.progressBar.setVisibility(View.GONE);
                }
            });

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void updateTodo(){
        CreateTodoData temp = binding.getNewTodo();
        mAPIService = ApiUtils.getAPIService();
        if(temp.getTitle() == null || temp.getDescription() == null){
            binding.errorMsg.setText("Text field cannot be empty");
            binding.errorMsg.setVisibility(View.VISIBLE);
            return;
        }
        binding.errorMsg.setVisibility(View.GONE);

        binding.progressBar.setVisibility(View.VISIBLE);
        try {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("title", temp.getTitle());
            jsonObject.addProperty("description", temp.getDescription());
            Call<CreateTodoResponse> call = mAPIService.updateTodo(jsonObject, pref.load().getUserID(), todoID);
            call.enqueue(new Callback<CreateTodoResponse>() {
                @Override
                public void onResponse(Call<CreateTodoResponse> call, Response<CreateTodoResponse> response) {
                    if(response.isSuccessful()){
                        binding.progressBar.setVisibility(View.GONE);
                        CreateTodoResponse resp = response.body();
                        Toast.makeText(CreateTodoActivity.this, resp.getMessage(), Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }

                @Override
                public void onFailure(Call<CreateTodoResponse> call, Throwable t) {
                    binding.progressBar.setVisibility(View.GONE);
                }
            });

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}