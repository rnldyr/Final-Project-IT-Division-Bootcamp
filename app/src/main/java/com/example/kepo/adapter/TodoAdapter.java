package com.example.kepo.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kepo.R;
import com.example.kepo.activity.MyTodoActivity;
import com.example.kepo.activity.ProfileActivity;
import com.example.kepo.activity.SplashScreenActivity;
import com.example.kepo.databinding.UserTodoLayoutBinding;
import com.example.kepo.model.CreateTodoResponse;
import com.example.kepo.model.GetUserTodoResponse;
import com.example.kepo.model.SharedPref;
import com.example.kepo.model.Todo;
import com.example.kepo.callback.TodoDiffCallback;
import com.example.kepo.network.APIService;
import com.example.kepo.network.ApiUtils;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TodoAdapter extends RecyclerView.Adapter<TodoAdapter.todoViewHolder>{

    private ArrayList<Todo> mTodoList;
    private Context context;
    private OnClickListener mOnClickListener;
    private ArrayList<Todo> checkedTodo = new ArrayList<>();
    private Snackbar snackbar;
    private APIService mAPIService;
    private SharedPref pref;
    private AlertDialog.Builder builder;
    private AlertDialog alert;

    public TodoAdapter(Context context, OnClickListener onClickListener){
        this.mTodoList = new ArrayList<>();
        this.context = context;
        this.mOnClickListener = onClickListener;
    }

    @NonNull
    @Override
    public todoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        UserTodoLayoutBinding todoLayoutBinding = UserTodoLayoutBinding.inflate(layoutInflater, parent, false);
        return new todoViewHolder(todoLayoutBinding, mOnClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull todoViewHolder holder, int position) {
        Todo todo = mTodoList.get(position);
        holder.toDoBinding.setTodo(todo);
        holder.toDoBinding.checkbox.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {
                if(holder.toDoBinding.checkbox.isChecked()){
                    checkedTodo.add(todo);
                } else if(!holder.toDoBinding.checkbox.isChecked()){
                    checkedTodo.remove(todo);
                }

                builder = new AlertDialog.Builder(v.getContext());

                builder.setTitle("Logout");
                builder.setMessage("Are you sure you want to delete all these todos?");

                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        mAPIService = ApiUtils.getAPIService();
                        try{
                            JsonObject jsonObject = new JsonObject();
                            JsonArray jsonArray = new JsonArray();
                            for (Todo t: checkedTodo) {
                                jsonArray.add(t.getTodoID());
                            }
                            jsonObject.add("todos", jsonArray);
                            Call<CreateTodoResponse> call = mAPIService.deleteTodo(jsonObject, pref.load().getUserID());
                            call.enqueue(new Callback<CreateTodoResponse>() {
                                @Override
                                public void onResponse(Call<CreateTodoResponse> call, Response<CreateTodoResponse> response) {
                                    CreateTodoResponse resp = response.body();
                                    Toast.makeText(v.getContext(), resp.getMessage(), Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onFailure(Call<CreateTodoResponse> call, Throwable t) {

                                }
                            });
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        clearCheckedItemList();
                    }
                });

                builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        clearCheckedItemList();
                    }
                });

                alert = builder.create();


                snackbar = Snackbar
                        .make(v, checkedTodo.size()+" Item(s)", BaseTransientBottomBar.LENGTH_INDEFINITE)
                        .setActionTextColor(Color.parseColor("#B23B3B"))
                        .setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE)
                        .setAction("DELETE", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                alert.show();
                            }
                        });

                if(checkedTodo.size() == 0){
                    snackbar.dismiss();
                }else{
                    snackbar.show();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mTodoList.size();
    }

    class todoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private UserTodoLayoutBinding toDoBinding;
        private OnClickListener onClickListener;

        public todoViewHolder(@NonNull UserTodoLayoutBinding toDoBinding, OnClickListener onClickListener) {
            super(toDoBinding.getRoot());
            this.toDoBinding = toDoBinding;
            this.onClickListener = onClickListener;

            this.toDoBinding.checkbox.setOnClickListener(this);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onClickListener.onClickListener(getAdapterPosition());
        }
    }

    public void updateToDoListItems(ArrayList<Todo> todoList){
        final TodoDiffCallback diffCallback = new TodoDiffCallback(this.mTodoList, todoList);
        final DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffCallback);

        this.mTodoList.clear();
        this.mTodoList.addAll(todoList);
        diffResult.dispatchUpdatesTo(this);
    }

    public interface OnClickListener{
        void onClickListener(int position);
    }

    public ArrayList<Todo> getCheckedItemList(){
        return checkedTodo;
    }

    public void clearCheckedItemList(){
        checkedTodo.clear();
    }
}
