package com.jc.jcsports.activities.phoneFunctions;

import android.view.View;
import android.widget.Button;

import androidx.databinding.DataBindingUtil;

import com.jc.jcsports.R;
import com.jc.jcsports.databinding.ActivityPhoneAuthenticBinding;
import com.jc.jcsports.utils.AbstractActivity;

public class PhoneAuthenticActivity extends AbstractActivity {

    private ActivityPhoneAuthenticBinding binding;
    public PhoneFunctions phoneFunctions;

    @Override
    public void basicSetting() {
        binding = DataBindingUtil.setContentView(PhoneAuthenticActivity.this, R.layout.activity_phone_authentic);
        binding.setLifecycleOwner(PhoneAuthenticActivity.this);
        binding.setThisActivity(PhoneAuthenticActivity.this);
        phoneFunctions = new PhoneFunctions(context, binding.authenticEditText);
        phoneFunctions.setAuthenticButton(binding.checkPhoneButton);
        phoneFunctions.setCheckAuthenticButton(binding.checkAuthenticButton);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding.authenticEditText.setText("");
        binding.phoneEditText.setText("");
    }

    public void getAuthenticSmSCode(View v) {
        String phoneNumber = binding.phoneEditText.getText().toString();
        phoneFunctions.getAuthenticSmsCode(this, phoneNumber);

    }

    public void checkAuthentic(View v) {
        String writtenCode = binding.authenticEditText.getText().toString();
        phoneFunctions.checkAuthentic(this, context, writtenCode);

    }
}