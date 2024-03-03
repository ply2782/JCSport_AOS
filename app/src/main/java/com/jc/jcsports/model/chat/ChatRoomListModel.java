package com.jc.jcsports.model.chat;

import com.google.gson.annotations.SerializedName;
import com.jc.jcsports.viewModel.ChatListViewModel;

import java.io.Serializable;

public class ChatRoomListModel implements Serializable {

    @SerializedName("c_Number")
    public int c_Number;

    @SerializedName("s_Number")
    public int s_Number;

    @SerializedName("c_IsDelete")
    public boolean c_IsDelete;

    @SerializedName("c_CreateDate")
    public String c_CreateDate;

    @SerializedName("c_ListType")
    public String c_ListType;


    @Override
    public String toString() {
        return "ChatRoomListModel{" +
                "c_Number=" + c_Number +
                ", s_Number=" + s_Number +
                ", c_IsDelete=" + c_IsDelete +
                ", c_CreateDate='" + c_CreateDate + '\'' +
                ", c_ListType='" + c_ListType + '\'' +
                '}';
    }
}
