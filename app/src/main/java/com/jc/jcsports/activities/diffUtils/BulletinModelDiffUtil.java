package com.jc.jcsports.activities.diffUtils;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import com.jc.jcsports.model.BulletinModel;

public class BulletinModelDiffUtil extends DiffUtil.ItemCallback<BulletinModel> {


    @Override
    public boolean areItemsTheSame(@NonNull BulletinModel oldItem, @NonNull BulletinModel newItem) {
        return oldItem.b_Number == newItem.b_Number;
    }

    @Override
    public boolean areContentsTheSame(@NonNull BulletinModel oldItem, @NonNull BulletinModel newItem) {
        return oldItem.toString().equals(newItem.toString());
    }

}

