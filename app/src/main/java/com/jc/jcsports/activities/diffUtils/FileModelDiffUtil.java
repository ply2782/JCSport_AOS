package com.jc.jcsports.activities.diffUtils;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import com.jc.jcsports.model.FileModel;

public class FileModelDiffUtil extends DiffUtil.ItemCallback<FileModel> {


    @Override
    public boolean areItemsTheSame(@NonNull FileModel oldItem, @NonNull FileModel newItem) {
        return oldItem.toString().equals(newItem.toString());
    }

    @Override
    public boolean areContentsTheSame(@NonNull FileModel oldItem, @NonNull FileModel newItem) {
        return oldItem.toString().equals(newItem.toString());
    }

}