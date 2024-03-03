package com.jc.jcsports.activities.userListFunctions;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jc.jcsports.R;
import com.jc.jcsports.bundle.SignUpBundle;
import com.jc.jcsports.databinding.FragmentUserListBinding;
import com.jc.jcsports.model.LoginModel;
import com.jc.jcsports.utils.AbstractFragment;
import com.jc.jcsports.utils.RetrofitClient;
import com.jc.jcsports.utils.RetrofitService;

public class UserListFragment extends AbstractFragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    private FragmentUserListBinding binding;
    private RetrofitService service;
    private UserListFunctions u_Functions;
    private RetrofitClient retrofitClient;

    public UserListFragment() {
    }

    public static UserListFragment newInstance(String param1, String param2) {
        UserListFragment fragment = new UserListFragment();
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
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_user_list, container, false);
        basicSetting();
        return binding.getRoot();
    }

    @Override
    public void basicSetting() {
        retroFitInit();
        u_Functions = new UserListFunctions();
        u_Functions.setContext(context);
        u_Functions.setFragmentActivity(fragmentActivity);
        u_Functions.setService(service);
        u_Functions.setSwipeRefreshLayout(binding.swipeLayout);
        u_Functions.setRecyclerView(binding.userListRecyclerView);

    }

    //todo : RestApi init
    private void retroFitInit() {
        retrofitClient = RetrofitClient.getInstance();
        service = RetrofitClient.getServerInterface();

    }

    @Override
    public void onResume() {
        super.onResume();
    }
}