package com.jc.jcsports.activities.storeFunctions;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jc.jcsports.R;
import com.jc.jcsports.activities.filelistFunctions.MyBulletinFunctions;
import com.jc.jcsports.databinding.FragmentStoreBinding;
import com.jc.jcsports.utils.AbstractFragment;

public class StoreFragment extends AbstractFragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private FragmentStoreBinding binding;
    private StoreFunctions functions;
    private String mParam1;
    private String mParam2;

    public StoreFragment() {
        // Required empty public constructor
    }


    public static StoreFragment newInstance(String param1, String param2) {
        StoreFragment fragment = new StoreFragment();
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
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_store, container, false);
        basicSetting();
        return binding.getRoot();
    }


    @Override
    public void basicSetting() {
        functions = new StoreFunctions();
        functions.setContext(context);

    }
}