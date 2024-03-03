package com.jc.jcsports.activities.mainFunctions;

import android.view.Window;

import androidx.databinding.DataBindingUtil;

import com.jc.jcsports.R;
import com.jc.jcsports.databinding.ActivityMainBinding;
import com.jc.jcsports.utils.AbstractActivity;
import com.jc.jcsports.utils.Utils;

public class MainActivity extends AbstractActivity {

    private ActivityMainBinding binding;
    private MainFunctions mainFunctions;

    @Override
    public void basicSetting() {
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        binding = DataBindingUtil.setContentView(MainActivity.this, R.layout.activity_main);
        binding.setLifecycleOwner(MainActivity.this);
        mainFunctions = new MainFunctions();
        mainFunctions.moveNext(this);

    }
}