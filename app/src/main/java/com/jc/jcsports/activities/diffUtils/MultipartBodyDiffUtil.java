package com.jc.jcsports.activities.diffUtils;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import com.jc.jcsports.model.FileModel;

import okhttp3.MultipartBody;

public class MultipartBodyDiffUtil extends DiffUtil.ItemCallback<MultipartBody.Part> {


    @Override
    public boolean areItemsTheSame(@NonNull MultipartBody.Part oldItem, @NonNull MultipartBody.Part newItem) {
        return oldItem == newItem;
    }

    @Override
    public boolean areContentsTheSame(@NonNull MultipartBody.Part oldItem, @NonNull MultipartBody.Part newItem) {
        String prefix = "filename=";
        String url_old = oldItem.headers().value(0).split(";")[2].substring((prefix.length() + 1));
        String url_new = oldItem.headers().value(0).split(";")[2].substring((prefix.length() + 1));
        return url_old.equals(url_new);
    }
}
