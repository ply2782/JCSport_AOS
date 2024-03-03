package com.jc.jcsports.model.chat;


import com.google.gson.annotations.SerializedName;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@ToString
@NoArgsConstructor
public class ChatMessageModel {



    @SerializedName("c_Number")
    public int c_Number;

    @SerializedName("s_Number")
    public int s_Number;

    @SerializedName("c_Message")
    public String c_Message;

    @SerializedName("c_UnReadCount")
    public int c_UnReadCount;

    @SerializedName("c_MessageTime")
    public String c_MessageTime;

    @SerializedName("c_MessageType")
    public String c_MessageType;

    @Override
    public String toString() {
        return "ChatMessageModel{" +
                "c_Number=" + c_Number +
                ", s_Number=" + s_Number +
                ", c_Message='" + c_Message + '\'' +
                ", c_UnReadCount=" + c_UnReadCount +
                ", c_MessageTime='" + c_MessageTime + '\'' +
                ", c_MessageType='" + c_MessageType + '\'' +
                '}';
    }
}
