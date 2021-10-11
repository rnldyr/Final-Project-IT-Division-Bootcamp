package com.example.kepo.model;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.example.kepo.BR;
import com.google.gson.annotations.SerializedName;

public class GetTodoDetailResponseData extends BaseObservable {

    private String username;
    @SerializedName("todo_id")
    private String todoID;
    private String title;
    private String description;
    @SerializedName("last_edited")
    private String lastEdited;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Bindable
    public String getTodoID() {
        return todoID;
    }

    public void setTodoID(String todoID) {
        this.todoID = todoID;
        notifyPropertyChanged(BR.todoID);
    }

    @Bindable
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
        notifyPropertyChanged(BR.title);
    }

    @Bindable
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
        notifyPropertyChanged(BR.description);
    }

    @Bindable
    public String getLastEdited() {
        return lastEdited;
    }

    public void setLastEdited(String lastEdited) {
        this.lastEdited = lastEdited;
        notifyPropertyChanged(BR.lastEdited);
    }
}
