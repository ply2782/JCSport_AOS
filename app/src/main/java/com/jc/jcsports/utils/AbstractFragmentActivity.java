package com.jc.jcsports.utils;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.FragmentActivity;

public abstract class AbstractFragmentActivity extends FragmentActivity {
    public Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        createObjects();
    }

    public void createObjects() {
        context = this;
        //todo ------
        basicSetting();
    }

    public abstract void basicSetting();

}
