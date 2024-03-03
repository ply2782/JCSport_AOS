package com.jc.jcsports.activities.agreeFunctions;

import android.content.Intent;
import android.view.View;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.jc.jcsports.R;
import com.jc.jcsports.activities.loginFunctions.LoginActivity;
import com.jc.jcsports.databinding.ActivityAgreeBinding;
import com.jc.jcsports.utils.AbstractActivity;

import java.util.HashMap;
import java.util.Map;

public class AgreeActivity extends AbstractActivity {

    private ActivityAgreeBinding binding;
    private AgreeFunctions agreeFunctions;

    @Override
    public void basicSetting() {
        binding = DataBindingUtil.setContentView(AgreeActivity.this, R.layout.activity_agree);
        binding.setLifecycleOwner(AgreeActivity.this);
        binding.setThisActivity(AgreeActivity.this);
        agreeFunctions = new AgreeFunctions(context, AgreeActivity.this);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        agreeFunctions.clickMapInit();
    }

    public void checkAgreeFirst(View v) {
        agreeFunctions.checkAgreeFirst(binding.layoutExpand1, binding.arrowImageView1);

    }


    public void checkAgreeSecond(View v) {
        agreeFunctions.checkAgreeSecond(binding.layoutExpand2, binding.arrowImageView2);
    }


    public void checkAgreeThird(View v) {
        agreeFunctions.checkAgreeThird(binding.layoutExpand3, binding.arrowImageView3);
    }


}