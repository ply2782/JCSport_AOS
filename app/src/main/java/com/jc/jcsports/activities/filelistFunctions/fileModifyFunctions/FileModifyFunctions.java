package com.jc.jcsports.activities.filelistFunctions.fileModifyFunctions;

import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.app.Activity.RESULT_OK;
import static android.os.Build.VERSION.SDK_INT;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.FileUtils;
import android.provider.OpenableColumns;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.jc.jcsports.R;
import com.jc.jcsports.activities.adapter.MultipartBodyAdapter;
import com.jc.jcsports.activities.diffUtils.MultipartBodyDiffUtil;
import com.jc.jcsports.commonInterface.CommonInterface;
import com.jc.jcsports.utils.GlideApp;
import com.jc.jcsports.utils.LoadingDialog;
import com.jc.jcsports.utils.RetrofitClient;
import com.jc.jcsports.utils.RetrofitService;
import com.jc.jcsports.utils.ServerURL;
import com.jc.jcsports.utils.Utils;
import com.jc.jcsports.viewModel.FileContentsTextCountViewModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.SingleObserver;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.internal.Util;

public class FileModifyFunctions {
    private boolean isB_File = false;
    private RequestBody
            p_0_delete = RequestBody.create("init", MediaType.parse("text/plain")),
            p_1_delete = RequestBody.create("init", MediaType.parse("text/plain")),
            p_2_delete = RequestBody.create("init", MediaType.parse("text/plain")),
            p_3_delete = RequestBody.create("init", MediaType.parse("text/plain")),
            p_4_delete = RequestBody.create("init", MediaType.parse("text/plain"));

