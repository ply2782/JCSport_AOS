package com.jc.jcsports.bundle;

import android.os.Bundle;

import com.jc.jcsports.utils.Utils;

public class SignUpBundle {
    private static SignUpBundle singleton;

    private static Bundle bundle;

    public static Bundle getBundle() {
        return bundle;
    }

    private SignUpBundle() {
        Utils.logCheck("singleton instance 생성");
        bundle = new Bundle();
    }

    private static class LazyHolder {
        private static final SignUpBundle instance = new SignUpBundle();
    }

    // 3. 객체가 heap에 있는지 확인
    // 있으면 주소값 리턴, 없으면Ï 새로운 객체 생성
    public static SignUpBundle getInstance() {
        if (singleton == null) {
            Utils.logCheck("SignUpBundle is New");
            return LazyHolder.instance;
        } else {
            Utils.logCheck("SignUpBundle is exists");
        }
        return singleton;
    }

    public static SignUpBundle empty() {
        if (singleton == null) {
            Utils.logCheck("SignUpBundle is already empty");
        } else {
            singleton = null;
            Utils.logCheck("SignUpBundle is now empty");
        }
        return singleton;
    }
}
