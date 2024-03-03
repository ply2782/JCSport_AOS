package com.jc.jcsports.viewModel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.jc.jcsports.model.BulletinModel;
import com.jc.jcsports.model.LoginModel;

import java.util.ArrayList;
import java.util.List;

public class B_Look_List_ViewModel extends ViewModel {
    private MutableLiveData<List<LoginModel>> liveData;
    private List<LoginModel> loginModels;

    public MutableLiveData<List<LoginModel>> getLiveData() {
        return liveData;
    }

    public void init() {
        if (liveData == null) {
            liveData = new MutableLiveData<>();
        }
        if (loginModels == null) {
            loginModels = new ArrayList<>();
        }
        liveData.postValue(loginModels);
    }

    //todo : 기존에 만들어 놓은 참조값으로 리스트를 늘려나갈 경우 diffUtil에서 같은 참조값으로 인식하기 때문에 기존 총합 갯수 유지하면서
    //todo : 새로운 갯수 추가로 넣어줄 수 있음
    //todo : 만약 new ArrayList<>() 를 만들고 거기에 addAll 로 해준다음 viewModel 에 postValue 해준다면 새로운 참조값으로 인식되어
    //todo : 기존에 가졌던 아이템 과 새로운 아이템의 속성이 같으면 지우고 다른 아이템만 보여줘서 아이템의 갯수가 유지되지 않고 새로 받은 아이템의 갯수만 표현된다
    public void addFileModelList(List<LoginModel> loginModels) {
        this.loginModels.addAll(loginModels);
        liveData.postValue(this.loginModels);
    }

    public void setFileModelList(List<LoginModel> loginModels) {
        this.loginModels = loginModels;
        liveData.postValue(this.loginModels);
    }

}