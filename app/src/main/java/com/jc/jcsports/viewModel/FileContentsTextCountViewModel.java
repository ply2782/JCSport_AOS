package com.jc.jcsports.viewModel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class FileContentsTextCountViewModel extends ViewModel {
    private MutableLiveData<String> stringMutableLiveData;
    private String text = "( 0 / 500 )";

    public void init() {
        if (stringMutableLiveData == null) {
            stringMutableLiveData = new MutableLiveData<>();
        }
        initText();
    }

    public MutableLiveData<String> getStringMutableLiveData() {
        return stringMutableLiveData;
    }

    private void initText() {
        stringMutableLiveData.postValue(this.text);
    }

    public void setText(String text) {
        if (text == null) return;
        this.text = text;
        stringMutableLiveData.postValue(this.text);
    }

}
