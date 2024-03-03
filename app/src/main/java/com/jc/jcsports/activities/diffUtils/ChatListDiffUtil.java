package com.jc.jcsports.activities.diffUtils;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import com.jc.jcsports.model.chat.ChatListModel;

public class ChatListDiffUtil  extends DiffUtil.ItemCallback<ChatListModel> {

    @Override
    public boolean areItemsTheSame(@NonNull ChatListModel oldItem, @NonNull ChatListModel newItem) {
        return oldItem.chatRoomListModel.c_Number == newItem.chatRoomListModel.c_Number;
    }

    @Override
    public boolean areContentsTheSame(@NonNull ChatListModel oldItem, @NonNull ChatListModel newItem) {
        return oldItem.toString().equals(newItem.toString());
    }
}

