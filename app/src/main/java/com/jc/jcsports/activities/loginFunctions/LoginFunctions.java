package com.jc.jcsports.activities.loginFunctions;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.jc.jcsports.activities.navigationFunctions.MainNavigationActivity;
import com.jc.jcsports.activities.phoneFunctions.PhoneAuthenticActivity;
import com.jc.jcsports.bundle.SignUpBundle;
import com.jc.jcsports.model.BulletinModel;
import com.jc.jcsports.model.LoginModel;
import com.jc.jcsports.utils.RetrofitClient;
import com.jc.jcsports.utils.RetrofitService;
import com.jc.jcsports.utils.ServerURL;
import com.jc.jcsports.utils.Utils;
import com.kakao.sdk.user.UserApiClient;
import com.kakao.sdk.user.model.User;

import java.io.Serializable;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.SingleObserver;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;


public class LoginFunctions {
    private RetrofitService service;
    private RetrofitClient retrofitClient;

    public LoginFunctions() {
        SignUpBundle.getInstance();
        retroFitInit();
    }

    //todo : RestApi init
    public void retroFitInit() {
        retrofitClient = RetrofitClient.getInstance();
        service = RetrofitClient.getServerInterface();
    }


    private void login(Context context, LoginActivity activity) {
        UserApiClient.getInstance().me((user, throwable13) -> {
            isExist_SignUp(String.valueOf(user.getId()), context, activity, user);
            return null;
        });
    }

    //todo : 카카오 로그인 기능
    public void loginKakao(Context context, LoginActivity activity) {

        //todo : 카카오톡 앱 있을 경우
        if (UserApiClient.getInstance().isKakaoTalkLoginAvailable(context)) {

            UserApiClient.getInstance().loginWithKakaoTalk(context, (oAuthToken, throwable) -> {
                if (throwable != null) {
                    // 사용자가 카카오톡 설치 후 디바이스 권한 요청 화면에서 로그인을 취소한 경우,
                    // 의도적인 로그인 취소로 보고 카카오계정으로 로그인 시도 없이 로그인 취소로 처리 (예: 뒤로 가기)
                    if (throwable.getMessage().equals("user cancelled.")) {
                        UserApiClient.getInstance().loginWithKakaoTalk(context, (oAuthToken1, throwable1) -> {
                            login(context, activity);
                            return null;
                        });
                    }
                    // 카카오톡에 연결된 카카오계정이 없는 경우, 카카오계정으로 로그인 시도
                    UserApiClient.getInstance().loginWithKakaoAccount(context, (oAuthToken12, throwable12) -> {
                        login(context, activity);
                        return null;
                    });

                } else {
                    login(context, activity);
                }
                return null;
            });
        } else {
            //todo : 카카오톡 앱 없을 경우
            UserApiClient.getInstance().loginWithKakaoAccount(context, (oAuthToken, throwable) -> {
                if (throwable == null) {
                    login(context, activity);
                }
                return null;
            });
        }
    }

    public void isExist_SignUp(String getId, Context context,
                               LoginActivity activity,
                               User user) {

        service.isExists(ServerURL.userController, getId)
                .subscribeOn(Schedulers.single())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                    }

                    @Override
                    public void onSuccess(@NonNull LoginModel loginModel) {
                        Utils.logCheck("loginModel => " + loginModel.toString());
                        if(loginModel.s_Number == 0){
                            signUp(activity, user);
                        }else{
                            SignUpBundle.getBundle().putSerializable("loginModel", (Serializable) loginModel);
                            nextPage(context);
                        }

                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        signUp(activity, user);
                    }
                });
    }

    public void nextPage(Context context) {
        Intent intent = new Intent(context, MainNavigationActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
    }

    public void signUp(LoginActivity activity, User user) {
        Intent intent = new Intent(activity, PhoneAuthenticActivity.class);
        SignUpBundle.getBundle().putString("s_Social", "kakao");
        SignUpBundle.getBundle().putString("s_Gender", user.getKakaoAccount().component19().toString());
        SignUpBundle.getBundle().putString("s_Birth", user.getKakaoAccount().getBirthday());
        SignUpBundle.getBundle().putString("s_AgeRange", user.getKakaoAccount().getAgeRange().toString());
        SignUpBundle.getBundle().putString("s_SocialToken", String.valueOf(user.getId()));
        activity.startActivity(intent);
    }
}