    private int b_Number;
    private List<MultipartBody.Part> userPhotoList;
    private Map<Integer, MultipartBody.Part> userPhotoList_Map;
    private Context context;
    private FileModifyActivity f_Activity;
    public TextWatcher editTextTextWatcher;
    private final int SCREEN_OFFSET_LIMIT = 5;
    private List<MultipartBody.Part> p_List;
    private ViewPager2 viewPager2;
    private TabLayout tabLayout;
    private MultipartBodyAdapter pagerAdapter;
    private AlertDialog alertDialog;
    private Button image_B, video_B;
    private CommonInterface.ClickInterface clickInterface;
    private ActivityResultLauncher<String> requestPermissionLauncher;
    private ActivityResultLauncher<Intent> externalPermissionLauncher;
    private Toast toast;
    private FileContentsTextCountViewModel v_Model;
    private ActivityResultLauncher<Intent>
            activityResultLauncher1,
            activityResultLauncher2, activityResultLauncher3,
            activityResultLauncher4, activityResultLauncher5;
    private View.OnClickListener onClickListener1_image = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_PICK);
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false);
            activityResultLauncher1.launch(intent);
            alertDialog.dismiss();
        }
    };

    private View.OnClickListener onClickListener1_video = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent();
            intent.setType("video/*");
            intent.setAction(Intent.ACTION_PICK);
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false);
            activityResultLauncher1.launch(intent);
            alertDialog.dismiss();
        }
    };

    private View.OnClickListener onClickListener2_image = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_PICK);
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false);
            activityResultLauncher2.launch(intent);
            alertDialog.dismiss();
        }
    };

    private View.OnClickListener onClickListener2_video = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent();
            intent.setType("video/*");
            intent.setAction(Intent.ACTION_PICK);
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false);
            activityResultLauncher2.launch(intent);
            alertDialog.dismiss();
        }
    };

    private View.OnClickListener onClickListener3_image = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_PICK);
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false);
            activityResultLauncher3.launch(intent);
            alertDialog.dismiss();
        }
    };

    private View.OnClickListener onClickListener3_video = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent();
            intent.setType("video/*");
            intent.setAction(Intent.ACTION_PICK);
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false);
            activityResultLauncher3.launch(intent);
            alertDialog.dismiss();
        }
    };


    private View.OnClickListener onClickListener4_image = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_PICK);
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false);
            activityResultLauncher4.launch(intent);
            alertDialog.dismiss();
        }
    };
    private View.OnClickListener onClickListener4_video = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent();
            intent.setType("video/*");
            intent.setAction(Intent.ACTION_PICK);
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false);
            activityResultLauncher4.launch(intent);
            alertDialog.dismiss();
        }
    };

    private View.OnClickListener onClickListener5_image = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_PICK);
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false);
            activityResultLauncher5.launch(intent);
            alertDialog.dismiss();
        }
    };

    private View.OnClickListener onClickListener5_video = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent();
            intent.setType("video/*");
            intent.setAction(Intent.ACTION_PICK);
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false);
            activityResultLauncher5.launch(intent);
            alertDialog.dismiss();
        }
    };
    private LoadingDialog loadingDialog;
    private RetrofitClient retrofitClient;
    private RetrofitService service;
    private MutableLiveData<Map<Integer, MultipartBody.Part>>
            userRealPhotoList_1,
            userRealPhotoList_2,
            userRealPhotoList_3,
            userRealPhotoList_4,
            userRealPhotoList_5;

    public FileModifyFunctions(Context context) {
        this.context = context;
        toastInit();
        editTextWatcherInit();
        retroFitInit();
        loadingDialogInit();
    }


    public void setP_List(List<MultipartBody.Part> p_List) {
        this.p_List = p_List;
    }

    public void setB_Number(int b_Number) {
        this.b_Number = b_Number;
    }

    public void setViewPager2(ViewPager2 viewPager2) {
        this.viewPager2 = viewPager2;
        viewPager2Init();

        connectViewPagerWithTabLayout();
        ObserverInit();
        customDialogInit();
    }

    public void setF_Activity(FileModifyActivity f_Activity) {
        this.f_Activity = f_Activity;
        requestPermissionInit();
        fileImageInit1();
        fileImageInit2();
        fileImageInit3();
        fileImageInit4();
        fileImageInit5();
    }

    public void setTabLayout(TabLayout tabLayout) {
        this.tabLayout = tabLayout;
    }

    public void setV_Model(FileContentsTextCountViewModel v_Model) {
        this.v_Model = v_Model;
    }

    //todo : RestApi init
    private void retroFitInit() {
        retrofitClient = RetrofitClient.getInstance();
        service = RetrofitClient.getServerInterface();

    }

    //todo : 로딩 다이얼로그
    private void loadingDialogInit() {
        loadingDialog = new LoadingDialog(context);
        loadingDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
    }


    private void ObserverInit() {
        Observer<Map<Integer, MultipartBody.Part>> userRealPhotoListObserver_1 = integerStringMap -> {
            //                fileModelUpdate1(integerStringMap);

            pagerAdapter.submitList(userPhotoList);
            pagerAdapter.notifyItemChanged(0);
        };

        Observer<Map<Integer, MultipartBody.Part>> userRealPhotoListObserver_2 = integerStringMap -> {
            //                fileModelUpdate2(integerStringMap);
            pagerAdapter.submitList(userPhotoList);
            pagerAdapter.notifyItemChanged(1);
        };

        Observer<Map<Integer, MultipartBody.Part>> userRealPhotoListObserver_3 = integerStringMap -> {
            //                fileModelUpdate3(integerStringMap);
            pagerAdapter.submitList(userPhotoList);
            pagerAdapter.notifyItemChanged(2);
        };

        Observer<Map<Integer, MultipartBody.Part>> userRealPhotoListObserver_4 = integerStringMap -> {
            //                fileModelUpdate4(integerStringMap);
            pagerAdapter.submitList(userPhotoList);
            pagerAdapter.notifyItemChanged(3);
        };

        Observer<Map<Integer, MultipartBody.Part>> userRealPhotoListObserver_5 = integerStringMap -> {
            //                fileModelUpdate5(integerStringMap);
            pagerAdapter.submitList(userPhotoList);
            pagerAdapter.notifyItemChanged(4);
        };


        userRealPhotoList_1.observe(f_Activity, userRealPhotoListObserver_1);
        userRealPhotoList_2.observe(f_Activity, userRealPhotoListObserver_2);
        userRealPhotoList_3.observe(f_Activity, userRealPhotoListObserver_3);
        userRealPhotoList_4.observe(f_Activity, userRealPhotoListObserver_4);
        userRealPhotoList_5.observe(f_Activity, userRealPhotoListObserver_5);
    }

    public void requestPermissionInit() {
        externalPermissionLauncher =
                f_Activity.registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                    Utils.logCheck("externalPermissionLauncher result => " + result);
                });
        requestPermissionLauncher =
                f_Activity.registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                    if (isGranted) {
                        // Permission is granted. Continue the action or workflow in your
                        // app.
                        Utils.logCheck("isGranted");
                    } else {
                        Utils.logCheck("is not Granted");
                        // Explain to the user that the feature is unavailable because the
                        // feature requires a permission that the user has denied. At the
                        // same time, respect the user's decision. Don't link to system
                        // settings in an effort to convince the user to change their
                        // decision.
                    }
                });
    }


    //todo : 사진 , 동영상 선택 다이얼로그 커스텀 초기화
    private void customDialogInit() {
        View dialogView = f_Activity.getLayoutInflater().inflate(R.layout.dialog_show_file_menu, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(dialogView);
        alertDialog = builder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        image_B = dialogView.findViewById(R.id.imageButton);
        video_B = dialogView.findViewById(R.id.videoButton);
        pagerAdapter.setAlertDialog(alertDialog);
    }


    //todo : 뷰페이저2 초기화
    private void viewPager2Init() {
        pagerAdapter = new MultipartBodyAdapter(new MultipartBodyDiffUtil(), context);
        clickInterfaceInit();
        viewPager2.setAdapter(pagerAdapter);
        viewPager2.setPageTransformer(new CompositePageTransformer());
        viewPager2.setOffscreenPageLimit(SCREEN_OFFSET_LIMIT);
        viewPager2Listener(viewPager2);
        fileModelInit();
    }


    //todo : 파일모델 초기화
    private void fileModelInit() {
        userPhotoList_Map = new HashMap<>();
        userPhotoList_Map.put(0, null);
        userPhotoList_Map.put(1, null);
        userPhotoList_Map.put(2, null);
        userPhotoList_Map.put(3, null);
        userPhotoList_Map.put(4, null);
        userRealPhotoList_1 = new MutableLiveData<>(userPhotoList_Map);
        userRealPhotoList_2 = new MutableLiveData<>(userPhotoList_Map);
        userRealPhotoList_3 = new MutableLiveData<>(userPhotoList_Map);
        userRealPhotoList_4 = new MutableLiveData<>(userPhotoList_Map);
        userRealPhotoList_5 = new MutableLiveData<>(userPhotoList_Map);
        List<MutableLiveData<Map<Integer, okhttp3.MultipartBody.Part>>> m_List = new ArrayList<>();
        m_List.add(userRealPhotoList_1);
        m_List.add(userRealPhotoList_2);
        m_List.add(userRealPhotoList_3);
        m_List.add(userRealPhotoList_4);
        m_List.add(userRealPhotoList_5);
        alreadyFileInit(m_List);

    }

    private void alreadyFileInit(List<MutableLiveData<Map<Integer, okhttp3.MultipartBody.Part>>> m_List) {
        int cnt = 0;
        for (MultipartBody.Part p : p_List) {
            userPhotoList_Map.put(cnt, p);
            m_List.get(cnt).setValue(userPhotoList_Map);
            cnt++;
        }
        Collection<MultipartBody.Part> userPhotoListValues = userPhotoList_Map.values();
        userPhotoList = new ArrayList<>(userPhotoListValues);
    }

    //todo : 뷰페이저2 리스너
    private void viewPager2Listener(ViewPager2 viewPager2) {
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
                if (positionOffsetPixels == 0) {

                }
            }

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
            }
        });
    }

    //todo : 문자 수 카운트 TextWatcher 세팅
    private void editTextWatcherInit() {
        editTextTextWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String countText = "( " + s.length() + " / 500 )";
                v_Model.setText(countText);
                if (s.length() >= 500) {
                    Toast.makeText(context, "글자수가 최대입니다.", Toast.LENGTH_SHORT).show();
                }
            }
        };
    }

    //todo : 탭 레이아웃과 뷰페이저2 연동
    private void connectViewPagerWithTabLayout() {
        new TabLayoutMediator(
                tabLayout,
                viewPager2,
                (tab, position) -> {

                }
        ).attach();
    }

    //todo : 파일 권한 확인 후 사진 및 영상 불러오기
    private void filePermission(Context context) {
        if (READ_MEDIA_IMAGES(context) && READ_MEDIA_VIDEO(context)) {
            showDialog(context);
        } else {
            toastMessage("앨범 파일 접근 허용을 해주시기 바랍니다.");
            // 요청 다이얼로그 띄우기

        }
    }

    //todo : 다이얼로그 보여주기
    private void showDialog(Context context) {
        alertDialog.show();
        dialogLayoutInfoUpdate(context);
    }

    //todo : 동적으로 커스텀 다이얼로그 width , height 변경
    private void dialogLayoutInfoUpdate(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display defaultDisplay = windowManager.getDefaultDisplay();
        Point point = new Point();
        defaultDisplay.getSize(point);
        int deviceWidth = point.x; // 디바이스 가로 길이
        WindowManager.LayoutParams params = new WindowManager.LayoutParams();
        params.copyFrom(alertDialog.getWindow().getAttributes());
        params.width = (int) (Math.round((deviceWidth * 0.7)));
        Window window = alertDialog.getWindow();
        window.setAttributes(params);
    }

    //todo : toast Init
    private void toastInit() {
        toast = new Toast(context);
    }

    //todo : 토스트 메시지 사용
    private void toastMessage(String text) {
        toast.setText(text);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.show();
    }

    //todo : 파일 접근 권한 읽기
    //todo : 파일 접근 권한 읽기
    private boolean READ_MEDIA_VIDEO(Context context) {
        if (ContextCompat.checkSelfPermission(
                context, Manifest.permission.READ_MEDIA_VIDEO) ==
                PackageManager.PERMISSION_GRANTED) {
            Utils.logCheck("READ_MEDIA_VIDEO");
            return true;
        } else {
            requestPermissionLauncher.launch(
                    Manifest.permission.READ_MEDIA_VIDEO);
            return false;
        }
    }


    //todo : 파일 접근 권한 읽기
    private boolean READ_MEDIA_IMAGES(Context context) {
        if (ContextCompat.checkSelfPermission(
                context, Manifest.permission.READ_MEDIA_IMAGES) ==
                PackageManager.PERMISSION_GRANTED) {
            // You can use the API that requires the permission.
            Utils.logCheck("READ_MEDIA_IMAGES");
            return true;
        } else {
            // You can directly ask for the permission.
            // The registered ActivityResultCallback gets the result of this request.
            requestPermissionLauncher.launch(
                    Manifest.permission.READ_MEDIA_IMAGES);
            return false;
        }
    }


    //todo : 클릭 인터페이스 초기화
    private void clickInterfaceInit() {
        clickInterface = new CommonInterface.ClickInterface() {
            @Override
            public void getPosition(int position) {
                if (SDK_INT >= Build.VERSION_CODES.R) {
                    permission_All_External__STORAGE();
                }
                filePermission(context);

                switch (position) {
                    case 1:
                        image_B.setOnClickListener(onClickListener2_image);
                        video_B.setOnClickListener(onClickListener2_video);
                        break;
                    case 2:
                        image_B.setOnClickListener(onClickListener3_image);
                        video_B.setOnClickListener(onClickListener3_video);
                        break;
                    case 3:
                        image_B.setOnClickListener(onClickListener4_image);
                        video_B.setOnClickListener(onClickListener4_video);
                        break;
                    case 4:
                        image_B.setOnClickListener(onClickListener5_image);
                        video_B.setOnClickListener(onClickListener5_video);
                        break;
                    default:
                        image_B.setOnClickListener(onClickListener1_image);
                        video_B.setOnClickListener(onClickListener1_video);
                        break;

                }
            }

            @Override
            public void getRemovePosition(int position) {
                isB_File = true;
                switch (position) {
                    case 1:
                        removeImageView_2();
                        break;
                    case 2:
                        removeImageView_3();
                        break;
                    case 3:
                        removeImageView_4();
                        break;
                    case 4:
                        removeImageView_5();
                        break;
                    default:
                        removeImageView_1();
                        break;

                }

            }
        };
        pagerAdapter.setClickInterface(clickInterface);
    }


    //todo : 사진 , 동영상 삭제하기
    public void removeImageView_1() {
        final int a = 0;
        userPhotoList.remove(a);
        userPhotoList_Map.put(a, null);
        userPhotoList.add(a, userPhotoList_Map.get(a));
        userRealPhotoList_1.setValue(userPhotoList_Map);
        p_0_delete = RequestBody.create("remove", MediaType.parse("text/plain"));
    }

    public void removeImageView_2() {
        final int a = 1;
        userPhotoList.remove(a);
        userPhotoList_Map.put(a, null);
        userPhotoList.add(a, userPhotoList_Map.get(a));
        userRealPhotoList_2.setValue(userPhotoList_Map);
        p_1_delete = RequestBody.create("remove", MediaType.parse("text/plain"));
    }

    public void removeImageView_3() {
        final int a = 2;
        userPhotoList.remove(a);
        userPhotoList_Map.put(a, null);
        userPhotoList.add(a, userPhotoList_Map.get(a));
        userRealPhotoList_3.setValue(userPhotoList_Map);
        p_2_delete = RequestBody.create("remove", MediaType.parse("text/plain"));
    }

    public void removeImageView_4() {
        final int a = 3;
        userPhotoList.remove(a);
        userPhotoList_Map.put(a, null);
        userPhotoList.add(a, userPhotoList_Map.get(a));
        userRealPhotoList_4.setValue(userPhotoList_Map);
        p_3_delete = RequestBody.create("remove", MediaType.parse("text/plain"));
    }

    public void removeImageView_5() {
        final int a = 4;
        userPhotoList.remove(a);
        userPhotoList_Map.put(a, null);
        userPhotoList.add(a, userPhotoList_Map.get(a));
        userRealPhotoList_5.setValue(userPhotoList_Map);
        p_4_delete = RequestBody.create("remove", MediaType.parse("text/plain"));
    }


    //todo : 파일 불러오기
    public void fileImageInit1() {
        activityResultLauncher1 = f_Activity.registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                Intent data = result.getData();
                //TODO : result 값이 성공할 경우
                if (result.getResultCode() == RESULT_OK) {
                    if (data == null) {
                        //TODO : 이미지 선택이 없을 경우
                        Snackbar.make(f_Activity.getCurrentFocus(), "현재 선택된 사진이 없습니다.", Snackbar.LENGTH_SHORT).show();
                        Log.e("abc", "data is null");
                    } else {
                        //TODO : 이미지 선택이 하나일 경우
                        Uri uri = result.getData().getData();
                        String imagePath = Utils.getRealPathFromURI(uri, context);
                        file_1(imagePath, uri);
                        userRealPhotoList_1.setValue(userPhotoList_Map);
                    }
                }
            }
        });
    }

    public void fileImageInit2() {
        activityResultLauncher2 = f_Activity.registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                Intent data = result.getData();
                //TODO : result 값이 성공할 경우
                if (result.getResultCode() == RESULT_OK) {
                    if (data == null) {
                        //TODO : 이미지 선택이 없을 경우
                        Snackbar.make(f_Activity.getCurrentFocus(), "현재 선택된 사진이 없습니다.", Snackbar.LENGTH_SHORT).show();
                        Log.e("abc", "data is null");
                    } else {
                        //TODO : 이미지 선택이 하나일 경우
                        Uri uri = result.getData().getData();
                        String imagePath = Utils.getRealPathFromURI(uri, context);
                        file_2(imagePath, uri);
                        userRealPhotoList_2.setValue(userPhotoList_Map);
                    }
                }
            }
        });
    }

    public void fileImageInit3() {
        activityResultLauncher3 = f_Activity.registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                Intent data = result.getData();
                //TODO : result 값이 성공할 경우
                if (result.getResultCode() == RESULT_OK) {
                    if (data == null) {
                        //TODO : 이미지 선택이 없을 경우
                        Snackbar.make(f_Activity.getCurrentFocus(), "현재 선택된 사진이 없습니다.", Snackbar.LENGTH_SHORT).show();
                        Log.e("abc", "data is null");
                    } else {
                        //TODO : 이미지 선택이 하나일 경우
                        Uri uri = result.getData().getData();
                        String imagePath = Utils.getRealPathFromURI(uri, context);
                        file_3(imagePath, uri);
                        userRealPhotoList_3.setValue(userPhotoList_Map);
                    }
                }
            }
        });
    }

    public void fileImageInit4() {
        activityResultLauncher4 = f_Activity.registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                Intent data = result.getData();
                //TODO : result 값이 성공할 경우
                if (result.getResultCode() == RESULT_OK) {
                    if (data == null) {
                        //TODO : 이미지 선택이 없을 경우
                        Snackbar.make(f_Activity.getCurrentFocus(), "현재 선택된 사진이 없습니다.", Snackbar.LENGTH_SHORT).show();
                        Log.e("abc", "data is null");
                    } else {
                        //TODO : 이미지 선택이 하나일 경우
                        Uri uri = result.getData().getData();
                        String imagePath = Utils.getRealPathFromURI(uri, context);
                        file_4(imagePath, uri);
                        userRealPhotoList_4.setValue(userPhotoList_Map);
                    }
                }
            }
        });
    }


    public void fileImageInit5() {
        activityResultLauncher5 = f_Activity.registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                Intent data = result.getData();
                //TODO : result 값이 성공할 경우
                if (result.getResultCode() == RESULT_OK) {
                    if (data == null) {
                        //TODO : 이미지 선택이 없을 경우
                        Snackbar.make(f_Activity.getCurrentFocus(), "현재 선택된 사진이 없습니다.", Snackbar.LENGTH_SHORT).show();
                        Log.e("abc", "data is null");
                    } else {
                        //TODO : 이미지 선택이 하나일 경우
                        Uri uri = result.getData().getData();
                        String imagePath = Utils.getRealPathFromURI(uri, context);
                        file_5(imagePath, uri);
                        userRealPhotoList_5.setValue(userPhotoList_Map);
                    }
                }
            }
        });
    }

    //todo : 여러개 이미지뷰에 이미지가 없는지 있는지 판단 하는 부분
    private void file_1(String imagePath, Uri uri) {
        try {
            final int a = 0;
            fileClickInit(a, uri, imagePath);
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }
    }

    private void file_2(String imagePath, Uri uri) {
        try {
            final int a = 1;
            fileClickInit(a, uri, imagePath);
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }
    }

    private void file_3(String imagePath, Uri uri) {
        try {
            final int a = 2;
            fileClickInit(a, uri, imagePath);
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }
    }

    private void file_4(String imagePath, Uri uri) {
        try {
            final int a = 3;
            fileClickInit(a, uri, imagePath);
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }
    }

    private void file_5(String imagePath, Uri uri) {
        try {
            final int a = 4;
            fileClickInit(a, uri, imagePath);
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }
    }

    private void fileClickInit(int a, Uri uri, String imagePath) throws JSONException, IOException {
        JSONObject jsonObject = new JSONObject();
        String file_name = fileDetailInfo(uri).getString("file_Name");
        long file_size = fileDetailInfo(uri).getLong("file_Size");
        String mime_Type = fileDetailInfo(uri).getString("mime_Type");

        jsonObject.put("mime_type", mime_Type);
        jsonObject.put("file_name", file_name);
        jsonObject.put("file_size", file_size);
        jsonObject.put("imagePath", imagePath);

        if (mime_Type.contains("video")) {
            MediaMetadataRetriever retriever = new MediaMetadataRetriever();
            retriever.setDataSource(context, Uri.fromFile(new File(imagePath)));
            String time = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
            long parseLong = Long.parseLong(time);
            retriever.release();
            jsonObject.put("file_duration", parseLong);
        }

        MultipartBody.Part file;
        if (mime_Type.contains("image")) {
            file = getImageFile(imagePath, file_name, a);
        } else {
            file = getVideoFile(imagePath, file_name, a);
        }


        if (userPhotoList_Map.get(a) != null) {
            if (SDK_INT >= Build.VERSION_CODES.N) {
                userPhotoList_Map.replace(a, file);
            } else {
                userPhotoList_Map.remove(a);
                userPhotoList_Map.put(a, file);
            }
        } else {
            userPhotoList_Map.put(a, file);
        }
        pagerAdapter.setImagePath(imagePath);
        userPhotoList.remove(a);
        userPhotoList.add(a, userPhotoList_Map.get(a));
    }

    //todo : 파일 사이즈 구하는 기능
    private JSONObject fileDetailInfo(Uri uri) throws JSONException {
        JSONObject jsonObject = new JSONObject();
        Cursor returnCursor =
                context.getContentResolver().query(uri, null, null, null, null);
        String mimeType = context.getContentResolver().getType(uri);
        int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
        int sizeIndex = returnCursor.getColumnIndex(OpenableColumns.SIZE);
        returnCursor.moveToFirst();
        String title = returnCursor.getString(nameIndex);
        long fileSize = returnCursor.getLong(sizeIndex);
        long bytes = fileSize;
        long kilobyte = bytes / 1024;
        long megabyte = kilobyte / 1024;
        jsonObject.put("file_Name", title);
        jsonObject.put("file_Size", kilobyte);
        jsonObject.put("mime_Type", mimeType);
        return jsonObject;
    }


    private MultipartBody.Part getImageFile(String filePath, String file_Name, int index) {
        File file = new File(filePath);
        RequestBody requestFile = RequestBody.create(file, MediaType.parse("multipart/form-data"));
        String file_Name_Copy = file_Name.replaceAll(" ", "").replaceAll("\\n", "");
        String file_Sep = "files_" + index;
        return MultipartBody.Part.createFormData(file_Sep, file_Name_Copy, requestFile);
    }

    private MultipartBody.Part getVideoFile(String filePath, String file_Name, int index) {
        File file = new File(filePath);
        RequestBody requestFile = RequestBody.create(file, MediaType.parse("multipart/form-data"));
        String file_Name_Copy = file_Name.replaceAll(" ", "").replaceAll("\\n", "");
        String file_Sep = "files_" + index;
        return MultipartBody.Part.createFormData(file_Sep, file_Name_Copy, requestFile);
    }


    //todo : 완성 api 통신
    public void complete(String b_Content, int s_Number) {
        if (!b_Content.trim().replaceAll(" ", "").equals("")) {
            complete_Content(b_Content, s_Number);
        } else {
            toastMessage("내용을 입력해주세요.");
        }
    }


    private void complete_Content(String b_Content, int s_Number) {
        RequestBody b_Content_R = RequestBody.create(b_Content, MediaType.parse("text/plain"));
        RequestBody s_Number_R = RequestBody.create(String.valueOf(s_Number), MediaType.parse("text/plain"));
        RequestBody b_Number_R = RequestBody.create(String.valueOf(b_Number), MediaType.parse("text/plain"));


        String i_Prefix = "showImage?";
        String v_Prefix = "showVideo?";
        String e_Prefix = "empty";

        MultipartBody.Part p_0 = userPhotoList.get(0);
        if (p_0 != null) {
            String url_0 = p_0.headers().value(0).split(";")[2].substring(("filename=".length() + 1));
            if (url_0.contains(i_Prefix) || url_0.contains(v_Prefix) || url_0.contains(e_Prefix)) {
                p_0 = null;
                p_0_delete = RequestBody.create("not Change", MediaType.parse("text/plain"));
            } else {
                p_0_delete = RequestBody.create("change", MediaType.parse("text/plain"));
                isB_File = true;
            }
        }


        MultipartBody.Part p_1 = userPhotoList.get(1);
        if (p_1 != null) {
            String url_1 = p_1.headers().value(0).split(";")[2].substring(("filename=".length() + 1));
            if (url_1.contains(i_Prefix) || url_1.contains(v_Prefix) || url_1.contains(e_Prefix)) {
                p_1 = null;
                p_1_delete = RequestBody.create("not Change", MediaType.parse("text/plain"));
            } else {
                p_1_delete = RequestBody.create("change", MediaType.parse("text/plain"));
                isB_File = true;
            }
        }


        MultipartBody.Part p_2 = userPhotoList.get(2);
        if (p_2 != null) {
            String url_2 = p_2.headers().value(0).split(";")[2].substring(("filename=".length() + 1));
            if (url_2.contains(i_Prefix) || url_2.contains(v_Prefix) || url_2.contains(e_Prefix)) {
                p_2 = null;
                p_2_delete = RequestBody.create("not Change", MediaType.parse("text/plain"));
            } else {
                p_2_delete = RequestBody.create("change", MediaType.parse("text/plain"));
                isB_File = true;
            }
        }


        MultipartBody.Part p_3 = userPhotoList.get(3);
        if (p_3 != null) {
            String url_3 = p_3.headers().value(0).split(";")[2].substring(("filename=".length() + 1));
            if (url_3.contains(i_Prefix) || url_3.contains(v_Prefix) || url_3.contains(e_Prefix)) {
                p_3 = null;
                p_3_delete = RequestBody.create("not Change", MediaType.parse("text/plain"));
            } else {
                p_3_delete = RequestBody.create("change", MediaType.parse("text/plain"));
                isB_File = true;
            }
        }


        MultipartBody.Part p_4 = userPhotoList.get(4);
        if (p_4 != null) {
            String url_4 = p_4.headers().value(0).split(";")[2].substring(("filename=".length() + 1));
            if (url_4.contains(i_Prefix) || url_4.contains(v_Prefix) || url_4.contains(e_Prefix)) {
                p_4 = null;
                p_4_delete = RequestBody.create("not Change", MediaType.parse("text/plain"));
            } else {
                p_4_delete = RequestBody.create("change", MediaType.parse("text/plain"));
                isB_File = true;
            }
        }

        service.updateBulletinBoard(
                        ServerURL.bulletinController,
                        b_Number_R,
                        s_Number_R, b_Content_R,
                        p_0,
                        p_0_delete,
                        p_1,
                        p_1_delete,
                        p_2,
                        p_2_delete,
                        p_3,
                        p_3_delete,
                        p_4,
                        p_4_delete
                )
                .subscribeOn(Schedulers.single())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Boolean>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        loadingDialog.show();
                        Log.d("debug", "Disposable");
                    }

                    @Override
                    public void onSuccess(@NonNull Boolean aBoolean) {
                        Log.d("debug", "boolean " + aBoolean);
                        if (aBoolean) {

                            Intent intent = new Intent();
                            intent.setAction("b_Detail");
                            intent.putExtra("b_Content", true);
                            if (isB_File) {
                                intent.putExtra("b_File", true);
                            }
                            f_Activity.sendBroadcast(intent);

                            showCompleteAlarm();
                            loadingDialog.dismiss();
                            f_Activity.finish();


                        } else {
                            loadingDialog.dismiss();
                            toastMessage("서버와의 통신이 원할하지 않습니다.");
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.d("debug", "error " + e);
                        loadingDialog.dismiss();
                        toastMessage("서버와의 통신이 원할하지 않습니다.");
                    }
                });

    }


    private void showCompleteAlarm() {
        String channelId = "fileModifyActivity_" + "" + context.getPackageName();
        // 알림 표시
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel((channelId), "JCSports", NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.setDescription("채널에 대한 설명.");
            notificationChannel.enableLights(true);
            notificationChannel.enableVibration(true);
            notificationChannel.setShowBadge(true);
            notificationChannel.setVibrationPattern(new long[]{1000, 1000});
            notificationManager.createNotificationChannel(notificationChannel);
        }
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, channelId);
        Bitmap logoBitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.jc_logo);
        builder.setLargeIcon(logoBitmap)
                .setContentTitle("JCSports")
                .setSubText("INFO")
                .setContentText("게시물이 변경되었습니다.")
                .setSmallIcon(R.mipmap.jc_logo)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .setVibrate(new long[]{1000, 1000})
                .setStyle(new NotificationCompat.BigTextStyle())
                .setAutoCancel(true);
        notificationManager.notify(ServerURL.bulletinNotification, builder.build());
    }


    @RequiresApi(api = Build.VERSION_CODES.R)
    private boolean permission_All_External__STORAGE() {
        if (Environment.isExternalStorageManager()) {
            return true;
        } else {
            try {
                Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                intent.addCategory("android.intent.category.DEFAULT");
                intent.setData(Uri.parse(String.format("package:%s", context.getPackageName())));
                externalPermissionLauncher.launch(intent);
                return true;
            } catch (Exception e) {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                externalPermissionLauncher.launch(intent);
                return false;
            }
        }

    }
}
