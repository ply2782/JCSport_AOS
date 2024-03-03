package com.jc.jcsports.model;


import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class FileModel implements Serializable {


    @SerializedName("b_Number")
    public int b_Number;

    @SerializedName("f_Number")
    public int f_Number;


    @SerializedName("f_Type")
    public String f_Type;

    @SerializedName("f_Name")
    public String f_Name;

    @SerializedName("f_Duration")
    public long f_Duration;

    @SerializedName("f_ContentLength")
    public long f_ContentLength;

    @SerializedName("f_isDelete")
    public boolean f_isDelete;

    @SerializedName("f_CreateDate")
    public String f_CreateDate;


    @Override
    public String toString() {
        return "FileModel{" +
                "b_Number=" + b_Number +
                ", f_Number=" + f_Number +
                ", f_Type=" + f_Type +
                ", f_Name='" + f_Name + '\'' +
                ", f_Duration=" + f_Duration +
                ", f_ContentLength=" + f_ContentLength +
                ", f_isDelete=" + f_isDelete +
                ", f_CreateDate=" + f_CreateDate +
                '}';
    }

}
