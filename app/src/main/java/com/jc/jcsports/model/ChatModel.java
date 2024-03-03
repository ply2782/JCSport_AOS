package com.jc.jcsports.model;


import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class ChatModel implements Serializable {

    public enum MessageType {
        IN, OUT, FILE, Message, TODAY
    }

    public enum Status {
        IN, OUT
    }

    @SerializedName("s_Number")
    public int s_Number;

    @SerializedName("b_Number")
    public int b_Number;

    @SerializedName("s_LoginModel")
    public LoginModel s_LoginModel;

    @SerializedName("c_Message")
    public String c_Message;

    @SerializedName("c_Message")
    public MessageType messageType;

    @SerializedName("c_Time")
    public String c_Time;

    @SerializedName("c_FileUrl")
    public String c_FileUrl;

    @SerializedName("c_Status")
    public Status c_Status;

    @SerializedName("c_R_Count")
    public int c_R_Count;


    @Override
    public String toString() {
        return "ChatModel{" +
                "s_Number=" + s_Number +
                ", b_Number=" + b_Number +
                ", s_LoginModel=" + s_LoginModel +
                ", c_Message='" + c_Message + '\'' +
                ", messageType=" + messageType +
                ", c_Time='" + c_Time + '\'' +
                ", c_FileUrl='" + c_FileUrl + '\'' +
                ", c_Status=" + c_Status +
                ", c_R_Count=" + c_R_Count +
                '}';
    }
}
