package com.jc.jcsports.model;


import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.sql.Timestamp;
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
public class BulletinModel implements Serializable {


    @SerializedName("s_Number")
    public int s_Number;

    @SerializedName("b_Content")
    public String b_Content;

    @SerializedName("b_Number")
    public int b_Number;

    @SerializedName("b_CreateDate")
    public String b_CreateDate;

    @SerializedName("b_isDelete")
    public boolean b_isDelete;

    @SerializedName("t_Model")
    public ThumbInfoModel t_Model;


    @SerializedName("b_Like_Count")
    public int b_Like_Count;

    @SerializedName("b_Reply_Count")
    public int b_Reply_Count;

    @SerializedName("b_Look_Count")
    public int b_Look_Count;


    @SerializedName("b_UpdateDate")
    public String b_UpdateDate;


    @Override
    public String toString() {
        return "BulletinModel{" +
                "s_Number=" + s_Number +
                ", b_Content='" + b_Content + '\'' +
                ", b_Number=" + b_Number +
                ", b_CreateDate='" + b_CreateDate + '\'' +
                ", b_isDelete=" + b_isDelete +
                ", t_Model=" + t_Model +
                '}';
    }
}
