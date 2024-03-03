package com.jc.jcsports.activities.filelistFunctions.fileCreateFunctions;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.FileUtils;
import android.provider.MediaStore;
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
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.jc.jcsports.R;
import com.jc.jcsports.activities.adapter.ViewPager2FileModelAdapter;
import com.jc.jcsports.activities.diffUtils.FileModelDiffUtil;
import com.jc.jcsports.commonInterface.CommonInterface;
import com.jc.jcsports.model.FileModel;
import com.jc.jcsports.utils.LoadingDialog;
import com.jc.jcsports.utils.RetrofitClient;
import com.jc.jcsports.utils.RetrofitService;
import com.jc.jcsports.utils.ServerURL;
import com.jc.jcsports.utils.Utils;
import com.jc.jcsports.utils.videotrim.VideoEditActivity;
import com.jc.jcsports.viewModel.FileContentsTextCountViewModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.core.SingleObserver;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;


public class FileCreateFunctions {
    public Toast toast;
    public RetrofitClient retrofitClient;
    public RetrofitService service;
    public FileContentsTextCountViewModel fileContentsTextCountViewModel;

    public Context context;
    public FileCreateActivity fileCreateActivity;
    public ViewPager2 viewPager2;
    public TabLayout tabLayout;
    public TextWatcher editTextTextWatcher;
    public ViewPager2FileModelAdapter viewPager2FileModelAdapter;
    public final int createFileCount = 4;
    public AlertDialog alertDialog;

