package com.jc.jcsports.viewModel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class NickNameTextCountViewModel extends ViewModel {
    private MutableLiveData<String> stringMutableLiveData;
    private String text = "";

    public MutableLiveData<String> getStringMutableLiveData() {
        if (stringMutableLiveData == null) {
            stringMutableLiveData = new MutableLiveData<>();
        }
        return stringMutableLiveData;
    }

    public void setText(String text) {
        if (text == null) return;
        this.text = text;
        stringMutableLiveData.postValue(this.text);
    }

}
