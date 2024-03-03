package com.jc.jcsports.activities.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.jc.jcsports.R;
import com.jc.jcsports.commonInterface.CommonInterface;
import com.jc.jcsports.databinding.ItemMultipartListBinding;
import com.jc.jcsports.utils.Utils;

import org.jetbrains.annotations.NotNull;

import okhttp3.MultipartBody;

public class MultipartBodyAdapter extends ListAdapter<MultipartBody.Part, RecyclerView.ViewHolder> {

    public final Context context;
    public AlertDialog alertDialog;
    public CommonInterface.ClickInterface clickInterface;
    private String imagePath = null;

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public MultipartBodyAdapter(@NonNull DiffUtil.ItemCallback<MultipartBody.Part> diffCallback, Context context) {
        super(diffCallback);
        this.context = context;
    }


    public static class ItemMultipartListBindingViewHolder extends RecyclerView.ViewHolder {
        public ItemMultipartListBinding binding;

        public ItemMultipartListBindingViewHolder(
                @NonNull @NotNull ItemMultipartListBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }

        public void onBind(MultipartBody.Part multiPart, int position, String imagePath) {
            String prefix = "filename=";
            String url = null;
            if (multiPart != null) {
                url = multiPart.headers().value(0).split(";")[2].substring((prefix.length() + 1));
                url = url.replaceAll(" ", "").replaceAll("\"", "").trim();
            }

            if (url != null) {
                String url_Image_Prefix = "showImage?";
                String url_Video_Prefix = "showVideo?";
                if (!url.contains(url_Image_Prefix) && !url.contains(url_Video_Prefix)) {
                    binding.setUrl(imagePath);
                } else {
                    binding.setUrl(url);
                }
            } else {
                binding.setUrl(url);
            }
            binding.setPosition(position);
            binding.executePendingBindings();
        }


    }

    public void setClickInterface(CommonInterface.ClickInterface clickInterface) {
        this.clickInterface = clickInterface;
    }


    public void setAlertDialog(AlertDialog alertDialog) {
        this.alertDialog = alertDialog;
    }

    //todo : 이미지 및 영상 파일 가져오기 기능
    public void getFile(View v, int position) {
        clickInterface.getPosition(position);
    }

    public void removeFile(View v, int position) {
        clickInterface.getRemovePosition(position);
    }


    @Override
    public int getItemViewType(int position) {
        return R.layout.item_multipart_list;
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemMultipartListBinding binding = ItemMultipartListBinding.inflate(inflater, parent, false);
        binding.setAdapter(this);
        return new ItemMultipartListBindingViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ItemMultipartListBindingViewHolder) {
            ItemMultipartListBindingViewHolder adapterViewHolder = (ItemMultipartListBindingViewHolder) holder;
            adapterViewHolder.onBind(getItem(position), position, imagePath);

        }
    }
}
