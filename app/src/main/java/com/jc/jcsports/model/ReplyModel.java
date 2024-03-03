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
public class ReplyModel implements Serializable {


    @SerializedName("b_Number")
    public int b_Number;

    @SerializedName("r_Content")
    public String r_Content;

    @SerializedName("r_CreateDate")
    public String r_CreateDate;

    @SerializedName("s_LoginModel")
    public LoginModel s_LoginModel;

    @Override
    public String toString() {
        return "ReplyModel{" +
                "b_Number=" + b_Number +
                ", r_Content='" + r_Content + '\'' +
                ", r_CreateDate='" + r_CreateDate + '\'' +
                ", s_LoginModel=" + s_LoginModel +
                '}';
    }
}
