package com.jc.jcsports.activities.filelistFunctions.fileDetailFunctions;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.jc.jcsports.R;
import com.jc.jcsports.activities.signupFunctions.SignUpActivity;
import com.jc.jcsports.bundle.SignUpBundle;
import com.jc.jcsports.databinding.ActivityFileDetailBinding;
import com.jc.jcsports.model.BulletinModel;
import com.jc.jcsports.model.LoginModel;
import com.jc.jcsports.utils.AbstractActivity;
import com.jc.jcsports.utils.RetrofitClient;
import com.jc.jcsports.utils.RetrofitService;
import com.jc.jcsports.utils.ServerURL;
import com.jc.jcsports.utils.Utils;
import com.jc.jcsports.utils.ViewModelFactory;
import com.jc.jcsports.viewModel.B_Like_Count_ViewModel;
import com.jc.jcsports.viewModel.B_Reply_Count_ViewModel;
import com.jc.jcsports.viewModel.LoginViewModel;
import com.jc.jcsports.viewModel.NickNameTextCountViewModel;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.SingleObserver;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import okhttp3.internal.Util;

public class FileDetailActivity extends AbstractActivity {

    public ActivityFileDetailBinding binding;
    public FileDetailFunctions fileDetailFunctions;
    private RetrofitService service;
    private RetrofitClient retrofitClient;
    private LoginModel loginModel;
    private B_Like_Count_ViewModel b_Like_Count_ViewModel;
    private B_Reply_Count_ViewModel b_reply_count_viewModel;
    private BroadcastReceiver br;
    private int b_Number = 0;

    @Override
    public void basicSetting() {
        binding = DataBindingUtil.setContentView(FileDetailActivity.this, R.layout.activity_file_detail);
        binding.setLifecycleOwner(FileDetailActivity.this);
        binding.setThisActivity(this);
        loginModelInit();
        b_Number = getResultIntent();
        retroFitInit();
        fileDetailFunctions = new FileDetailFunctions(this, context,
                binding.fileViewPager2, b_Number, service, binding.bContentView);
        fileDetailFunctions.setB_Like_Count_ViewModel(b_Like_Count_ViewModel);
        fileDetailFunctions.setB_reply_count_viewModel(b_reply_count_viewModel);
        b_Init(b_Number);
        brInit();
    }

    private void brInit() {
        br = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                //todo : refresh
                boolean params_1 = intent.getBooleanExtra("b_Content", false);
                boolean params_2 = intent.getBooleanExtra("b_File", false);
                if (params_1) {
                    b_Init(b_Number);
                }
                if (params_2) {
                    fileDetailFunctions.api_File();
                }
            }
        };
    }


    @Override
    protected void onStop() {
        super.onStop();
        fileDetailFunctions.stopVideo();
        Utils.logCheck("FileDetailActivity is onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Utils.logCheck("FileDetailActivity is onDestroy");
        if (br != null) {
            unregisterReceiver(br);
            fileDetailFunctions.exoplayerRelease();
        }

    }

    private void loginModelInit() {
        loginModel = (LoginModel) SignUpBundle.getBundle().getSerializable("loginModel");
        Utils.logCheck(loginModel.s_PhotoList.toString());
        b_Like_Count_ViewModel = new ViewModelProvider(FileDetailActivity.this).get(B_Like_Count_ViewModel.class);
        b_Like_Count_ViewModel.init();
        b_reply_count_viewModel = new ViewModelProvider(FileDetailActivity.this).get(B_Reply_Count_ViewModel.class);
        b_reply_count_viewModel.init();
        binding.setLoginModel(loginModel);
        binding.setBLikeCountViewModel(b_Like_Count_ViewModel);
        binding.setBReplyViewModel(b_reply_count_viewModel);
    }

    //todo : RestApi init
    private void retroFitInit() {
        retrofitClient = RetrofitClient.getInstance();
        service = RetrofitClient.getServerInterface();
    }

    public void Like(View v) {
        fileDetailFunctions.b_Like();
    }

    private void b_Init(int b_Number) {
        int s_Number = loginModel.s_Number;
        service.getB_DetailContent(ServerURL.bulletinController, b_Number, s_Number)
                .subscribeOn(Schedulers.single())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onSuccess(@NonNull BulletinModel bulletinModel) {
                        fileDetailFunctions.exoplayerRelease();
                        binding.setBModel(bulletinModel);
                        b_Like_Count_ViewModel.setCount(bulletinModel.b_Like_Count);
                        b_reply_count_viewModel.setCount(bulletinModel.b_Reply_Count);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }
                });
    }


    public void showLookListDialog(View v) {
        fileDetailFunctions.showLookListDialog();
    }

    public void showReplyDialog(View v) {
        fileDetailFunctions.showReplyDialog();
    }

    private int getResultIntent() {
        Intent intent = getIntent();
        return intent.getIntExtra("b_Number", 0);
    }

    public void showMenuPopUp(View v) {
        fileDetailFunctions.showDialog();
    }

    public void back(View v) {
        fileDetailFunctions.back();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Utils.logCheck("FileDetailActivity is onResume");
        IntentFilter filter_B_Content = new IntentFilter();
        filter_B_Content.addAction("b_Detail");
        registerReceiver(br, filter_B_Content);
        fileDetailFunctions.startVideo();
    }


}