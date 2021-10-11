package com.example.kepo.model;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.example.kepo.BR;

public class GetUserTodoResponse extends BaseObservable {

    private int status;
    private String message;
    private GetUserTodoResponseData data;


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
    public GetUserTodoResponseData getData() {
        return data;
    }

    public void setData(GetUserTodoResponseData data) {
        this.data = data;
        notifyPropertyChanged(BR.data);
    }
}
