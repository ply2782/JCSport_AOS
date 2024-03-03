package com.jc.jcsports.viewModel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.jc.jcsports.model.chat.ChatListModel;

import java.util.ArrayList;
import java.util.List;

public class ChatListViewModel extends ViewModel {
    private MutableLiveData<List<ChatListModel>> modelMutableLiveData;
    private List<ChatListModel> chatListModelList;

    public void init() {
        if (modelMutableLiveData == null) {
            modelMutableLiveData = new MutableLiveData<>();
        }
        chatListModelList = new ArrayList<>();
        modelMutableLiveData.postValue(chatListModelList);
    }

    public MutableLiveData<List<ChatListModel>> getModelMutableLiveData() {
        return modelMutableLiveData;
    }

    public void addItem(List<ChatListModel> chatListModels) {
        this.chatListModelList.addAll(chatListModels);
        modelMutableLiveData.postValue(chatListModelList);

    }

    public void setItem(List<ChatListModel> chatListModels) {
        this.chatListModelList = chatListModels;
        modelMutableLiveData.postValue(chatListModelList);

    }


    public void removeItem() {
        chatListModelList.clear();
    }


}