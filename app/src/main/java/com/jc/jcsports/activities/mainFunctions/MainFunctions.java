package com.jc.jcsports.activities.mainFunctions;

import android.content.Intent;
import android.os.Handler;

import com.jc.jcsports.activities.agreeFunctions.AgreeActivity;

public class MainFunctions {

    private Handler handler;
    public MainFunctions() {
        handler = new Handler();
    }

    public void moveNext(MainActivity activity) {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(activity, AgreeActivity.class); //화면 전환
                activity.startActivity(intent);
                activity.finish();
            }
        }, 3000); //딜레이 타임 조절
    }
}
