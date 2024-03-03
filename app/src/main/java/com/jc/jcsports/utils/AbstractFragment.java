package com.jc.jcsports.utils;

import android.app.Activity;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

public abstract class AbstractFragment extends Fragment {

    public FragmentActivity fragmentActivity;
    public Context context;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        fragmentActivity = (FragmentActivity) context;
        this.context = context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        fragmentActivity = null;
    }

    public abstract void basicSetting();

}
