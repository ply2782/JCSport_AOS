package com.jc.jcsports.activities.filelistFunctions;

import static com.jc.jcsports.utils.Utils.ARG_PARAM1;
import static com.jc.jcsports.utils.Utils.ARG_PARAM2;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jc.jcsports.R;
import com.jc.jcsports.databinding.FragmentMyBulletinBinding;
import com.jc.jcsports.utils.AbstractFragment;
import com.jc.jcsports.utils.Utils;


public class MyBulletinFragment extends AbstractFragment {

    private FragmentMyBulletinBinding binding;
    private String mParam1;
    private String mParam2;
    private MyBulletinFunctions myBulletinFunctions;
    private BroadcastReceiver br, createBr;

    public MyBulletinFragment() {
        // Required empty public constructor
    }

    public static MyBulletinFragment newInstance(String param1, String param2) {
        MyBulletinFragment fragment = new MyBulletinFragment();
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
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_my_bulletin, container, false);
        basicSetting();
        return binding.getRoot();
    }

    @Override
    public void basicSetting() {
        myBulletinFunctions = new MyBulletinFunctions(fragmentActivity, context,
                binding.itemRecyclerView, binding.refreshLayout);
        brInit();
    }


    private void brInit() {
        br = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                //todo : refresh
                boolean params_1 = intent.getBooleanExtra("b_File", false);
                if (params_1) {
                    myBulletinFunctions.api_Modify();
                }
                boolean params_2 = intent.getBooleanExtra("b_Delete", false);
                if (params_2) {
                    myBulletinFunctions.api_getReFreshFullItems();
                }
            }
        };
        createBr = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                myBulletinFunctions.api_getReFreshFullItems();
            }
        };

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Utils.logCheck("MF_Fragment is onDestroy");
        if (br != null) {
            fragmentActivity.unregisterReceiver(br);
            fragmentActivity.unregisterReceiver(createBr);
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("b_Detail");
        fragmentActivity.registerReceiver(br, intentFilter);
        IntentFilter intentFilter_1 = new IntentFilter();
        intentFilter_1.addAction("b_Refresh");
        fragmentActivity.registerReceiver(createBr, intentFilter_1);
    }
}