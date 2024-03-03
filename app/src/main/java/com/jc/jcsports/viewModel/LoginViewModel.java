package com.jc.jcsports.viewModel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.jc.jcsports.model.LoginModel;

public class LoginViewModel extends ViewModel {

    private MutableLiveData<LoginModel> listMutableLiveData;
    private LoginModel loginModels;

    public MutableLiveData<LoginModel> getListMutableLiveData() {
        if (listMutableLiveData == null) {
            listMutableLiveData = new MutableLiveData<>();
        }
        return listMutableLiveData;
    }

    public void setLoginModels(LoginModel loginModels) {
        if (this.loginModels == null) return;
        this.loginModels = loginModels;
        listMutableLiveData.postValue(this.loginModels);
    }


}
