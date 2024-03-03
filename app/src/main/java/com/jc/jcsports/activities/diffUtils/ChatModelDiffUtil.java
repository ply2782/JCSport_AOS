package com.jc.jcsports.activities.diffUtils;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import com.jc.jcsports.model.BulletinModel;
import com.jc.jcsports.model.ChatModel;

public class ChatModelDiffUtil extends DiffUtil.ItemCallback<ChatModel> {


    @Override
    public boolean areItemsTheSame(@NonNull ChatModel oldItem, @NonNull ChatModel newItem) {
        return oldItem.toString().equals(newItem.toString());
    }

    @Override
    public boolean areContentsTheSame(@NonNull ChatModel oldItem, @NonNull ChatModel newItem) {
        return oldItem.toString().equals(newItem.toString());
    }
}
