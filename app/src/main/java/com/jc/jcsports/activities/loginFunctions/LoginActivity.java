package com.jc.jcsports.activities.loginFunctions;

import androidx.databinding.DataBindingUtil;

import android.view.View;

import com.jc.jcsports.R;
import com.jc.jcsports.databinding.ActivityLoginBinding;
import com.jc.jcsports.utils.AbstractActivity;

public class LoginActivity extends AbstractActivity {


    private ActivityLoginBinding binding;
    private LoginFunctions loginFunctions;

    @Override
    public void basicSetting() {
        binding = DataBindingUtil.setContentView(LoginActivity.this, R.layout.activity_login);
        binding.setLifecycleOwner(LoginActivity.this);
        binding.setThisActivity(LoginActivity.this);
        loginFunctions = new LoginFunctions();
    }


    //todo 카카오 로그인
    public void loginKakao(View v) {
        loginFunctions.loginKakao(context, this);
    }
}