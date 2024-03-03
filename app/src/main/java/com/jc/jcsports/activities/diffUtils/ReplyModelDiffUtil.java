package com.jc.jcsports.activities.diffUtils;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import com.jc.jcsports.model.FileModel;
import com.jc.jcsports.model.ReplyModel;

public class ReplyModelDiffUtil extends DiffUtil.ItemCallback<ReplyModel> {


    @Override
    public boolean areItemsTheSame(@NonNull ReplyModel oldItem, @NonNull ReplyModel newItem) {
        return oldItem.hashCode() == newItem.hashCode();
    }

    @Override
    public boolean areContentsTheSame(@NonNull ReplyModel oldItem, @NonNull ReplyModel newItem) {
        return oldItem.toString().equals(newItem.toString());
    }

}