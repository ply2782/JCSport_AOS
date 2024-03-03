package com.jc.jcsports.activities.userFunctions;

import static com.jc.jcsports.utils.Utils.ARG_PARAM1;
import static com.jc.jcsports.utils.Utils.ARG_PARAM2;

import android.content.IntentFilter;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jc.jcsports.R;
import com.jc.jcsports.bundle.SignUpBundle;
import com.jc.jcsports.databinding.FragmentUserBinding;
import com.jc.jcsports.model.LoginModel;
import com.jc.jcsports.utils.AbstractFragment;


public class UserFragment extends AbstractFragment {


    // TODO: Rename and change types of parameters
    private FragmentUserBinding binding;
    private String mParam1;
    private String mParam2;
    private UserFunctions u_Functions;
    private LoginModel loginModel;

    public UserFragment() {
        // Required empty public constructor
    }

    public static UserFragment newInstance(String param1, String param2) {
        UserFragment fragment = new UserFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_user, container, false);
        basicSetting();
        return binding.getRoot();
    }

    @Override
    public void basicSetting() {
        u_Functions = new UserFunctions();
        u_Functions.setContext(context);
        u_Functions.setF_Activity(fragmentActivity);
        myModelInit();
    }

    private void myModelInit() {
        loginModel = (LoginModel) SignUpBundle.getBundle().getSerializable("loginModel");
        binding.setMyModel(loginModel);
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}