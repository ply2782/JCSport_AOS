package com.jc.jcsports.activities.filelistFunctions.fileCreateFunctions;

import android.view.View;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.jc.jcsports.R;
import com.jc.jcsports.bundle.SignUpBundle;
import com.jc.jcsports.databinding.ActivityFileCreateBinding;
import com.jc.jcsports.model.LoginModel;
import com.jc.jcsports.utils.AbstractActivity;
import com.jc.jcsports.viewModel.FileContentsTextCountViewModel;


public class FileCreateActivity extends AbstractActivity  {

    public ActivityFileCreateBinding binding;
    public FileCreateFunctions fileDetailFunctions;
    private LoginModel loginModel;

    @Override
    public void basicSetting() {
        binding = DataBindingUtil.setContentView(FileCreateActivity.this, R.layout.activity_file_create);
        binding.setLifecycleOwner(FileCreateActivity.this);
        binding.setThisActivity(this);
        FileContentsTextCountViewModel fileContentsTextCountViewModel = new ViewModelProvider(FileCreateActivity.this).get(FileContentsTextCountViewModel.class);
        fileContentsTextCountViewModel.init();
        binding.setCountText(fileContentsTextCountViewModel);
        fileDetailFunctions = new FileCreateFunctions(
                context,
                fileContentsTextCountViewModel,
                binding.fileViewPager2,
                binding.intoTabLayout,
                this);
        loginModelInit();

    }


    private void loginModelInit() {
        loginModel = (LoginModel) SignUpBundle.getBundle().getSerializable("loginModel");
    }

    public void completed(View v) {
        fileDetailFunctions.complete(binding.contentEditText.getText().toString(), loginModel.s_Number);

    }


}