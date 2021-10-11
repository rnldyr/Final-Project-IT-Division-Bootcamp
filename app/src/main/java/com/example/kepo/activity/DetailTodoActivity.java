package com.example.kepo.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.kepo.R;
import com.example.kepo.databinding.ActivityDetailTodoBinding;
import com.example.kepo.model.GetTodoDetailResponse;
import com.example.kepo.model.GetTodoDetailResponseData;
import com.example.kepo.network.APIService;
import com.example.kepo.network.ApiUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailTodoActivity extends AppCompatActivity {

    private ActivityDetailTodoBinding binding;
    private String todoID;
    private String userID;
    private String whoseTodo;
    private APIService mAPIService;

    @Override
    protected void onResume() {
        super.onResume();
        loadTodoDetail();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_detail_todo);

        binding.progressBar.bringToFront();

        todoID = getIntent().getStringExtra("EXTRA_TODO_ID");
        userID = getIntent().getStringExtra("EXTRA_USER_ID");
        whoseTodo = getIntent().getStringExtra("EXTRA_WHOSE_TODO");

        binding.setTodo(new GetTodoDetailResponseData());

        if(whoseTodo.equals("Mine")){
            binding.fab.setVisibility(View.VISIBLE);
        }else if(whoseTodo.equals("Others")){
            binding.fab.setVisibility(View.GONE);
        }

        binding.btnBackDetailTodo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailTodoActivity.this, CreateTodoActivity.class);
                intent.putExtra("EXTRA_CREATE_OR_UPDATE", "Update");
                intent.putExtra("EXTRA_TODO_ID", binding.getTodo().getTodoID());
                intent.putExtra("EXTRA_TODO_TITLE", binding.getTodo().getTitle());
                intent.putExtra("EXTRA_TODO_DESCRIPTION", binding.getTodo().getDescription());
                startActivity(intent);
            }
        });

        mAPIService = ApiUtils.getAPIService();
        loadTodoDetail();
    }

    private void loadTodoDetail(){
        binding.progressBar.setVisibility(View.VISIBLE);
        try {
            Call<GetTodoDetailResponse> call = mAPIService.getTodoDetail(userID, todoID);
            call.enqueue(new Callback<GetTodoDetailResponse>() {
                @Override
                public void onResponse(Call<GetTodoDetailResponse> call, Response<GetTodoDetailResponse> response) {
                    if(response.isSuccessful()){
                        GetTodoDetailResponse resp = response.body();
                        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                        Date date = null;//You will get date object relative to server/client timezone wherever it is parsed
                        try {
                            date = dateFormat.parse(resp.getData().getLastEdited());
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm"); //If you need time just put specific format for time like 'HH:mm:ss'
                        resp.getData().setLastEdited(formatter.format(date));
                        binding.progressBar.setVisibility(View.GONE);
                        binding.setTodo(resp.getData());
                    }
                }

                @Override
                public void onFailure(Call<GetTodoDetailResponse> call, Throwable t) {
                    binding.progressBar.setVisibility(View.GONE);
                }
            });

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}