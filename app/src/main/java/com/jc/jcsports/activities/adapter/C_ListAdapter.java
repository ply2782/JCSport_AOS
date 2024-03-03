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
import com.jc.jcsports.activities.chatFunctions.chatRoom.ChatRoomActivity;
import com.jc.jcsports.databinding.ItemCListBinding;
import com.jc.jcsports.model.chat.ChatJoinModel;
import com.jc.jcsports.model.chat.ChatListModel;
import com.jc.jcsports.model.chat.ChatMessageModel;
import com.jc.jcsports.utils.Utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class C_ListAdapter extends ListAdapter<ChatListModel, RecyclerView.ViewHolder> {

    private Context context;
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public void setContext(Context context) {
        this.context = context;

    }

    public C_ListAdapter(@NonNull DiffUtil.ItemCallback<ChatListModel> diffCallback) {
        super(diffCallback);
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public int getItemViewType(int position) {
        return R.layout.item_c_list;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemCListBinding binding = ItemCListBinding.inflate(inflater, parent, false);
        binding.setCListAdapter(this);
        return new ItemCListViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ItemCListViewHolder) {
            ItemCListViewHolder adapterViewHolder = (ItemCListViewHolder) holder;
            adapterViewHolder.setContext(context);
            adapterViewHolder.setItem(getItem(position));
        }
    }


    public void move(View v, int c_Number) {
        Context context = v.getContext();
        Intent intent = new Intent(context, ChatRoomActivity.class);
        intent.putExtra("c_Number", c_Number);
        context.startActivity(intent);
    }


    public class ItemCListViewHolder extends RecyclerView.ViewHolder {
        private ItemCListBinding binding;
        private Context context;

        public ItemCListViewHolder(@NonNull ItemCListBinding binding) {
            super(binding.getRoot());
            this.binding = binding;


        }


        public void setContext(Context context) {
            this.context = context;
        }

        public void setItem(ChatListModel chatListModel) {

            try {

                if (chatListModel.c_JoinModel.size() > 0) {
                    List<String> joinList = new ArrayList<>();
                    for (ChatJoinModel c : chatListModel.c_JoinModel) {
                        joinList.add(c.s_LoginModel.s_NickName);
                    }
                    Date d = simpleDateFormat.parse(chatListModel.chatMessageModel.c_MessageTime);
                    chatListModel.chatMessageModel.c_MessageTime = Utils.txtDate(d);
                    binding.setJoinList(joinList);
                    binding.setCListModel(chatListModel);
                }
                binding.executePendingBindings();

            } catch (ParseException e) {
                e.printStackTrace();
            }
        }


    }
}
