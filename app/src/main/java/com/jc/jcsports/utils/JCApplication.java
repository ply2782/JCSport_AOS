package com.jc.jcsports.utils;

import android.app.Activity;
import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.messaging.FirebaseMessaging;
import com.jc.jcsports.model.LoginModel;
import com.kakao.sdk.common.KakaoSdk;

import java.io.File;

public class JCApplication extends Application {

    private static JCApplication instance;
    private boolean isBackground = false;

    // 앱의 캐시 디렉토리 삭제
    public static void deleteCache(Context context) {
        try {
            File cacheDir = context.getCacheDir();
            deleteRecursive(cacheDir);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 재귀적으로 디렉토리와 파일 삭제
    private static void deleteRecursive(File fileOrDirectory) {
        if (fileOrDirectory.isDirectory()) {
            for (File child : fileOrDirectory.listFiles()) {
                deleteRecursive(child);
            }
        }
        fileOrDirectory.delete();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        kakaoSetting();
        isCheckAboutForeGroundOrBackGround();
        Context context = getApplicationContext();
        deleteCache(context);

    }

    public void kakaoSetting() {
        KakaoSdk.init(this, "b01aaf833b224fe7a270f1899cd6ceff");
    }

    public void isCheckAboutForeGroundOrBackGround() {
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {

            }

            @Override
            public void onActivityStarted(@NonNull Activity activity) {

            }

            @Override
            public void onActivityResumed(Activity activity) {
                if (isBackground) {
                    isBackground = false;
                    /** 포그라운드일때*/
                    Log.d("debug", "isBackground : " + isBackground);
                }
            }

            @Override
            public void onActivityPaused(@NonNull Activity activity) {

            }

            @Override
            public void onActivityStopped(@NonNull Activity activity) {

            }

            @Override
            public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(@NonNull Activity activity) {
                if (isBackground == true) {

                }
            }
        });
        IntentFilter screenOffFilter = new IntentFilter(Intent.ACTION_SCREEN_OFF);
        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (isBackground) {
                    isBackground = false;
                    /** 백그라운드 스크린 꺼져있는 상태일때 */
                    Log.d("debug", "isBackground : false");
                }
            }
        }, screenOffFilter);
    }

    @Override
    public Context getApplicationContext() {
        return super.getApplicationContext();
    }
}
