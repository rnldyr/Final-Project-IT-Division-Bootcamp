package com.example.kepo.callback;

import androidx.recyclerview.widget.DiffUtil;

import com.example.kepo.model.User;

import java.util.List;

public class UserDiffCallback extends DiffUtil.Callback {

    private final List<User> mOldUserList;
    private final List<User> mNewUserList;

    public UserDiffCallback(List<User> mOldUserList, List<User> mNewUserList) {
        this.mOldUserList = mOldUserList;
        this.mNewUserList = mNewUserList;
    }


    @Override
    public int getOldListSize() {
        return mOldUserList.size();
    }

    @Override
    public int getNewListSize() {
        return mNewUserList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return mOldUserList.get(oldItemPosition).getUserID() == mNewUserList.get(newItemPosition).getUserID();
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        final User oldUser = mOldUserList.get(oldItemPosition);
        final User newUser = mNewUserList.get(newItemPosition);

        return oldUser.getUsername().equals(newUser.getUsername());
    }
}
