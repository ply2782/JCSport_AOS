package com.jc.jcsports.activities.diffUtils;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import com.jc.jcsports.model.LoginModel;
import com.jc.jcsports.model.ThumbInfoModel;

public class UserListDiffUtil extends DiffUtil.ItemCallback<LoginModel> {


    @Override
    public boolean areItemsTheSame(@NonNull LoginModel oldItem, @NonNull LoginModel newItem) {
        return oldItem.s_Number == newItem.s_Number;
    }

    @Override
    public boolean areContentsTheSame(@NonNull LoginModel oldItem, @NonNull LoginModel newItem) {
        return oldItem.toString().equals(newItem.toString());
    }

}