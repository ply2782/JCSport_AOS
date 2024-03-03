package com.jc.jcsports.activities.filelistFunctions.fileModifyFunctions;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.net.Uri;
import android.view.View;

import com.jc.jcsports.R;
import com.jc.jcsports.bundle.SignUpBundle;
import com.jc.jcsports.databinding.ActivityFileModifyBinding;
import com.jc.jcsports.model.FileModel;
import com.jc.jcsports.model.LoginModel;
import com.jc.jcsports.utils.AbstractActivity;
import com.jc.jcsports.utils.ServerURL;
import com.jc.jcsports.utils.Utils;
import com.jc.jcsports.viewModel.FileContentsTextCountViewModel;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class FileModifyActivity extends AbstractActivity {

    private ActivityFileModifyBinding binding;
    public FileModifyFunctions f_Functions;
    public String b_Content;
    public int b_Number;
    public String countText;
    private List<FileModel> f_Model;
    private List<MultipartBody.Part> p_List;
    private FileContentsTextCountViewModel v_Model;
    private LoginModel loginModel;

    @Override
    public void basicSetting() {
        binding = DataBindingUtil.setContentView(FileModifyActivity.this, R.layout.activity_file_modify);
        binding.setLifecycleOwner(FileModifyActivity.this);
        binding.setThisActivity(this);
        v_Model = new ViewModelProvider(FileModifyActivity.this).get(FileContentsTextCountViewModel.class);
        v_Model.init();
        f_Functions = new FileModifyFunctions(context);
        fileData();
        binding.setCountText(v_Model);
        f_Functions.setF_Activity(this);
        f_Functions.setTabLayout(binding.intoTabLayout);
        f_Functions.setViewPager2(binding.fileViewPager2);
        f_Functions.setV_Model(v_Model);
        loginModelInit();
    }


    private void loginModelInit() {
        loginModel = (LoginModel) SignUpBundle.getBundle().getSerializable("loginModel");
    }

    public void completed(View v) {
        f_Functions.complete(binding.contentEditText.getText().toString(), loginModel.s_Number);

    }

    private void fileData() {
        p_List = new ArrayList<>();
        Intent intent = getIntent();
        f_Model = (List<FileModel>) intent.getSerializableExtra("f_Model");
        b_Content = intent.getStringExtra("b_Content");
        b_Number = intent.getIntExtra("b_Number", 0);
        countText = "( " + b_Content.length() + " / 500 )";
        for (FileModel f : f_Model) {
            if (f.f_isDelete) {
                File file = new File("");
                RequestBody requestFile = RequestBody.create(file, MediaType.parse("multipart/form-data"));
                String file_Name_Copy = "empty".replaceAll(" ", "").replaceAll("\\n", "");
                MultipartBody.Part p = MultipartBody.Part.createFormData("files", file_Name_Copy, requestFile);
                p_List.add(p);
            } else {
                if (f.f_Type.equals("video")) {
                    String v_Url = ServerURL.VIDEO_URL + f.f_Name;
                    f.f_Name = v_Url;
                    Uri uri = Uri.parse(v_Url);
                    MultipartBody.Part v_P = getVideoFile(uri, f.f_Name);
                    p_List.add(v_P);
                } else {
                    String i_Url = ServerURL.IMAGE_URL + f.f_Name;
                    f.f_Name = i_Url;
                    Uri uri = Uri.parse(i_Url);
                    MultipartBody.Part i_P = getImageFile(uri, f.f_Name);
                    p_List.add(i_P);
                }
            }

        }

        f_Functions.setP_List(p_List);
        f_Functions.setB_Number(b_Number);
        v_Model.setText(countText);
    }

    private MultipartBody.Part getImageFile(Uri uri, String file_Name) {
        File file = new File(uri.toString());
        RequestBody requestFile = RequestBody.create(file, MediaType.parse("multipart/form-data"));
        String file_Name_Copy = file_Name.replaceAll(" ", "").replaceAll("\\n", "");
        return MultipartBody.Part.createFormData("files", file_Name_Copy, requestFile);
    }

    private MultipartBody.Part getVideoFile(Uri uri, String file_Name) {
        File file = new File(uri.toString());
        RequestBody requestFile = RequestBody.create(file, MediaType.parse("multipart/form-data"));
        String file_Name_Copy = file_Name.replaceAll(" ", "").replaceAll("\\n", "");
        return MultipartBody.Part.createFormData("files", file_Name_Copy, requestFile);
    }
}