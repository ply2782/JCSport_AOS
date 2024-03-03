package com.jc.jcsports.model.chat;

import com.google.gson.annotations.SerializedName;
import com.jc.jcsports.model.LoginModel;

import java.io.Serializable;

public class ChatJoinModel implements Serializable {

    @SerializedName("c_Number")
    public int c_Number;

    @SerializedName("s_Number")
    public int s_Number;

    @SerializedName("s_LoginModel")
    public LoginModel s_LoginModel;

    @SerializedName("enterTime")
    public String enterTime;

    @SerializedName("exitTime")
    public String exitTime;

    @Override
    public String toString() {
        return "ChatJoinModel{" +
                "c_Number=" + c_Number +
                ", s_Number=" + s_Number +
                ", s_LoginModel=" + s_LoginModel +
                ", enterTime='" + enterTime + '\'' +
                ", exitTime='" + exitTime + '\'' +
                '}';
    }
}
