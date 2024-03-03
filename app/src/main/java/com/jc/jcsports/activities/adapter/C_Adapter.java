package com.jc.jcsports.activities.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.jc.jcsports.R;

import com.jc.jcsports.databinding.ItemCMeBinding;
import com.jc.jcsports.databinding.ItemCTodayBinding;
import com.jc.jcsports.databinding.ItemCYouBinding;
import com.jc.jcsports.model.ChatModel;
import com.jc.jcsports.model.ReplyModel;

public class C_Adapter extends ListAdapter<ChatModel, RecyclerView.ViewHolder> {

    private Context context;

    public C_Adapter(@NonNull DiffUtil.ItemCallback<ChatModel> diffCallback) {
        super(diffCallback);
    }

    public void setContext(Context context) {
        this.context = context;
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        switch (viewType) {
            case R.layout.item_me:
                ItemCMeBinding itemMeBinding = ItemCMeBinding.inflate(inflater, viewGroup, false);
                return new ItemCMeViewHolder(itemMeBinding);

            case R.layout.item_you:
                ItemCYouBinding itemYouBinding = ItemCYouBinding.inflate(inflater, viewGroup, false);
                return new ItemCYouViewHolder(itemYouBinding);

            case R.layout.item_c_today:
                ItemCTodayBinding itemTodayBinding = ItemCTodayBinding.inflate(inflater, viewGroup, false);
                return new ItemCTodayViewHolder(itemTodayBinding);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        if (viewHolder instanceof ItemCMeViewHolder) {
            ((ItemCMeViewHolder) viewHolder).onBind(getItem(i));
        } else if (viewHolder instanceof ItemCYouViewHolder) {
            ((ItemCYouViewHolder) viewHolder).onBind(getItem(i));
        } else if (viewHolder instanceof ItemCTodayViewHolder) {
            ((ItemCTodayViewHolder) viewHolder).onBind(getItem(i));
        }
    }

    public class ItemCMeViewHolder extends RecyclerView.ViewHolder {

        private ItemCMeBinding itemCMeBinding;

        public ItemCMeViewHolder(@NonNull ItemCMeBinding itemCMeBinding) {
            super(itemCMeBinding.getRoot());
            this.itemCMeBinding = itemCMeBinding;
        }

        public void onBind(ChatModel chatModel) {
            itemCMeBinding.executePendingBindings();
        }
    }

    public class ItemCYouViewHolder extends RecyclerView.ViewHolder {

        private ItemCYouBinding itemCYouBinding;

        public ItemCYouViewHolder(@NonNull ItemCYouBinding itemCYouBinding) {
            super(itemCYouBinding.getRoot());
            this.itemCYouBinding = itemCYouBinding;
        }

        public void onBind(ChatModel chatModel) {
            itemCYouBinding.executePendingBindings();
        }
    }


    public class ItemCTodayViewHolder extends RecyclerView.ViewHolder {

        private ItemCTodayBinding itemCTodayBinding;


        public ItemCTodayViewHolder(@NonNull ItemCTodayBinding itemCTodayBinding) {
            super(itemCTodayBinding.getRoot());
            this.itemCTodayBinding = itemCTodayBinding;
        }

        public void onBind(ChatModel chatModel) {
            itemCTodayBinding.setCModel(chatModel);
            itemCTodayBinding.executePendingBindings();
        }
    }
}
