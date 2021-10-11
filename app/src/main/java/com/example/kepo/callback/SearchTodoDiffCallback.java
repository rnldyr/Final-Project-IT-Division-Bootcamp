package com.example.kepo.callback;

import androidx.recyclerview.widget.DiffUtil;

import com.example.kepo.model.SearchTodoResponseData;

import java.util.List;

public class SearchTodoDiffCallback extends DiffUtil.Callback {

    private final List<SearchTodoResponseData> mOldTodoList;
    private final List<SearchTodoResponseData> mNewTodoList;

    public SearchTodoDiffCallback(List<SearchTodoResponseData> mOldTodoList, List<SearchTodoResponseData> mNewTodoList) {
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
        final SearchTodoResponseData oldToDo = mOldTodoList.get(oldItemPosition);
        final SearchTodoResponseData newToDo = mNewTodoList.get(newItemPosition);

        return oldToDo.getTitle().equals(newToDo.getTitle());
    }
}
