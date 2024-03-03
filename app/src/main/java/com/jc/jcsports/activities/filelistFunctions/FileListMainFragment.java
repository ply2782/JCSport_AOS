package com.jc.jcsports.activities.filelistFunctions;

import static com.jc.jcsports.utils.Utils.ARG_PARAM1;
import static com.jc.jcsports.utils.Utils.ARG_PARAM2;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;

import com.jc.jcsports.R;
import com.jc.jcsports.databinding.FragmentFileListMainBinding;
import com.jc.jcsports.utils.AbstractFragment;
import com.jc.jcsports.utils.Utils;

public class FileListMainFragment extends AbstractFragment {

    private String mParam1;
    private String mParam2;
    private FragmentFileListMainBinding binding;
    private FileListMainFunctions fileListMainFunctions;

    public FileListMainFragment() {
        // Required empty public constructor

    }


    public static FileListMainFragment newInstance(String param1, String param2) {
        FileListMainFragment fragment = new FileListMainFragment();
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
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_file_list_main, container, false);
        binding.setLifecycleOwner(getViewLifecycleOwner());
        binding.setThisFragment(this);
        basicSetting();
        return binding.getRoot();
    }

    @Override
    public void basicSetting() {
        fileListMainFunctions = new FileListMainFunctions(context,
                fragmentActivity,
                binding.viewPager2,
                binding.htabTabs);
    }

    public void moveFileCreateActivity(View v){
        fileListMainFunctions.moveFileCreateActivity();
    }



}