package com.example.kepo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kepo.databinding.SearchUserLayoutBinding;

import com.example.kepo.model.User;
import com.example.kepo.callback.UserDiffCallback;

import java.util.ArrayList;

public class UserAdapter extends  RecyclerView.Adapter<UserAdapter. userViewHolder>{

    private ArrayList<User> mUserList;
    private Context context;
    private UserAdapter.OnClickListener mOnClickListener;

    public UserAdapter(Context context, UserAdapter.OnClickListener onClickListener){
        this.mUserList = new ArrayList<>();
        this.context = context;
        this.mOnClickListener = onClickListener;
    }

    @NonNull
    @Override
    public UserAdapter.userViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        SearchUserLayoutBinding userLayoutBinding = SearchUserLayoutBinding.inflate(layoutInflater, parent, false);
        return new UserAdapter.userViewHolder(userLayoutBinding, mOnClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull UserAdapter.userViewHolder holder, int position) {
        User user = mUserList.get(position);
        holder.userBinding.setUser(user);
    }

    @Override
    public int getItemCount() {
        return mUserList.size();
    }

    class userViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private SearchUserLayoutBinding userBinding;
        private UserAdapter.OnClickListener onClickListener;

        public userViewHolder(@NonNull SearchUserLayoutBinding userBinding, UserAdapter.OnClickListener onClickListener) {
            super(userBinding.getRoot());
            this.userBinding = userBinding;
            this.onClickListener = onClickListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onClickListener.onClickListener(getAdapterPosition());
        }
    }

    public void updateUserListItems(ArrayList<User> userList){
        final UserDiffCallback diffCallback = new UserDiffCallback(this.mUserList, userList);
        final DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffCallback);

        this.mUserList.clear();
        this.mUserList.addAll(userList);
        diffResult.dispatchUpdatesTo(this);
    }

    public interface OnClickListener{
        void onClickListener(int position);
    }
}