    public ActivityResultLauncher<Intent>
            activityResultLauncher1, activityResultLauncher1_VideoEdit,
            activityResultLauncher2, activityResultLauncher2_VideoEdit,
            activityResultLauncher3, activityResultLauncher3_VideoEdit,
            activityResultLauncher4, activityResultLauncher4_VideoEdit,
            activityResultLauncher5, activityResultLauncher5_VideoEdit;
    public Map<Integer, MultipartBody.Part> userPhotoList, thumbNailPhotoList;
    public ActivityResultLauncher<String> requestPermissionLauncher;
    public Map<Integer, String> pictureMap;
    public MutableLiveData<Map<Integer, String>> userRealPhotoList;
    private final int RESULT_OK = -1;
    public List<FileModel> fileModelList;
    public CommonInterface.ClickInterface clickInterface;
    public Button image_B, video_B;
    public View.OnClickListener onClickListener1_image = new View.OnClickListener() {
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

    public View.OnClickListener onClickListener1_video = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent();
            intent.setType("video/*");
            intent.setAction(Intent.ACTION_PICK);
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false);
            activityResultLauncher1_VideoEdit.launch(intent);
            alertDialog.dismiss();
        }
    };

    public View.OnClickListener onClickListener2_image = new View.OnClickListener() {
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

    public View.OnClickListener onClickListener2_video = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent();
            intent.setType("video/*");
            intent.setAction(Intent.ACTION_PICK);
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false);
            activityResultLauncher2_VideoEdit.launch(intent);
            alertDialog.dismiss();
        }
    };

    public View.OnClickListener onClickListener3_image = new View.OnClickListener() {
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

    public View.OnClickListener onClickListener3_video = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent();
            intent.setType("video/*");
            intent.setAction(Intent.ACTION_PICK);
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false);
            activityResultLauncher3_VideoEdit.launch(intent);
            alertDialog.dismiss();
        }
    };


    public View.OnClickListener onClickListener4_image = new View.OnClickListener() {
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
    public View.OnClickListener onClickListener4_video = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent();
            intent.setType("video/*");
            intent.setAction(Intent.ACTION_PICK);
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false);
            activityResultLauncher4_VideoEdit.launch(intent);
            alertDialog.dismiss();
        }
    };

    public View.OnClickListener onClickListener5_image = new View.OnClickListener() {
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

    public View.OnClickListener onClickListener5_video = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent();
            intent.setType("video/*");
            intent.setAction(Intent.ACTION_PICK);
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false);
            activityResultLauncher5_VideoEdit.launch(intent);
            alertDialog.dismiss();
        }
    };
    private LoadingDialog loadingDialog;


    public FileCreateFunctions(Context context,
                               FileContentsTextCountViewModel fileContentsTextCountViewModel,
                               ViewPager2 viewPager2,
                               TabLayout tabLayout,
                               FileCreateActivity fileCreateActivity
    ) {
        this.context = context;
        this.viewPager2 = viewPager2;
        this.tabLayout = tabLayout;
        this.fileCreateActivity = fileCreateActivity;
        this.fileContentsTextCountViewModel = fileContentsTextCountViewModel;


        toastInit();
        editTextWatcherInit();
        adapterInit();
        viewPager2Init();
        connectViewPagerWithTabLayout();
        fileImageInit1();
        fileImageInit2();
        fileImageInit3();
        fileImageInit4();
        fileImageInit5();
        itemInit();
        customDialogInit();
        clickInterfaceInit();
        loadingDialogInit();
        retroFitInit();
        requestPermissionInit();
    }

    public void requestPermissionInit() {
        requestPermissionLauncher =
                fileCreateActivity.registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                    if (isGranted) {
                        Utils.logCheck("isGranted");
                    } else {
                        Utils.logCheck("is not Granted");

                    }
                });
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


    //todo : 완성 api 통신
    public void complete(String b_Content, int s_Number) {
        complete_Content(b_Content, s_Number);
    }


    private void complete_Content(String b_Content, int s_Number) {
        RequestBody b_Content_R = RequestBody.create(b_Content, MediaType.parse("text/plain"));
        RequestBody s_Number_R = RequestBody.create(String.valueOf(s_Number), MediaType.parse("text/plain"));
        Collection<MultipartBody.Part> userPhotoListValues = userPhotoList.values();
        Collection<MultipartBody.Part> thumbNailPhotoListValues = thumbNailPhotoList.values();
        List<MultipartBody.Part> userPhotoList = new ArrayList<>(userPhotoListValues);
        List<MultipartBody.Part> thumbNailPhotoList = new ArrayList<>(thumbNailPhotoListValues);


        service.createThumbNailBulletinResult(
                        ServerURL.bulletinController,
                        s_Number_R, b_Content_R, thumbNailPhotoList
                ).subscribeOn(Schedulers.single())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        loadingDialog.show();
                        Log.d("debug", "Disposable");
                    }

                    @Override
                    public void onSuccess(@NonNull Boolean aBoolean) {
                        Log.d("debug", "boolean " + aBoolean);
                        if (aBoolean) {
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

        service.createBulletinResult(
                        ServerURL.bulletinController,
                        s_Number_R, b_Content_R, userPhotoList)
                .subscribeOn(Schedulers.single())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<>() {
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
                            intent.setAction("b_Refresh");
                            fileCreateActivity.sendBroadcast(intent);

                            Intent intent_1 = new Intent();
                            intent_1.setAction("b_MyRefresh");
                            fileCreateActivity.sendBroadcast(intent_1);

                            showCompleteAlarm();
                            loadingDialog.dismiss();
                            fileCreateActivity.finish();


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


    //todo : 클릭 인터페이스 초기화
    private void clickInterfaceInit() {
        clickInterface = new CommonInterface.ClickInterface() {
            @Override
            public void getPosition(int position) {
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
        viewPager2FileModelAdapter.setClickInterface(clickInterface);
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

    //todo : 파일 권한 확인 후 사진 및 영상 불러오기
    private void filePermission(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (READ_MEDIA_IMAGES(context) && READ_MEDIA_VIDEO(context) ) {
                showDialog(context);
            } else {
                toastMessage("앨범 파일 접근 허용을 해주시기 바랍니다.");
            }
        } else {
            if (READ_EXTERNAL_STORAGE(context) && WRITE_EXTERNAL_STORAGE(context) ) {
                showDialog(context);
            } else {
                toastMessage("앨범 파일 접근 허용을 해주시기 바랍니다.");
            }
        }
    }



    //todo : 파일 접근 권한 읽기
    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
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

    private boolean WRITE_EXTERNAL_STORAGE(Context context) {
        if (ContextCompat.checkSelfPermission(
                context, Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                PackageManager.PERMISSION_GRANTED) {
            Utils.logCheck("READ_MEDIA_VIDEO");
            return true;
        } else {
            requestPermissionLauncher.launch(
                    Manifest.permission.WRITE_EXTERNAL_STORAGE);
            return false;
        }
    }

    private boolean READ_EXTERNAL_STORAGE(Context context) {
        if (ContextCompat.checkSelfPermission(
                context, Manifest.permission.READ_EXTERNAL_STORAGE) ==
                PackageManager.PERMISSION_GRANTED) {
            Utils.logCheck("READ_MEDIA_VIDEO");
            return true;
        } else {
            requestPermissionLauncher.launch(
                    Manifest.permission.READ_EXTERNAL_STORAGE);
            return false;
        }
    }


    //todo : 파일 접근 권한 읽기
    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
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


    //todo : 파일 불러오기
    public void fileImageInit1() {
        activityResultLauncher1 = fileCreateActivity.registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                Intent data = result.getData();
                //TODO : result 값이 성공할 경우
                if (result.getResultCode() == RESULT_OK) {
                    if (data == null) {
                        //TODO : 이미지 선택이 없을 경우
                        Snackbar.make(fileCreateActivity.getCurrentFocus(), "현재 선택된 사진이 없습니다.", Snackbar.LENGTH_SHORT).show();
                        Log.e("abc", "data is null");
                    } else {
                        //TODO : 이미지 선택이 하나일 경우
                        Uri uri = result.getData().getData();
                        String imagePath = Utils.getRealPathFromURI(uri, context);
                        file_1(imagePath, uri);
                        /** */
                        try {
                            File newFile_ThumbNail = new File(imagePath);
                            Uri uri_ThumbNail = FileProvider.getUriForFile(context, context.getPackageName() + ".provider", newFile_ThumbNail);
                            String file_name = fileDetailInfo(uri_ThumbNail).getString("file_Name");
                            MultipartBody.Part file = getImageFile(imagePath, file_name);
                            thumbNailPhotoList.remove(0);
                            thumbNailPhotoList.put(0, file);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        /***/
                        userRealPhotoList.setValue(pictureMap);

                    }
                }
            }
        });

        activityResultLauncher1_VideoEdit = fileCreateActivity.registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                Intent data = result.getData();
                //TODO : result 값이 성공할 경우
                if (result.getResultCode() == RESULT_OK) {
                    if (data == null) {
                        //TODO : 이미지 선택이 없을 경우
                        Snackbar.make(fileCreateActivity.getCurrentFocus(), "현재 선택된 사진이 없습니다.", Snackbar.LENGTH_SHORT).show();
                        Log.e("abc", "data is null");
                    } else {
                        //TODO : 이미지 선택이 하나일 경우
                        String trimmedVideoURi = data.getStringExtra("editedFilePath");
                        String trimmedVideoThumbNailURi = data.getStringExtra("thumbNailFilePath");
                        if (trimmedVideoURi != null) {
                            File newFile = new File(trimmedVideoURi);
                            Uri uri = FileProvider.getUriForFile(context, context.getPackageName() + ".provider", newFile);
                            file_1(trimmedVideoURi, uri);

                            /** */
                            try {
                                File newFile_ThumbNail = new File(trimmedVideoThumbNailURi);
                                Uri uri_ThumbNail = FileProvider.getUriForFile(context, context.getPackageName() + ".provider", newFile_ThumbNail);
                                String file_name = fileDetailInfo(uri_ThumbNail).getString("file_Name");
                                MultipartBody.Part file = getImageFile(trimmedVideoThumbNailURi, file_name);
                                thumbNailPhotoList.remove(0);
                                thumbNailPhotoList.put(0, file);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            /***/
                            userRealPhotoList.setValue(pictureMap);
                        } else {
                            Uri uri = result.getData().getData();
                            String imagePath = Utils.getRealPathFromURI(uri, context);
                            Intent intent = new Intent(context, VideoEditActivity.class);
                            intent.putExtra("filePath", imagePath);
                            activityResultLauncher1_VideoEdit.launch(intent);
                        }
                    }
                }
            }
        });
    }


    public void fileImageInit2() {
        activityResultLauncher2 = fileCreateActivity.registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                Intent data = result.getData();
                //TODO : result 값이 성공할 경우
                if (result.getResultCode() == RESULT_OK) {
                    if (data == null) {
                        //TODO : 이미지 선택이 없을 경우
                        Snackbar.make(fileCreateActivity.getCurrentFocus(), "현재 선택된 사진이 없습니다.", Snackbar.LENGTH_SHORT).show();
                        Log.e("abc", "data is null");
                    } else {
                        //TODO : 이미지 선택이 하나일 경우
                        Uri uri = result.getData().getData();
                        String imagePath = Utils.getRealPathFromURI(uri, context);
                        file_2(imagePath, uri);
                        userRealPhotoList.setValue(pictureMap);
                    }
                }
            }
        });
        activityResultLauncher2_VideoEdit = fileCreateActivity.registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                Intent data = result.getData();
                //TODO : result 값이 성공할 경우
                if (result.getResultCode() == RESULT_OK) {
                    if (data == null) {
                        //TODO : 이미지 선택이 없을 경우
                        Snackbar.make(fileCreateActivity.getCurrentFocus(), "현재 선택된 사진이 없습니다.", Snackbar.LENGTH_SHORT).show();
                        Log.e("abc", "data is null");
                    } else {
                        //TODO : 이미지 선택이 하나일 경우
                        String trimmedVideoURi = data.getStringExtra("filePath");
                        if (trimmedVideoURi != null) {
                            File newFile = new File(trimmedVideoURi);
                            Uri uri = FileProvider.getUriForFile(context, context.getPackageName() + ".provider", newFile);
                            file_2(trimmedVideoURi, uri);
                            userRealPhotoList.setValue(pictureMap);
                        } else {
                            Uri uri = result.getData().getData();
                            String imagePath = Utils.getRealPathFromURI(uri, context);
                            Intent intent = new Intent(context, VideoEditActivity.class);
                            intent.putExtra("filePath", imagePath);
                            activityResultLauncher2_VideoEdit.launch(intent);
                        }
                    }
                }
            }
        });
    }

    public void fileImageInit3() {
        activityResultLauncher3 = fileCreateActivity.registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                Intent data = result.getData();
                //TODO : result 값이 성공할 경우
                if (result.getResultCode() == RESULT_OK) {
                    if (data == null) {
                        //TODO : 이미지 선택이 없을 경우
                        Snackbar.make(fileCreateActivity.getCurrentFocus(), "현재 선택된 사진이 없습니다.", Snackbar.LENGTH_SHORT).show();
                        Log.e("abc", "data is null");
                    } else {
                        //TODO : 이미지 선택이 하나일 경우
                        Uri uri = result.getData().getData();
                        String imagePath = Utils.getRealPathFromURI(uri, context);
                        file_3(imagePath, uri);
                        userRealPhotoList.setValue(pictureMap);
                    }
                }
            }
        });
        activityResultLauncher3_VideoEdit = fileCreateActivity.registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                Intent data = result.getData();
                //TODO : result 값이 성공할 경우
                if (result.getResultCode() == RESULT_OK) {
                    if (data == null) {
                        //TODO : 이미지 선택이 없을 경우
                        Snackbar.make(fileCreateActivity.getCurrentFocus(), "현재 선택된 사진이 없습니다.", Snackbar.LENGTH_SHORT).show();
                        Log.e("abc", "data is null");
                    } else {
                        //TODO : 이미지 선택이 하나일 경우
                        String trimmedVideoURi = data.getStringExtra("filePath");
                        if (trimmedVideoURi != null) {
                            File newFile = new File(trimmedVideoURi);
                            Uri uri = FileProvider.getUriForFile(context, context.getPackageName() + ".provider", newFile);
                            file_3(trimmedVideoURi, uri);
                            userRealPhotoList.setValue(pictureMap);
                        } else {
                            Uri uri = result.getData().getData();
                            String imagePath = Utils.getRealPathFromURI(uri, context);
                            Intent intent = new Intent(context, VideoEditActivity.class);
                            intent.putExtra("filePath", imagePath);
                            activityResultLauncher3_VideoEdit.launch(intent);
                        }
                    }
                }
            }
        });
    }

    public void fileImageInit4() {
        activityResultLauncher4 = fileCreateActivity.registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                Intent data = result.getData();
                //TODO : result 값이 성공할 경우
                if (result.getResultCode() == RESULT_OK) {
                    if (data == null) {
                        //TODO : 이미지 선택이 없을 경우
                        Snackbar.make(fileCreateActivity.getCurrentFocus(), "현재 선택된 사진이 없습니다.", Snackbar.LENGTH_SHORT).show();
                        Log.e("abc", "data is null");
                    } else {
                        //TODO : 이미지 선택이 하나일 경우
                        Uri uri = result.getData().getData();
                        String imagePath = Utils.getRealPathFromURI(uri, context);
                        file_4(imagePath, uri);
                        userRealPhotoList.setValue(pictureMap);
                    }
                }
            }
        });
        activityResultLauncher4_VideoEdit = fileCreateActivity.registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                Intent data = result.getData();
                //TODO : result 값이 성공할 경우
                if (result.getResultCode() == RESULT_OK) {
                    if (data == null) {
                        //TODO : 이미지 선택이 없을 경우
                        Snackbar.make(fileCreateActivity.getCurrentFocus(), "현재 선택된 사진이 없습니다.", Snackbar.LENGTH_SHORT).show();
                        Log.e("abc", "data is null");
                    } else {
                        //TODO : 이미지 선택이 하나일 경우
                        String trimmedVideoURi = data.getStringExtra("filePath");
                        if (trimmedVideoURi != null) {
                            File newFile = new File(trimmedVideoURi);
                            Uri uri = FileProvider.getUriForFile(context, context.getPackageName() + ".provider", newFile);
                            file_4(trimmedVideoURi, uri);
                            userRealPhotoList.setValue(pictureMap);
                        } else {
                            Uri uri = result.getData().getData();
                            String imagePath = Utils.getRealPathFromURI(uri, context);
                            Intent intent = new Intent(context, VideoEditActivity.class);
                            intent.putExtra("filePath", imagePath);
                            activityResultLauncher4_VideoEdit.launch(intent);
                        }
                    }
                }
            }
        });
    }


    public void fileImageInit5() {
        activityResultLauncher5 = fileCreateActivity.registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                Intent data = result.getData();
                //TODO : result 값이 성공할 경우
                if (result.getResultCode() == RESULT_OK) {
                    if (data == null) {
                        //TODO : 이미지 선택이 없을 경우
                        Snackbar.make(fileCreateActivity.getCurrentFocus(), "현재 선택된 사진이 없습니다.", Snackbar.LENGTH_SHORT).show();
                        Log.e("abc", "data is null");
                    } else {
                        //TODO : 이미지 선택이 하나일 경우
                        Uri uri = result.getData().getData();
                        String imagePath = Utils.getRealPathFromURI(uri, context);
                        file_5(imagePath, uri);
                        userRealPhotoList.setValue(pictureMap);
                    }
                }
            }
        });
        activityResultLauncher5_VideoEdit = fileCreateActivity.registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                Intent data = result.getData();
                //TODO : result 값이 성공할 경우
                if (result.getResultCode() == RESULT_OK) {
                    if (data == null) {
                        //TODO : 이미지 선택이 없을 경우
                        Snackbar.make(fileCreateActivity.getCurrentFocus(), "현재 선택된 사진이 없습니다.", Snackbar.LENGTH_SHORT).show();
                        Log.e("abc", "data is null");
                    } else {
                        //TODO : 이미지 선택이 하나일 경우
                        String trimmedVideoURi = data.getStringExtra("filePath");
                        if (trimmedVideoURi != null) {
                            File newFile = new File(trimmedVideoURi);
                            Uri uri = FileProvider.getUriForFile(context, context.getPackageName() + ".provider", newFile);
                            file_5(trimmedVideoURi, uri);
                            userRealPhotoList.setValue(pictureMap);
                        } else {
                            Uri uri = result.getData().getData();
                            String imagePath = Utils.getRealPathFromURI(uri, context);
                            Intent intent = new Intent(context, VideoEditActivity.class);
                            intent.putExtra("filePath", imagePath);
                            activityResultLauncher5_VideoEdit.launch(intent);
                        }
                    }
                }
            }
        });
    }

    //todo : 여러개 이미지뷰에 이미지가 없는지 있는지 판단 하는 부분
    private void file_1(String filePath, Uri uri) {
        try {
            final int a = 0;
            fileClickInit(a, uri, filePath);
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }
    }

    private void file_2(String filePath, Uri uri) {
        try {
            final int a = 1;
            fileClickInit(a, uri, filePath);
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }
    }

    private void file_3(String filePath, Uri uri) {
        try {
            final int a = 2;
            fileClickInit(a, uri, filePath);
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }
    }

    private void file_4(String filePath, Uri uri) {
        try {
            final int a = 3;
            fileClickInit(a, uri, filePath);
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }
    }

    private void file_5(String filePath, Uri uri) {
        try {
            final int a = 4;
            fileClickInit(a, uri, filePath);
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }
    }

    private void fileClickInit(int a, Uri uri, String filePath) throws JSONException, IOException {
        JSONObject jsonObject = new JSONObject();
        String file_name = fileDetailInfo(uri).getString("file_Name");
        long file_size = fileDetailInfo(uri).getLong("file_Size");
        String mime_Type = fileDetailInfo(uri).getString("mime_Type");

        jsonObject.put("mime_type", mime_Type);
        jsonObject.put("file_name", file_name);
        jsonObject.put("file_size", file_size);
        jsonObject.put("filePath", filePath);


        if (mime_Type.contains("video")) {
            MediaMetadataRetriever retriever = new MediaMetadataRetriever();
            retriever.setDataSource(context, Uri.fromFile(new File(filePath)));
            String time = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
            long parseLong = Long.parseLong(time);
            retriever.release();
            jsonObject.put("file_duration", parseLong);

        }

        MultipartBody.Part file;
        if (mime_Type.contains("image")) {
            file = getImageFile(filePath, file_name);
        } else {
            file = getVideoFile(filePath, file_name);
        }


        if (pictureMap.get(a) != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                pictureMap.replace(a, String.valueOf(jsonObject));
                userPhotoList.replace(a, file);

            } else {
                pictureMap.remove(a);
                pictureMap.put(a, String.valueOf(jsonObject));
                userPhotoList.remove(a);
                userPhotoList.put(a, file);

            }
        } else {
            pictureMap.put(a, String.valueOf(jsonObject));
            userPhotoList.put(a, file);

        }
    }

    //todo : 변수명 초기화 세팅
    private void itemInit() {
        userPhotoList = new HashMap<>();
        userPhotoList.put(0, null);
        userPhotoList.put(1, null);
        userPhotoList.put(2, null);
        userPhotoList.put(3, null);
        userPhotoList.put(4, null);
        thumbNailPhotoList = new HashMap<>();
        thumbNailPhotoList.put(0, null);
        thumbNailPhotoList.put(1, null);
        thumbNailPhotoList.put(2, null);
        thumbNailPhotoList.put(3, null);
        thumbNailPhotoList.put(4, null);
        pictureMap = new HashMap<>();
        pictureMap.put(0, null);
        pictureMap.put(1, null);
        pictureMap.put(2, null);
        pictureMap.put(3, null);
        pictureMap.put(4, null);
        userRealPhotoList = new MutableLiveData<>(pictureMap);
        Observer<Map<Integer, String>> userRealPhotoListObserver = integerStringMap -> {
            try {
                fileModelUpdate1(integerStringMap);
                fileModelUpdate2(integerStringMap);
                fileModelUpdate3(integerStringMap);
                fileModelUpdate4(integerStringMap);
                fileModelUpdate5(integerStringMap);
                viewPager2FileModelAdapter.submitList(fileModelList);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        };
        userRealPhotoList.observe(fileCreateActivity, userRealPhotoListObserver);
    }


    //todo : 파일 모델 업데이트
    private void fileModelUpdate1(Map<Integer, String> itemMap) throws JSONException {
        final int a = 0;
        String file_Info = itemMap.get(a);
        fileInsertInit(file_Info, a);
        viewPager2FileModelAdapter.notifyItemChanged(a);
    }

    private void fileModelUpdate2(Map<Integer, String> itemMap) throws JSONException {
        final int a = 1;
        String file_Info = itemMap.get(a);
        fileInsertInit(file_Info, a);
        viewPager2FileModelAdapter.notifyItemChanged(a);
    }

    private void fileModelUpdate3(Map<Integer, String> itemMap) throws JSONException {
        final int a = 2;
        String file_Info = itemMap.get(a);
        fileInsertInit(file_Info, a);
        viewPager2FileModelAdapter.notifyItemChanged(a);
    }

    private void fileModelUpdate4(Map<Integer, String> itemMap) throws JSONException {
        final int a = 3;
        String file_Info = itemMap.get(a);
        fileInsertInit(file_Info, a);
        viewPager2FileModelAdapter.notifyItemChanged(a);
    }

    private void fileModelUpdate5(Map<Integer, String> itemMap) throws JSONException {
        final int a = 4;
        String file_Info = itemMap.get(a);
        fileInsertInit(file_Info, a);
        viewPager2FileModelAdapter.notifyItemChanged(a);
    }

    //todo : 파일 세팅할떄 공통적인 작업
    private void fileInsertInit(String file_Info, int a) throws JSONException {
        if (file_Info != null) {
            JSONObject jsonObject = new JSONObject(file_Info);
            String f_Name = jsonObject.getString("filePath");
            long f_ContentLength = jsonObject.getLong("file_size");
            String f_Type = jsonObject.getString("mime_type").contains("mp4") ? "video" : "image";
            fileModelList.get(a).f_Name = f_Name;
            fileModelList.get(a).f_ContentLength = f_ContentLength;
            fileModelList.get(a).f_Type = f_Type;
            if (f_Type.equals("video")) {
                long f_Duration = jsonObject.getLong("file_duration");
                fileModelList.get(a).f_Duration = f_Duration;
            }

        } else {
            fileModelList.get(a).f_Name = null;
        }
    }

    //todo : 사진 , 동영상 삭제하기
    public void removeImageView_1() {
        final int a = 0;
        pictureMap.remove(a);
        userPhotoList.remove(a);
        userRealPhotoList.setValue(pictureMap);
    }

    public void removeImageView_2() {
        final int a = 1;
        pictureMap.remove(a);
        userPhotoList.remove(a);
        userRealPhotoList.setValue(pictureMap);
    }

    public void removeImageView_3() {
        final int a = 2;
        pictureMap.remove(a);
        userPhotoList.remove(a);
        userRealPhotoList.setValue(pictureMap);
    }

    public void removeImageView_4() {
        final int a = 3;
        pictureMap.remove(a);
        userPhotoList.remove(a);
        userRealPhotoList.setValue(pictureMap);
    }

    public void removeImageView_5() {
        final int a = 4;
        pictureMap.remove(a);
        userPhotoList.remove(a);
        userRealPhotoList.setValue(pictureMap);
    }


    //todo : 사진 , 동영상 선택 다이얼로그 커스텀 초기화
    private void customDialogInit() {
        View dialogView = fileCreateActivity.getLayoutInflater().inflate(R.layout.dialog_show_file_menu, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(dialogView);
        alertDialog = builder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        image_B = dialogView.findViewById(R.id.imageButton);
        video_B = dialogView.findViewById(R.id.videoButton);
        viewPager2FileModelAdapter.setAlertDialog(alertDialog);
    }


    //todo : 어댑터 초기화
    private void adapterInit() {
        viewPager2FileModelAdapter = new ViewPager2FileModelAdapter(new FileModelDiffUtil(), context);

    }


    //todo : 뷰페이저2 초기화
    private void viewPager2Init() {
        viewPager2.setAdapter(viewPager2FileModelAdapter);
        viewPager2.setPageTransformer(new CompositePageTransformer());
        viewPager2.setOffscreenPageLimit(createFileCount);
        viewPager2Listener(viewPager2);
        fileModelInit();
    }


    //todo : 파일모델 초기화
    private void fileModelInit() {
        fileModelList = new ArrayList<>();
        FileModel f_1 = new FileModel();
        fileModelList.add(f_1);
        FileModel f_2 = new FileModel();
        fileModelList.add(f_2);
        FileModel f_3 = new FileModel();
        fileModelList.add(f_3);
        FileModel f_4 = new FileModel();
        fileModelList.add(f_4);
        FileModel f_5 = new FileModel();
        fileModelList.add(f_5);
        viewPager2FileModelAdapter.submitList(fileModelList);
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
                fileContentsTextCountViewModel.setText(countText);
                if (s.length() >= 500) {
                    Toast.makeText(context, "글자수가 최대입니다.", Toast.LENGTH_SHORT).show();
                }

            }
        };
    }


    //todo : 게시물 생성 api 통신 후 알람
    private void showCompleteAlarm() {
        String channelId = "fileCreateActivity_" + "" + context.getPackageName();
        // 알림 표시
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
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
                .setContentText("게시물이 생성되었습니다.")
                .setSmallIcon(R.mipmap.jc_logo)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .setVibrate(new long[]{1000, 1000})
                .setStyle(new NotificationCompat.BigTextStyle())
                .setAutoCancel(true);
        notificationManager.notify(ServerURL.bulletinNotification, builder.build());
    }

    private MultipartBody.Part getImageFile(String filePath, String file_Name) {
        File file = new File(filePath);
        RequestBody requestFile = RequestBody.create(file, MediaType.parse("multipart/form-data"));
        String file_Name_Copy = file_Name.replaceAll(" ", "").replaceAll("\\n", "");
        return MultipartBody.Part.createFormData("files", file_Name_Copy, requestFile);
    }

    private MultipartBody.Part getVideoFile(String filePath, String file_Name) {
        File file = new File(filePath);
        RequestBody requestFile = RequestBody.create(file, MediaType.parse("multipart/form-data"));
        String file_Name_Copy = file_Name.replaceAll(" ", "").replaceAll("\\n", "");
        return MultipartBody.Part.createFormData("files", file_Name_Copy, requestFile);
    }
}
