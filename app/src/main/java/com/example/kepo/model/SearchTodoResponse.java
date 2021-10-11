package com.example.kepo.model;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.library.baseAdapters.BR;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class SearchTodoResponse extends BaseObservable {

    private int status;
    private String message;
    @SerializedName("data")
    private ArrayList<SearchTodoResponseData> listTodo;

    @Bindable
    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
        notifyPropertyChanged(BR.status);
    }

    @Bindable
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
        notifyPropertyChanged(BR.message);
    }

    @Bindable
    public ArrayList<SearchTodoResponseData> getListTodo() {
        return listTodo;
    }

    public void setListTodo(ArrayList<SearchTodoResponseData> listTodo) {
        this.listTodo = listTodo;
        notifyPropertyChanged(BR.listTodo);
    }
}
