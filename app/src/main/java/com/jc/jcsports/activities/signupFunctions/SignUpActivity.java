package com.jc.jcsports.activities.signupFunctions;

import android.view.View;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.jc.jcsports.R;
import com.jc.jcsports.databinding.ActivitySignUpBinding;
import com.jc.jcsports.utils.AbstractActivity;
import com.jc.jcsports.utils.ViewModelFactory;
import com.jc.jcsports.viewModel.LoginViewModel;
import com.jc.jcsports.viewModel.NickNameTextCountViewModel;

public class SignUpActivity extends AbstractActivity {

    private ActivitySignUpBinding binding;
    public SignUpFunctions signUpFunctions;

    @Override
    public void basicSetting() {
        binding = DataBindingUtil.setContentView(SignUpActivity.this, R.layout.activity_sign_up);
        binding.setLifecycleOwner(SignUpActivity.this);
        binding.setThisActivity(SignUpActivity.this);
        NickNameTextCountViewModel liveDataTextCount = new ViewModelProvider(SignUpActivity.this).get(NickNameTextCountViewModel.class);
        binding.setCurrentCount(liveDataTextCount);
        signUpFunctions = new SignUpFunctions(
                this, context,
                binding.mainShapeableImageView,
                binding.secondShapeableImageView,
                binding.thirdShapeableImageView,
                binding.fourthShapeableImageView,
                binding.nicknameEditText,
                liveDataTextCount
        );
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        signUpFunctions.removeHandler();
    }


    public void getImageFile_1(View v) {
        signUpFunctions.firstImageViewPermission(context);
    }

    public void getImageFile_2(View v) {
        signUpFunctions.secondImageViewPermission(context);
    }

    public void getImageFile_3(View v) {
        signUpFunctions.thirdImageViewPermission(context);
    }

    public void getImageFile_4(View v) {
        signUpFunctions.fourthImageViewPermission(context);
    }


    public void removeImageFile_1(View v){
        signUpFunctions.removeImageView_1();
    }

    public void removeImageFile_2(View v){
        signUpFunctions.removeImageView_2();
    }

    public void removeImageFile_3(View v){
        signUpFunctions.removeImageView_3();
    }

    public void removeImageFile_4(View v){
        signUpFunctions.removeImageView_4();
    }

    public void checkDoubleNickName(View v) {
        String nickName = binding.nicknameEditText.getText().toString();
        signUpFunctions.checkDoubleNickName(context, nickName);

    }

    public void completeButton(View v) {
        signUpFunctions.signUp(context);
    }

}