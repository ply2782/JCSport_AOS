package com.jc.jcsports.utils;

import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public abstract class AbstractActivity extends AppCompatActivity {
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
