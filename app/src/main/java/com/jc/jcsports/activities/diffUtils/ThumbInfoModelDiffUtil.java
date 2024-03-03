package com.jc.jcsports.activities.diffUtils;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import com.jc.jcsports.model.BulletinModel;
import com.jc.jcsports.model.ThumbInfoModel;

public class ThumbInfoModelDiffUtil  extends DiffUtil.ItemCallback<ThumbInfoModel> {


    @Override
    public boolean areItemsTheSame(@NonNull ThumbInfoModel oldItem, @NonNull ThumbInfoModel newItem) {
        return oldItem.toString().equals(newItem.toString());
    }

    @Override
    public boolean areContentsTheSame(@NonNull ThumbInfoModel oldItem, @NonNull ThumbInfoModel newItem) {
        return oldItem.toString().equals(newItem.toString());
    }

}