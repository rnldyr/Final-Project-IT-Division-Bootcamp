package com.example.kepo.model;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.example.kepo.BR;
import com.google.gson.annotations.SerializedName;

public class Todo extends BaseObservable {

    @SerializedName("todo_id")
    private String todoID;
    private String title;
    @SerializedName("last_edited")
    private String lastEdited;

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
    public String getLastEdited() {
        return lastEdited;
    }

    public void setLastEdited(String lastEdited) {
        this.lastEdited = lastEdited;
        notifyPropertyChanged(BR.lastEdited);
    }
}
