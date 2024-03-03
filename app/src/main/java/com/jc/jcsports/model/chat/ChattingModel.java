package com.jc.jcsports.model.chat;

import com.google.gson.annotations.SerializedName;
import com.jc.jcsports.model.LoginModel;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ChattingModel implements Serializable {

    public enum MessageType {
        MESSAGE, IMAGE, IMOTICON, VIDEO, IN, OUT, EXIT, ENTER
    }


    @SerializedName("c_Number")
    public int c_Number;

    @SerializedName("s_Number")
    public int s_Number;

    @SerializedName("s_LoginModel")
    public LoginModel s_LoginModel;

    @SerializedName("c_Message")
    public String c_Message;

    @SerializedName("c_ImageUrl")
    public String c_FileUrl;

    @SerializedName("c_VideoUrl")
    public String c_VideoUrl;

    @SerializedName("c_ImoticonUrl")
    public String c_ImoticonUrl;


    @SerializedName("c_MessageTime")
    public String c_MessageTime;

    @SerializedName("c_UnReadCount")
    public int c_UnReadCount;

    @SerializedName("c_MessageType")
    public MessageType c_MessageType;


    @Override
    public String toString() {
        return "ChattingModel{" +
                "c_Number=" + c_Number +
                ", s_Number=" + s_Number +
                ", s_LoginModel=" + s_LoginModel +
                ", c_Message='" + c_Message + '\'' +
                ", c_FileUrl='" + c_FileUrl + '\'' +
                ", c_VideoUrl='" + c_VideoUrl + '\'' +
                ", c_ImoticonUrl='" + c_ImoticonUrl + '\'' +
                ", c_MessageTime='" + c_MessageTime + '\'' +
                ", c_UnReadCount=" + c_UnReadCount +
                ", c_MessageType=" + c_MessageType +
                '}';
    }
}
