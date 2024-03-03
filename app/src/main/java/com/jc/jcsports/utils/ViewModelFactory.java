package com.jc.jcsports.utils;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.jc.jcsports.viewModel.B_Look_List_ViewModel;
import com.jc.jcsports.viewModel.BulletinViewModel;
import com.jc.jcsports.viewModel.ChatListViewModel;
import com.jc.jcsports.viewModel.ChattingViewModel;
import com.jc.jcsports.viewModel.LoginViewModel;
import com.jc.jcsports.viewModel.MyBulletinViewModel;
import com.jc.jcsports.viewModel.MyThumbInfoViewModel;
import com.jc.jcsports.viewModel.ReplyModelViewModel;
import com.jc.jcsports.viewModel.ThumbInfoViewModel;
import com.jc.jcsports.viewModel.UserListViewModel;

import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unchecked")
public class ViewModelFactory implements ViewModelProvider.Factory {
    @NonNull
    @NotNull
    @Override
    public <T extends ViewModel> T create(@NonNull @NotNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(LoginViewModel.class)) {
            return (T) new LoginViewModel();
        } else if (modelClass.isAssignableFrom(BulletinViewModel.class)) {
            return (T) new BulletinViewModel();
        } else if (modelClass.isAssignableFrom(MyBulletinViewModel.class)) {
            return (T) new MyBulletinViewModel();
        } else if (modelClass.isAssignableFrom(ThumbInfoViewModel.class)) {
            return (T) new ThumbInfoViewModel();
        } else if (modelClass.isAssignableFrom(MyThumbInfoViewModel.class)) {
            return (T) new MyThumbInfoViewModel();
        } else if (modelClass.isAssignableFrom(ReplyModelViewModel.class)) {
            return (T) new ReplyModelViewModel();
        }else if (modelClass.isAssignableFrom(B_Look_List_ViewModel.class)) {
            return (T) new B_Look_List_ViewModel();
        }else if (modelClass.isAssignableFrom(ChatListViewModel.class)) {
            return (T) new ChatListViewModel();
        }else if (modelClass.isAssignableFrom(ChattingViewModel.class)) {
            return (T) new ChattingViewModel();
        }else if (modelClass.isAssignableFrom(UserListViewModel.class)) {
            return (T) new UserListViewModel();
        }




        throw new IllegalArgumentException("Not found ViewModel class.");
    }
}

