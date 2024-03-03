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
public class ThumbInfoModel implements Serializable {


    @SerializedName("b_Number")
    public int b_Number;


    @SerializedName("b_Thumb")
    public String b_Thumb;

    @SerializedName("b_ThumbType")
    public String b_ThumbType;

    @Override
    public String toString() {
        return "ThumbInfoModel{" +
                "b_Number=" + b_Number +
                ", b_Thumb='" + b_Thumb + '\'' +
                ", b_ThumbType='" + b_ThumbType + '\'' +
                '}';
    }
}
