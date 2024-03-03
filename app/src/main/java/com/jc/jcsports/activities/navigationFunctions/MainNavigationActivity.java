package com.jc.jcsports.activities.navigationFunctions;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import androidx.databinding.DataBindingUtil;

import com.jc.jcsports.R;
import com.jc.jcsports.bundle.SignUpBundle;
import com.jc.jcsports.databinding.ActivityMainNavigationBinding;
import com.jc.jcsports.model.LoginModel;
import com.jc.jcsports.utils.AbstractFragmentActivity;
import com.jc.jcsports.utils.Utils;

public class MainNavigationActivity extends AbstractFragmentActivity {

    private ActivityMainNavigationBinding binding;
    private MainNavigationFunctions mainNavigationFunctions;

    @Override
    public void basicSetting() {
        binding = DataBindingUtil.setContentView(MainNavigationActivity.this, R.layout.activity_main_navigation);
        binding.setLifecycleOwner(MainNavigationActivity.this);
        binding.setThisActivity(this);
        mainNavigationFunctions = new MainNavigationFunctions(
                MainNavigationActivity.this,
                binding.bottomNavigation);

    }

    public void show_UserInfo(View v) {
        mainNavigationFunctions.show_UserInfo();
    }

}