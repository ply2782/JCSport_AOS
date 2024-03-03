package com.jc.jcsports.viewModel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.jc.jcsports.model.chat.ChatListModel;
import com.jc.jcsports.model.chat.ChattingModel;

import java.util.ArrayList;
import java.util.List;

public class ChattingViewModel extends ViewModel {
    private MutableLiveData<List<ChattingModel>> modelMutableLiveData;
    private List<ChattingModel> chattingModels;

    public List<ChattingModel> getChattingModels() {
        return chattingModels;
    }

    public void init() {
        if (modelMutableLiveData == null) {
            modelMutableLiveData = new MutableLiveData<>();
        }
        chattingModels = new ArrayList<>();
        modelMutableLiveData.postValue(chattingModels);
    }

    public MutableLiveData<List<ChattingModel>> getModelMutableLiveData() {
        return modelMutableLiveData;
    }

    public void addAllItem(List<ChattingModel> chatListModels) {
        this.chattingModels.addAll(chatListModels);
        modelMutableLiveData.postValue(chattingModels);

    }

    public void addItem(ChattingModel chattingModel) {
        this.chattingModels.add(0,chattingModel);
        modelMutableLiveData.postValue(chattingModels);
    }

    public void setItem(List<ChattingModel> chatListModels) {
        this.chattingModels = chatListModels;
        modelMutableLiveData.postValue(chattingModels);

    }
}
