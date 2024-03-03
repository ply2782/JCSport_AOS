package com.jc.jcsports.activities.agreeFunctions;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.jc.jcsports.activities.loginFunctions.LoginActivity;
import com.jc.jcsports.utils.Utils;

import java.util.HashMap;
import java.util.Map;

public class AgreeFunctions {

    private MutableLiveData<Map<String, Boolean>> isClick;
    private Observer<Map<String, Boolean>> clickObserver;
    private Map<String, Boolean> clickMap;
    private AgreeActivity activity;
    private Context context;

    public AgreeFunctions(Context context, AgreeActivity activity) {
        this.context = context;
        this.activity = activity;
        isClickInit();
        isClickObserverInit();
    }

    public void clickMapInit() {
        clickMap.clear();
        clickMap = null;
    }

    //todo : 클릭 상태 초기화
    private void isClickInit() {
        clickMap = new HashMap<>();
        clickMap.put("first", false);
        clickMap.put("second", false);
        clickMap.put("third", false);
        isClick = new MutableLiveData<>(clickMap);
    }

    //todo : 클릭 상태 실시간 관찰 기능
    private void isClickObserverInit() {
        clickObserver = stringBooleanMap -> {
            boolean f = Boolean.TRUE.equals(stringBooleanMap.get("first"));
            boolean s = Boolean.TRUE.equals(stringBooleanMap.get("second"));
            boolean t = Boolean.TRUE.equals(stringBooleanMap.get("third"));
            if (f && s && t) {
                Intent intent = new Intent(activity, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                activity.startActivity(intent);
            }
        };
        isClick.observe(activity, clickObserver);
    }




    //todo : 첫번째 동의버튼 클릭 기능
    public void checkAgreeFirst(LinearLayout layoutExpand1, ImageView arrowImageView1) {
        if (Boolean.TRUE.equals(clickMap.get("first"))) {
            clickMap.put("first", false);
        } else {
            clickMap.put("first", true);
        }
        isClick.setValue(clickMap);
        layoutExpand1Animation(layoutExpand1, arrowImageView1);
    }

    private void layoutExpand1Animation(LinearLayout layoutExpand1, ImageView arrowImageView1) {
        if (layoutExpand1.getVisibility() == View.VISIBLE) {
            layoutExpand1.setVisibility(View.GONE);
            arrowImageView1.animate().rotation(180f).setDuration(300).start();
        } else {
            layoutExpand1.setVisibility(View.VISIBLE);
            arrowImageView1.animate().rotation(0f).setDuration(300).start();
        }
    }


    //todo : 두번째 동의버튼 클릭 기능
    public void checkAgreeSecond(LinearLayout layoutExpand2, ImageView arrowImageView2) {
        if (Boolean.TRUE.equals(clickMap.get("second"))) {
            clickMap.put("second", false);
        } else {
            clickMap.put("second", true);
        }
        isClick.setValue(clickMap);
        layoutExpand2Animation(layoutExpand2, arrowImageView2);
    }

    private void layoutExpand2Animation(LinearLayout layoutExpand2, ImageView arrowImageView2) {
        if (layoutExpand2.getVisibility() == View.VISIBLE) {
            layoutExpand2.setVisibility(View.GONE);
            arrowImageView2.animate().rotation(180f).setDuration(300).start();
        } else {
            layoutExpand2.setVisibility(View.VISIBLE);
            arrowImageView2.animate().rotation(0f).setDuration(300).start();
        }
    }

    //todo : 세번째 동의버튼 클릭 기능
    public void checkAgreeThird(LinearLayout layoutExpand3, ImageView arrowImageView3) {
        if (Boolean.TRUE.equals(clickMap.get("third"))) {
            clickMap.put("third", false);
        } else {
            clickMap.put("third", true);
        }
        isClick.setValue(clickMap);
        layoutExpand3Animation(layoutExpand3, arrowImageView3);
    }

    private void layoutExpand3Animation(LinearLayout layoutExpand3, ImageView arrowImageView3) {
        if (layoutExpand3.getVisibility() == View.VISIBLE) {
            layoutExpand3.setVisibility(View.GONE);
            arrowImageView3.animate().rotation(180f).setDuration(300).start();
        } else {
            layoutExpand3.setVisibility(View.VISIBLE);
            arrowImageView3.animate().rotation(0f).setDuration(300).start();
        }
    }


}
