package com.jc.jcsports.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class LoginModel implements Serializable {


    @SerializedName("s_Number")
    public int s_Number;
    @SerializedName("s_NickName")
    public String s_NickName;
    @SerializedName("s_PhotoList")
    public List<FileModel> s_PhotoList;
    @SerializedName("s_Social")
    public String social;
    @SerializedName("s_SocialToken")
    public String s_SocialToken;
    @SerializedName("s_Time")
    public String s_Time;
    @SerializedName("s_IsLogin")
    public boolean s_IsLogin;
    @SerializedName("s_FirebaseToken")
    public String s_FirebaseToken;
    @SerializedName("s_PhoneNumber")
    public String s_PhoneNumber;
    @SerializedName("s_Birth")
    public String s_Birth;
    @SerializedName("s_Gender")
    public String s_Gender;
    @SerializedName("s_AgeRange")
    public String s_AgeRange;

    @Override
    public String toString() {
        return "LoginModel{" +
                "s_Number=" + s_Number +
                ", s_NickName='" + s_NickName + '\'' +
                ", s_PhotoList=" + s_PhotoList +
                ", social='" + social + '\'' +
                ", s_SocialToken='" + s_SocialToken + '\'' +
                ", s_Time='" + s_Time + '\'' +
                ", s_IsLogin=" + s_IsLogin +
                ", s_FirebaseToken='" + s_FirebaseToken + '\'' +
                ", s_PhoneNumber='" + s_PhoneNumber + '\'' +
                ", s_Birth='" + s_Birth + '\'' +
                ", s_Gender='" + s_Gender + '\'' +
                ", s_AgeRange='" + s_AgeRange + '\'' +
                '}';
    }
}
