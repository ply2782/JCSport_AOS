package com.jc.jcsports.activities.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.jc.jcsports.R;
import com.jc.jcsports.activities.chatFunctions.chatRoom.ChatRoomActivity;
import com.jc.jcsports.databinding.ItemBFileListBinding;
import com.jc.jcsports.databinding.ItemUserlistBinding;
import com.jc.jcsports.model.LoginModel;
import com.jc.jcsports.utils.RetrofitService;
import com.jc.jcsports.utils.ServerURL;
import com.jc.jcsports.utils.Utils;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.SingleObserver;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class U_UserListAdapter extends ListAdapter<LoginModel, RecyclerView.ViewHolder> {

    private Context context;
    private RetrofitService service;
    private int myS_Number;

    public U_UserListAdapter(@NonNull DiffUtil.ItemCallback<LoginModel> diffCallback) {
        super(diffCallback);
    }

    public void setMyS_Number(int myS_Number) {
        this.myS_Number = myS_Number;
    }

    public void setService(RetrofitService service) {
        this.service = service;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }


    @Override
    public int getItemCount() {
        return super.getItemCount();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemUserlistBinding binding = ItemUserlistBinding.inflate(inflater, parent, false);
        binding.setThisAdapter(this);
        return new ItemUserListViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ItemUserListViewHolder) {
            ItemUserListViewHolder adapterViewHolder = (ItemUserListViewHolder) holder;
            adapterViewHolder.onBind(getItem(position));
            adapterViewHolder.setContext(context);
        }
    }


    public void letsChat(View v, int yourS_Number) {
        Utils.logCheck("myS_Number, yourS_Number "+myS_Number +" , " +yourS_Number);
        service.goToChatRoom(ServerURL.userListController, myS_Number, yourS_Number)
                .subscribeOn(Schedulers.single())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<>() {
                    @Override
                    public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {

                    }

                    @Override
                    public void onSuccess(@io.reactivex.rxjava3.annotations.NonNull Integer integer) {
                        Intent intent = new Intent(context, ChatRoomActivity.class);
                        intent.putExtra("c_Number", integer);
                        context.startActivity(intent);
                    }

                    @Override
                    public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {

                    }
                });

    }

    @Override
    public int getItemViewType(int position) {
        return R.layout.item_userlist;
    }

    public class ItemUserListViewHolder extends RecyclerView.ViewHolder {

        private ItemUserlistBinding binding;
        private Context context;

        public ItemUserListViewHolder(@NonNull ItemUserlistBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }


        public void setContext(Context context) {
            this.context = context;
        }

        public void onBind(LoginModel loginModel) {
            binding.setLModel(loginModel);
            binding.executePendingBindings();
        }


    }
}
