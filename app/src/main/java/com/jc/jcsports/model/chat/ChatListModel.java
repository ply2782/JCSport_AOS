package com.jc.jcsports.model.chat;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class ChatListModel implements Serializable {


    @SerializedName("c_ChatRoomListModel")
    public ChatRoomListModel chatRoomListModel;

    @SerializedName("c_MessageModel")
    public ChatMessageModel chatMessageModel;

    @SerializedName("c_JoinModel")
    public List<ChatJoinModel> c_JoinModel;

    @Override
    public String toString() {
        return "ChatListModel{" +
                "chatRoomListModel=" + chatRoomListModel +
                ", chatMessageModel=" + chatMessageModel +
                ", c_JoinModel=" + c_JoinModel +
                '}';
    }
}
