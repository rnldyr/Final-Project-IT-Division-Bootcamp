package com.example.kepo.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.kepo.R;
import com.example.kepo.adapter.SearchTodoAdapter;
import com.example.kepo.databinding.ActivityDetailUserBinding;
import com.example.kepo.model.GetUserTodoResponse;
import com.example.kepo.model.LoginResponseData;
import com.example.kepo.model.SearchTodoResponse;
import com.example.kepo.model.SearchTodoResponseData;
import com.example.kepo.model.Todo;
import com.example.kepo.network.APIService;
import com.example.kepo.network.ApiUtils;
import com.google.gson.JsonObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailUserActivity extends AppCompatActivity implements SearchTodoAdapter.OnClickListener {

    private ActivityDetailUserBinding binding;
    private SearchTodoAdapter searchTodoAdapter;
    private APIService mAPIService;
    private ArrayList<SearchTodoResponseData> listTodo;
    private LoginResponseData thisUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_detail_user);
        listTodo = new ArrayList<>();

        thisUser = new LoginResponseData();
        thisUser.setUserID(getIntent().getStringExtra("EXTRA_USER_ID"));
        thisUser.setName(getIntent().getStringExtra("EXTRA_NAME"));
        thisUser.setUsername(getIntent().getStringExtra("EXTRA_USERNAME"));

        binding.setUser(thisUser);

        binding.btnBackDetailUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        initAdapter();
        loadUserToDo();
    }

    private void initAdapter(){
        searchTodoAdapter = new SearchTodoAdapter(this, this);
        binding.rvOtherUserTodo.setLayoutManager(new LinearLayoutManager(this));
        binding.rvOtherUserTodo.setAdapter(searchTodoAdapter);
    }

    private void loadUserToDo(){
        binding.progressBar.bringToFront();
        binding.progressBar.setVisibility(View.VISIBLE);
        mAPIService = ApiUtils.getAPIService();
        try{
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("searchQuery", thisUser.getUsername());
            jsonObject.addProperty("filterUser", 1);
            jsonObject.addProperty("filterTodo", 0);
            Call<SearchTodoResponse> call = mAPIService.searchTodo(jsonObject);
            call.enqueue(new Callback<SearchTodoResponse>() {
                @Override
                public void onResponse(Call<SearchTodoResponse> call, Response<SearchTodoResponse> response) {
                    if(response.isSuccessful()){
                        SearchTodoResponse resp = response.body();
                        ArrayList<SearchTodoResponseData> todos = resp.getListTodo();
                        for (SearchTodoResponseData t : todos) {
                            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                            Date date = null;//You will get date object relative to server/client timezone wherever it is parsed
                            try {
                                date = dateFormat.parse(t.getLastEdited());
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm"); //If you need time just put specific format for time like 'HH:mm:ss'
                            t.setLastEdited(formatter.format(date));
                        }
                        binding.progressBar.setVisibility(View.GONE);
                        if(todos.size() == 0){
                            binding.tvNoDataMsg.setVisibility(View.VISIBLE);
                        }else{
                            binding.tvNoDataMsg.setVisibility(View.GONE);
                        }
                        binding.setTodoCount(todos.size());

                        listTodo = todos;
                        searchTodoAdapter.updateToDoListItems(todos);
                    }
                }

                @Override
                public void onFailure(Call<SearchTodoResponse> call, Throwable t) {

                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onClickListener(int position) {
        Intent intent = new Intent(DetailUserActivity.this, DetailTodoActivity.class);
        intent.putExtra("EXTRA_TODO_ID", listTodo.get(position).getTodoID());
        intent.putExtra("EXTRA_USER_ID", thisUser.getUserID());
        intent.putExtra("EXTRA_WHOSE_TODO", "Others");
        startActivity(intent);
    }
}