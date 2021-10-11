package com.example.kepo.activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.kepo.R;
import com.example.kepo.adapter.TodoAdapter;
import com.example.kepo.databinding.ActivityMyTodoBinding;
import com.example.kepo.model.GetUserTodoResponse;
import com.example.kepo.model.LoginResponseData;
import com.example.kepo.model.SharedPref;
import com.example.kepo.model.Todo;
import com.example.kepo.network.APIService;
import com.example.kepo.network.ApiUtils;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyTodoActivity extends AppCompatActivity implements TodoAdapter.OnClickListener {

    private ActivityMyTodoBinding binding;
    private TodoAdapter toDoAdapter;
    private APIService mAPIService;
    private LoginResponseData currUser;
    private SharedPref pref;
    private ArrayList<Todo> listTodo;
    private Snackbar snackbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_my_todo);

        listTodo = new ArrayList<>();

        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyTodoActivity.this, CreateTodoActivity.class);
                intent.putExtra("EXTRA_CREATE_OR_UPDATE", "Create");
                startActivity(intent);
            }
        });

        binding.btnBackMyTodo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        pref = new SharedPref(this);
        currUser = pref.load();

        initAdapter();
        loadUserToDo();

        toDoAdapter.clearCheckedItemList();
    }

    @Override
    protected void onResume(){
        toDoAdapter.clearCheckedItemList();
        super.onResume();
        loadUserToDo();
    }

    private void initAdapter(){
        toDoAdapter = new TodoAdapter(this, this);
        binding.rvMyTodo.setLayoutManager(new LinearLayoutManager(this));
        binding.rvMyTodo.setAdapter(toDoAdapter);
    }

    private void loadUserToDo(){
        binding.progressBar.bringToFront();
        binding.progressBar.setVisibility(View.VISIBLE);
        mAPIService = ApiUtils.getAPIService();
        try{
            Call<GetUserTodoResponse> call = mAPIService.userToDo(currUser.getUserID());
            call.enqueue(new Callback<GetUserTodoResponse>() {
                @RequiresApi(api = Build.VERSION_CODES.M)
                @SuppressLint("ResourceAsColor")
                @Override
                public void onResponse(Call<GetUserTodoResponse> call, Response<GetUserTodoResponse> response) {
                    if(response.isSuccessful()){
                        GetUserTodoResponse resp = response.body();
                        ArrayList<Todo> todos = resp.getData().getListTodo();
                        for (Todo t : todos) {
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
                        listTodo = todos;
                        toDoAdapter.updateToDoListItems(todos);
                    }
                }

                @Override
                public void onFailure(Call<GetUserTodoResponse> call, Throwable t) {

                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onClickListener(int position) {
        Intent intent = new Intent(MyTodoActivity.this, DetailTodoActivity.class);
        intent.putExtra("EXTRA_TODO_ID", listTodo.get(position).getTodoID());
        intent.putExtra("EXTRA_USER_ID", currUser.getUserID());
        intent.putExtra("EXTRA_WHOSE_TODO", "Mine");
        startActivity(intent);
    }

}