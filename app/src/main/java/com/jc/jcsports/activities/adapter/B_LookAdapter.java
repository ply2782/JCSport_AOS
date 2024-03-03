package com.jc.jcsports.activities.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.jc.jcsports.R;
import com.jc.jcsports.databinding.ItemBLookListBinding;
import com.jc.jcsports.model.LoginModel;
import com.jc.jcsports.utils.Utils;

public class B_LookAdapter extends ListAdapter<LoginModel, RecyclerView.ViewHolder> {
    private Context context;

    public B_LookAdapter(@NonNull DiffUtil.ItemCallback<LoginModel> diffCallback, Context context) {
        super(diffCallback);
        this.context = context;
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public int getItemViewType(int position) {
        return R.layout.item_b_look_list;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemBLookListBinding binding = ItemBLookListBinding.inflate(inflater, parent, false);
        return new ItemBLookListViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ItemBLookListViewHolder) {
            ItemBLookListViewHolder adapterViewHolder = (ItemBLookListViewHolder) holder;
            adapterViewHolder.onBind(getItem(position));
        }
    }

    public class ItemBLookListViewHolder extends RecyclerView.ViewHolder {

        private ItemBLookListBinding itemBLookListBinding;

        public ItemBLookListViewHolder(@NonNull ItemBLookListBinding itemBLookListBinding) {
            super(itemBLookListBinding.getRoot());
            this.itemBLookListBinding = itemBLookListBinding;
        }

        public void onBind(LoginModel loginModel) {
            itemBLookListBinding.setLoginModel(loginModel);
            itemBLookListBinding.executePendingBindings();
        }
    }
}
