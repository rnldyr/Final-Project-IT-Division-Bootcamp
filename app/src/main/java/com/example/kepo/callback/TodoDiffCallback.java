package com.example.kepo.callback;

import androidx.recyclerview.widget.DiffUtil;

import com.example.kepo.model.Todo;

import java.util.List;

public class TodoDiffCallback extends DiffUtil.Callback {

    private final List<Todo> mOldTodoList;
    private final List<Todo> mNewTodoList;

    public TodoDiffCallback(List<Todo> mOldTodoList, List<Todo> mNewTodoList) {
        this.mOldTodoList = mOldTodoList;
        this.mNewTodoList = mNewTodoList;
    }


    @Override
    public int getOldListSize() {
        return mOldTodoList.size();
    }

    @Override
    public int getNewListSize() {
        return mNewTodoList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return mOldTodoList.get(oldItemPosition).getTodoID() == mNewTodoList.get(newItemPosition).getTodoID();
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        final Todo oldToDo = mOldTodoList.get(oldItemPosition);
        final Todo newToDo = mNewTodoList.get(newItemPosition);

        return oldToDo.getTitle().equals(newToDo.getTitle());
    }

}
