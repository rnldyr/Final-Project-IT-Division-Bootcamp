package com.example.kepo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kepo.databinding.SearchTodoListLayoutBinding;
import com.example.kepo.callback.SearchTodoDiffCallback;
import com.example.kepo.model.SearchTodoResponseData;

import java.util.ArrayList;

public class SearchTodoAdapter extends RecyclerView.Adapter<SearchTodoAdapter.searchTodoViewHolder>{

    private ArrayList<SearchTodoResponseData> mTodoList;
    private Context context;
    private SearchTodoAdapter.OnClickListener mOnClickListener;

    public SearchTodoAdapter(Context context, SearchTodoAdapter.OnClickListener onClickListener){
        this.mTodoList = new ArrayList<>();
        this.context = context;
        this.mOnClickListener = onClickListener;
    }

    @NonNull
    @Override
    public SearchTodoAdapter.searchTodoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        SearchTodoListLayoutBinding todoLayoutBinding = SearchTodoListLayoutBinding.inflate(layoutInflater, parent, false);
        return new SearchTodoAdapter.searchTodoViewHolder(todoLayoutBinding, mOnClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchTodoAdapter.searchTodoViewHolder holder, int position) {
        SearchTodoResponseData todo = mTodoList.get(position);
        holder.toDoBinding.setTodo(todo);
    }

    @Override
    public int getItemCount() {
        return mTodoList.size();
    }

    class searchTodoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private SearchTodoListLayoutBinding toDoBinding;
        private SearchTodoAdapter.OnClickListener onClickListener;

        public searchTodoViewHolder(@NonNull SearchTodoListLayoutBinding toDoBinding, SearchTodoAdapter.OnClickListener onClickListener) {
            super(toDoBinding.getRoot());
            this.toDoBinding = toDoBinding;
            this.onClickListener = onClickListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onClickListener.onClickListener(getAdapterPosition());
        }
    }

    public void updateToDoListItems(ArrayList<SearchTodoResponseData> todoList){
        final SearchTodoDiffCallback diffCallback = new SearchTodoDiffCallback(this.mTodoList, todoList);
        final DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffCallback);

        this.mTodoList.clear();
        this.mTodoList.addAll(todoList);
        diffResult.dispatchUpdatesTo(this);
    }

    public interface OnClickListener{
        void onClickListener(int position);
    }
}
