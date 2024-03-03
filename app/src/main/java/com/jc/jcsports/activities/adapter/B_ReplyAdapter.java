package com.jc.jcsports.activities.adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.jc.jcsports.R;
import com.jc.jcsports.bundle.SignUpBundle;
import com.jc.jcsports.databinding.ItemEmptyBinding;
import com.jc.jcsports.databinding.ItemMeBinding;
import com.jc.jcsports.databinding.ItemYouBinding;
import com.jc.jcsports.model.LoginModel;
import com.jc.jcsports.model.ReplyModel;
import com.jc.jcsports.utils.ServerURL;

public class B_ReplyAdapter extends ListAdapter<ReplyModel, RecyclerView.ViewHolder> {

    private Context context;
    private int myUid = 0;

    public B_ReplyAdapter(@NonNull DiffUtil.ItemCallback<ReplyModel> diffCallback, Context context) {
        super(diffCallback);
        this.context = context;
        LoginModel loginModel = (LoginModel) SignUpBundle.getBundle().getSerializable("loginModel");
        myUid = loginModel.s_Number;

    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public int getItemViewType(int position) {
        LoginModel loginModel = getItem(position).s_LoginModel;
        if (loginModel == null) {
            return R.layout.item_empty;
        } else {
            int i_Number = loginModel.s_Number;
            if (i_Number == myUid) {
                return R.layout.item_me;
            } else {
                return R.layout.item_you;
            }
        }
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        switch (viewType) {
            case R.layout.item_me:
                ItemMeBinding itemMeBinding = ItemMeBinding.inflate(inflater, viewGroup, false);
                return new ItemMeViewHolder(itemMeBinding);

            case R.layout.item_you:
                ItemYouBinding itemYouBinding = ItemYouBinding.inflate(inflater, viewGroup, false);
                return new ItemYouViewHolder(itemYouBinding);

            case R.layout.item_empty:
                ItemEmptyBinding itemEmptyBinding = ItemEmptyBinding.inflate(inflater, viewGroup, false);
                return new ItemEmptyViewHolder(itemEmptyBinding);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        if (viewHolder instanceof ItemMeViewHolder) {
            ((ItemMeViewHolder) viewHolder).onBind(getItem(i));
        } else if (viewHolder instanceof ItemYouViewHolder) {
            ((ItemYouViewHolder) viewHolder).onBind(getItem(i));
        }
    }

    public class ItemMeViewHolder extends RecyclerView.ViewHolder {

        private ItemMeBinding itemMeBinding;

        public ItemMeViewHolder(@NonNull ItemMeBinding itemMeBinding) {
            super(itemMeBinding.getRoot());
            this.itemMeBinding = itemMeBinding;
        }

        public void onBind(ReplyModel replyModel) {
            itemMeBinding.setRModel(replyModel);
            itemMeBinding.executePendingBindings();
        }
    }

    public class ItemYouViewHolder extends RecyclerView.ViewHolder {

        private ItemYouBinding itemYouBinding;

        public ItemYouViewHolder(@NonNull ItemYouBinding itemYouBinding) {
            super(itemYouBinding.getRoot());
            this.itemYouBinding = itemYouBinding;
        }

        public void onBind(ReplyModel replyModel) {
            itemYouBinding.setRModel(replyModel);
            itemYouBinding.executePendingBindings();
        }
    }

    public class ItemEmptyViewHolder extends RecyclerView.ViewHolder {

        private ItemEmptyBinding itemEmptyBinding;

        public ItemEmptyViewHolder(@NonNull ItemEmptyBinding itemEmptyBinding) {
            super(itemEmptyBinding.getRoot());
            this.itemEmptyBinding = itemEmptyBinding;
        }

    }


}
