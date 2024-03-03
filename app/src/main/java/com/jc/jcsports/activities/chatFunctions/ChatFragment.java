package com.jc.jcsports.activities.chatFunctions;

import static com.jc.jcsports.utils.Utils.ARG_PARAM1;
import static com.jc.jcsports.utils.Utils.ARG_PARAM2;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jc.jcsports.R;
import com.jc.jcsports.databinding.FragmentChatBinding;
import com.jc.jcsports.utils.AbstractFragment;

public class ChatFragment extends AbstractFragment {

    private String mParam1;
    private String mParam2;
    private FragmentChatBinding binding;
    private ChatFunctions c_Functions;

    public ChatFragment() {

    }

    public static ChatFragment newInstance(String param1, String param2) {
        ChatFragment fragment = new ChatFragment();
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
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_chat, container, false);
        basicSetting();
        return binding.getRoot();
    }


    @Override
    public void basicSetting() {
        c_Functions = new ChatFunctions();
        c_Functions.setContext(context);
        c_Functions.setF_Activity(fragmentActivity);
        c_Functions.setSwipeRefreshLayout(binding.refreshLayout);
        c_Functions.setRecyclerView(binding.itemRecyclerView);
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}