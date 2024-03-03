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
import com.jc.jcsports.databinding.ItemViewPager2FileListBinding;
import com.jc.jcsports.model.FileModel;

import org.jetbrains.annotations.NotNull;


public class ViewPager2FileModelAdapter extends ListAdapter<FileModel, RecyclerView.ViewHolder> {

    public final Context context;
    public AlertDialog alertDialog;
    public CommonInterface.ClickInterface clickInterface;

    public static class ItemViewPager2FileListBindingViewHolder extends RecyclerView.ViewHolder {
        public ItemViewPager2FileListBinding binding;

        public ItemViewPager2FileListBindingViewHolder(
                @NonNull @NotNull ItemViewPager2FileListBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }

        public void onBind(FileModel fileModel, int position) {
            binding.setFileModel(fileModel);
            binding.setPosition(position);
            binding.executePendingBindings();
        }


    }

    public void setClickInterface(CommonInterface.ClickInterface clickInterface) {
        this.clickInterface = clickInterface;
    }

    public ViewPager2FileModelAdapter(@NonNull DiffUtil.ItemCallback<FileModel> diffCallback, Context context) {
        super(diffCallback);
        this.context = context;

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
        return R.layout.item_view_pager2_file_list;
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemViewPager2FileListBinding binding = ItemViewPager2FileListBinding.inflate(inflater, parent, false);
        binding.setAdapter(this);
        return new ItemViewPager2FileListBindingViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ItemViewPager2FileListBindingViewHolder) {
            ItemViewPager2FileListBindingViewHolder adapterViewHolder = (ItemViewPager2FileListBindingViewHolder) holder;
            adapterViewHolder.onBind(getItem(position), position);

        }
    }

}
