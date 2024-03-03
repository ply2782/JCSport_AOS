package com.jc.jcsports.viewModel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.jc.jcsports.model.BulletinModel;
import com.jc.jcsports.model.LoginModel;
import com.jc.jcsports.model.ReplyModel;

import java.util.ArrayList;
import java.util.List;

public class ReplyModelViewModel extends ViewModel {

    private MutableLiveData<List<ReplyModel>> listMutableLiveData;
    private List<ReplyModel> replyModels;

    public MutableLiveData<List<ReplyModel>> getListMutableLiveData() {
        if (listMutableLiveData == null) {
            listMutableLiveData = new MutableLiveData<>();
        }
        return listMutableLiveData;
    }

    public void init() {
        if (listMutableLiveData == null) {
            listMutableLiveData = new MutableLiveData<>();
        }
        if (replyModels == null) {
            replyModels = new ArrayList<>();
        }
        listMutableLiveData.postValue(replyModels);
    }

    public void addReplyModelList(List<ReplyModel> replyModels) {
        this.replyModels.addAll(replyModels);
        listMutableLiveData.postValue(this.replyModels);
    }

    public void setReplyModelList(List<ReplyModel> replyModels) {
        this.replyModels = replyModels;
        listMutableLiveData.postValue(this.replyModels);
    }



    public void removeReplyModels() {
        replyModels.clear();
    }


}
