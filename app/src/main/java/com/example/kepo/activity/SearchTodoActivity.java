package com.example.kepo.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.kepo.R;
import com.example.kepo.adapter.SearchTodoAdapter;
import com.example.kepo.adapter.UserAdapter;
import com.example.kepo.databinding.ActivitySearchTodoBinding;
import com.example.kepo.model.SearchTodoResponse;
import com.example.kepo.model.SearchTodoResponseData;
import com.example.kepo.model.SearchUserResponse;
import com.example.kepo.model.Todo;
import com.example.kepo.model.User;
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

public class SearchTodoActivity extends AppCompatActivity implements SearchTodoAdapter.OnClickListener{

    private ActivitySearchTodoBinding binding;
    private APIService mAPIService;
    private SearchTodoAdapter searchTodoAdapter;
    private ArrayList<SearchTodoResponseData> listTodo;

    @Override
    protected void onResume() {
        super.onResume();
        binding.checkboxUser.setChecked(false);
        binding.checkboxTodo.setChecked(false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_search_todo);

        binding.tvResultMsg.setVisibility(View.INVISIBLE);
        binding.setSearchQuery("");

        listTodo = new ArrayList<>();

        binding.btnBackSearchTodo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        binding.btnSearchTodo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(binding.getSearchQuery().isEmpty()){
                    Toast.makeText(SearchTodoActivity.this, "Text field cannot be empty", Toast.LENGTH_SHORT).show();
                    binding.tvResultMsg.setVisibility(View.INVISIBLE);
                    return;
                }
                if(!binding.checkboxTodo.isChecked() && !binding.checkboxUser.isChecked()){
                    Toast.makeText(SearchTodoActivity.this, "You must choose either to search by user, todo, or both", Toast.LENGTH_SHORT).show();
                    binding.tvResultMsg.setVisibility(View.INVISIBLE);
                    return;
                }
                initAdapter();
                searchTodo();
            }
        });

    }

    private void initAdapter(){
        searchTodoAdapter = new SearchTodoAdapter(this, this);
        binding.rvSearchTodo.setLayoutManager(new LinearLayoutManager(this));
        binding.rvSearchTodo.setAdapter(searchTodoAdapter);
    }

    private void searchTodo(){
        binding.progressBar.bringToFront();
        binding.progressBar.setVisibility(View.VISIBLE);
        mAPIService = ApiUtils.getAPIService();
        try{
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("searchQuery", binding.getSearchQuery());
            jsonObject.addProperty("filterUser", binding.checkboxUser.isChecked());
            jsonObject.addProperty("filterTodo", binding.checkboxTodo.isChecked());
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
                        binding.tvResultMsg.setText("Results for \"" + binding.getSearchQuery() + "\"");
                        binding.tvResultMsg.setVisibility(View.VISIBLE);
                        if(todos.size() == 0){
                            binding.tvNoDataMsg.setVisibility(View.VISIBLE);
                        }else{
                            binding.tvNoDataMsg.setVisibility(View.GONE);
                        }

                        listTodo = todos;
                        searchTodoAdapter.updateToDoListItems(todos);
                        binding.setSearchQuery("");
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
        Intent intent = new Intent(SearchTodoActivity.this, DetailTodoActivity.class);
        intent.putExtra("EXTRA_USER_ID", listTodo.get(position).getUserID());
        intent.putExtra("EXTRA_TODO_ID", listTodo.get(position).getTodoID());
        intent.putExtra("EXTRA_WHOSE_TODO", "Others");
        startActivity(intent);
    }
}