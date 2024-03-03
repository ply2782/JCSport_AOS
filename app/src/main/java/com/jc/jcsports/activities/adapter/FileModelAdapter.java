package com.jc.jcsports.activities.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.jc.jcsports.R;
import com.jc.jcsports.activities.filelistFunctions.fileDetailFunctions.FileDetailActivity;
import com.jc.jcsports.databinding.ItemFileListBinding;
import com.jc.jcsports.model.BulletinModel;
import com.jc.jcsports.model.FileModel;
import com.jc.jcsports.model.ThumbInfoModel;
import com.jc.jcsports.utils.ServerURL;
import com.jc.jcsports.utils.Utils;

import org.jetbrains.annotations.NotNull;

public class FileModelAdapter extends ListAdapter<BulletinModel, RecyclerView.ViewHolder> {

    private Context context;

    public FileModelAdapter(@NonNull DiffUtil.ItemCallback<BulletinModel> diffCallback, Context context) {
        super(diffCallback);
        this.context = context;
    }

    @Override
    public int getItemViewType(int position) {
        return R.layout.item_file_list;
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemFileListBinding binding = ItemFileListBinding.inflate(inflater, parent, false);
        return new ItemFileListBindingAdapterViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ItemFileListBindingAdapterViewHolder) {
            ItemFileListBindingAdapterViewHolder adapterViewHolder = (ItemFileListBindingAdapterViewHolder) holder;
            adapterViewHolder.onBind(getItem(position));
        }
    }

    public class ItemFileListBindingAdapterViewHolder extends RecyclerView.ViewHolder {

        private ItemFileListBinding binding;

        public ItemFileListBindingAdapterViewHolder(@NonNull @NotNull ItemFileListBinding binding_Copy) {
            super(binding_Copy.getRoot());
            binding = binding_Copy;
            binding.setViewHolder(this);
        }

        public void onBind(BulletinModel bulletinModel) {
            ThumbInfoModel thumbInfoModel = bulletinModel.t_Model;
            if (thumbInfoModel != null) {
                int b_Number = thumbInfoModel.b_Number;
                String f_Name = thumbInfoModel.b_Thumb;
                String type = thumbInfoModel.b_ThumbType;
                String imageUrl;
                if (type.equals("video")) {
                    imageUrl = ServerURL.VIDEO_URL + f_Name;
                } else {
                    imageUrl = ServerURL.IMAGE_URL + f_Name;
                }
                binding.setFName(imageUrl);
                binding.setBNumber(b_Number);
            }else{
                binding.setBNumber(0);
            }
            binding.executePendingBindings();
        }

        public void moveActivity(View v, int b_Number) {
            Intent intent = new Intent(context, FileDetailActivity.class);
            intent.putExtra("b_Number", b_Number);
            context.startActivity(intent);
        }
    }
}
