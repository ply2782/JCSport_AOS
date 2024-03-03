package com.jc.jcsports.viewModel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class B_Like_Count_ViewModel extends ViewModel {
    private MutableLiveData<Integer> integerMutableLiveData;
    private int count = 0;

    public void init(){
        if (integerMutableLiveData == null) {
            integerMutableLiveData = new MutableLiveData<>();
        }
        integerMutableLiveData.postValue(count);
    }

    public MutableLiveData<Integer> getIntegerMutableLiveData() {

        return integerMutableLiveData;
    }

    public void setCount(int count) {
        this.count = count;
        integerMutableLiveData.postValue(this.count);
    }

}