package com.jc.jcsports.viewModel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.jc.jcsports.model.LoginModel;

import java.util.ArrayList;
import java.util.List;

public class UserListViewModel extends ViewModel {

    private MutableLiveData<List<LoginModel>> listMutableLiveData;
    private List<LoginModel> loginModels;

    public MutableLiveData<List<LoginModel>> getListMutableLiveData() {
        if (listMutableLiveData == null) {
            listMutableLiveData = new MutableLiveData<>();
        }
        return listMutableLiveData;
    }


    public void init() {
        if (listMutableLiveData == null) {
            listMutableLiveData = new MutableLiveData<>();
        }
        if (loginModels == null) {
            loginModels = new ArrayList<>();
        }
    }

    public void setLoginModels(List<LoginModel> loginModels) {
        if (this.loginModels == null) return;
        this.loginModels.addAll(loginModels);
        listMutableLiveData.postValue(this.loginModels);
    }

    public void loginModelsInit(List<LoginModel> loginModels) {
        this.loginModels = loginModels;
        listMutableLiveData.postValue(this.loginModels);
    }


    public void remove() {
        loginModels.clear();
    }

}
