package com.jc.jcsports.activities.signupFunctions;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.messaging.FirebaseMessaging;
import com.jc.jcsports.R;
import com.jc.jcsports.activities.navigationFunctions.MainNavigationActivity;
import com.jc.jcsports.bundle.SignUpBundle;
import com.jc.jcsports.model.LoginModel;
import com.jc.jcsports.utils.LoadingDialog;
import com.jc.jcsports.utils.RetrofitClient;
import com.jc.jcsports.utils.RetrofitService;
import com.jc.jcsports.utils.ServerURL;
import com.jc.jcsports.utils.Utils;
import com.jc.jcsports.viewModel.NickNameTextCountViewModel;

import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Pattern;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.SingleObserver;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class SignUpFunctions {


    private interface CallbackInterface {
        void callBack();
    }


    public RetrofitClient retrofitClient;
    public RetrofitService service;
    public ActivityResultLauncher<Intent> activityResultLauncher_1;
    public ActivityResultLauncher<Intent> activityResultLauncher_2;
    public ActivityResultLauncher<Intent> activityResultLauncher_3;
    public ActivityResultLauncher<Intent> activityResultLauncher_4;
    public ActivityResultLauncher<Intent> activityResultLauncher_5;

    public Map<Integer, MultipartBody.Part> userPhotoList;
    public Map<Integer, String> pictureMap;
    public MutableLiveData<Map<Integer, String>> userRealPhotoList;
    private final int RESULT_OK = -1;
    public Handler handler;
    public ActivityResultLauncher<String> requestPermissionLauncher;
    private Toast toast;
    private LoadingDialog loadingDialog;


    //todo : all of init
    public SignUpFunctions(
            SignUpActivity activity,
            Context context,
            ShapeableImageView imageView_1,
            ShapeableImageView imageView_2,
            ShapeableImageView imageView_3,
            ShapeableImageView imageView_4,
            EditText nickNameEditTextView,
            NickNameTextCountViewModel nickNameTextCountViewModel
    ) {
        /**/
        retroFitInit();
        /**/
        imageSettingInit(activity, context, imageView_1, imageView_2, imageView_3, imageView_4);
        /**/
        nickNameTextInit(nickNameEditTextView);
        /**/
        fileImage_1(activity, context);
        fileImage_2(activity, context);
        fileImage_3(activity, context);
        fileImage_4(activity, context);
        /**/
        handlerInit(context, nickNameTextCountViewModel);
        /**/
        requestPermissionInit(activity);
        /**/
        toastInit(context);
        //로딩창 객체 생성
        //로딩창을 투명하게
        loadingDialog = new LoadingDialog(context);
        loadingDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
    }

    //todo : handler dismiss after activity destroy
    public void removeHandler() {
        handler.removeMessages(0);
        handler = null;
    }

    //todo : toast Init
    private void toastInit(Context context) {
        toast = new Toast(context);
    }

    //todo : 토스트 메시지 사용
    private void toastMessage(String text) {
        toast.setText(text);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.show();
    }


    //todo : 마지막 가입 정보 세팅 및 진행 작업
    public void signUp(Context context) {

        Bundle bundle = SignUpBundle.getBundle();

        CallbackInterface callbackInterface = () -> {
            String s_Social_b = bundle.getString("s_Social");
            String s_NickName_b = bundle.getString("s_NickName");
            String s_Gender_b = bundle.getString("s_Gender");
            String s_Birth_b = bundle.getString("s_Birth");
            String s_FirebaseToken_b = bundle.getString("s_FirebaseToken");
            String s_PhoneNumber_b = bundle.getString("s_PhoneNumber");
            String s_SocialToken_b = bundle.getString("s_SocialToken");
            String s_AgeRange_b = bundle.getString("s_AgeRange");

            if (!StringUtils.isEmpty(s_NickName_b)) {
                if (userPhotoList.get(0) != null) {
                    loadingDialog.show();
                    RequestBody s_NickName = RequestBody.create(s_NickName_b, MediaType.parse("text/plain"));
                    RequestBody s_Social = RequestBody.create(s_Social_b, MediaType.parse("text/plain"));
                    RequestBody s_SocialToken = RequestBody.create(s_SocialToken_b, MediaType.parse("text/plain"));
                    RequestBody s_Gender = RequestBody.create(s_Gender_b, MediaType.parse("text/plain"));
                    RequestBody s_Birth = RequestBody.create(s_Birth_b, MediaType.parse("text/plain"));
                    RequestBody s_FirebaseToken = RequestBody.create(s_FirebaseToken_b, MediaType.parse("text/plain"));
                    RequestBody s_PhoneNumber = RequestBody.create(s_PhoneNumber_b, MediaType.parse("text/plain"));
                    RequestBody s_AgeRange = RequestBody.create(s_AgeRange_b, MediaType.parse("text/plain"));
                    Collection<MultipartBody.Part> userPhotoListValues = userPhotoList.values();
                    List<MultipartBody.Part> userPhotoList = new ArrayList<>(userPhotoListValues);
                    service.createAccountResult(
                                    ServerURL.userController,
                                    userPhotoList,
                                    s_NickName,
                                    s_Social,
                                    s_SocialToken,
                                    s_Gender,
                                    s_Birth,
                                    s_FirebaseToken,
                                    s_PhoneNumber,
                                    s_AgeRange)
                            .subscribeOn(Schedulers.single())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new SingleObserver<Boolean>() {
                                @Override
                                public void onSubscribe(@NonNull Disposable d) {
                                    Log.d("debug", "Disposable");
                                }

                                @Override
                                public void onSuccess(@NonNull Boolean aBoolean) {
                                    Log.d("debug", "boolean " + aBoolean);
                                    if (aBoolean) {
                                        getMyModel(s_SocialToken_b, context);
                                    } else {
                                        toastMessage("서버와의 통신이 원할하지 않습니다.");
                                        loadingDialog.dismiss();
                                    }
                                }

                                @Override
                                public void onError(@NonNull Throwable e) {
                                    Log.d("debug", "error " + e);
                                    loadingDialog.dismiss();
                                }
                            });
                } else {
                    toastMessage("사진 1개 이상을 넣어주세요.");
                }
            } else {
                toastMessage("닉네임 중복체크를 해주세요.");
            }
        };
        firebaseToken(bundle, callbackInterface);
    }

    //todo : myModel 가져오기
    private void getMyModel(String getId, Context context) {
        service.isExists(ServerURL.userController, getId)
                .subscribeOn(Schedulers.single())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                    }

                    @Override
                    public void onSuccess(@NonNull LoginModel loginModel) {
                        loadingDialog.dismiss();
                        showCompleteAlarm(context);
                        SignUpBundle.empty();
                        SignUpBundle.getBundle().putSerializable("loginModel", (Serializable) loginModel);
                        Intent intent = new Intent(context, MainNavigationActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        context.startActivity(intent);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }
                });
    }

    //todo : 아이디 생성 api 통신 후 알람
    private void showCompleteAlarm(Context context) {
        String channelId = "SignUpActivity_" + "" + context.getPackageName();
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
                .setContentText("회원가입이 완료되었습니다.")
                .setSmallIcon(R.mipmap.jc_logo)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .setVibrate(new long[]{1000, 1000})
                .setStyle(new NotificationCompat.BigTextStyle())
                .setAutoCancel(true);
        notificationManager.notify(ServerURL.bulletinNotification, builder.build());
    }

    //todo : 파이어베이스 토큰 생성
    private void firebaseToken(Bundle bundle, CallbackInterface callbackInterface) {
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                Log.w("abc", "Fetching FCM registration token failed", task.getException());
                return;
            }
            String token = task.getResult();
            bundle.putString("s_FirebaseToken", token);
            callbackInterface.callBack();
        });
    }

    //todo : 중복 닉네임 판별 기능
    public void checkDoubleNickName(Context context, String nickNameText) {

        service.doubleCheckNickName(ServerURL.userController, nickNameText)
                .subscribeOn(Schedulers.single())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Boolean>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        Log.d("debug", "Disposable");
                    }

                    @Override
                    public void onSuccess(@NonNull Boolean aBoolean) {
                        Log.d("debug", "boolean " + aBoolean);
                        if (!aBoolean) {
                            SignUpBundle.getBundle().putString("s_NickName", nickNameText);
                            toastMessage("사용가능한 아이디입니다.");
                        } else {
                            toastMessage("중복된 아이디입니다.");

                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.d("debug", "error " + e);
                    }
                });
    }

    //todo : 첫번쨰 사진 파일 허용 관련 체크 후 사진 선택
    public void firstImageViewPermission(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (permission_READ_MEDIA_IMAGES(context)) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_PICK);
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false);
                activityResultLauncher_1.launch(intent);
            } else {
                toastMessage("앨범 파일 접근 허용을 해주시기 바랍니다.");
            }
        } else {
            if (permission_READ_EXTERNAL_STORAGE(context) && permission_WRITE_EXTERNAL_STORAGE(context)) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_PICK);
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false);
                activityResultLauncher_1.launch(intent);
            } else {
                toastMessage("앨범 파일 접근 허용을 해주시기 바랍니다.");
            }
        }

    }


    //todo : 두번쨰 사진 파일 허용 관련 체크 후 사진 선택
    public void secondImageViewPermission(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (permission_READ_MEDIA_IMAGES(context)) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_PICK);
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false);
                activityResultLauncher_2.launch(intent);
            } else {
                toastMessage("앨범 파일 접근 허용을 해주시기 바랍니다.");
            }
        } else {
            if (permission_READ_EXTERNAL_STORAGE(context) && permission_WRITE_EXTERNAL_STORAGE(context)) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_PICK);
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false);
                activityResultLauncher_2.launch(intent);
            } else {
                toastMessage("앨범 파일 접근 허용을 해주시기 바랍니다.");

            }
        }


    }

    //todo : 세번쨰 사진 파일 허용 관련 체크 후 사진 선택
    public void thirdImageViewPermission(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (permission_READ_MEDIA_IMAGES(context)) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_PICK);
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false);
                activityResultLauncher_3.launch(intent);
            } else {
                toastMessage("앨범 파일 접근 허용을 해주시기 바랍니다.");
            }
        } else {
            if (permission_READ_EXTERNAL_STORAGE(context) && permission_WRITE_EXTERNAL_STORAGE(context)) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_PICK);
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false);
                activityResultLauncher_3.launch(intent);
            } else {
                toastMessage("앨범 파일 접근 허용을 해주시기 바랍니다.");

            }
        }
    }

    //todo : 네번쨰 사진 파일 허용 관련 체크 후 사진 선택
    public void fourthImageViewPermission(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (permission_READ_MEDIA_IMAGES(context)) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_PICK);
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false);
                activityResultLauncher_4.launch(intent);
            } else {
                toastMessage("앨범 파일 접근 허용을 해주시기 바랍니다.");
            }
        } else {
            if (permission_READ_EXTERNAL_STORAGE(context) && permission_WRITE_EXTERNAL_STORAGE(context)) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_PICK);
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false);
                activityResultLauncher_4.launch(intent);
            } else {
                toastMessage("앨범 파일 접근 허용을 해주시기 바랍니다.");

            }
        }
    }

    //todo : 사진 삭제하기 _1
    public void removeImageView_1() {
        pictureMap.remove(0);
        userPhotoList.remove(0);
        userRealPhotoList.setValue(pictureMap);
    }

    //todo : 사진 삭제하기 _2
    public void removeImageView_2() {
        pictureMap.remove(1);
        userPhotoList.remove(1);
        userRealPhotoList.setValue(pictureMap);
    }

    //todo : 사진 삭제하기 _3
    public void removeImageView_3() {
        pictureMap.remove(2);
        userPhotoList.remove(2);
        userRealPhotoList.setValue(pictureMap);
    }

    //todo : 사진 삭제하기 _4
    public void removeImageView_4() {
        pictureMap.remove(3);
        userPhotoList.remove(3);
        userRealPhotoList.setValue(pictureMap);
    }

    //todo : 닉네임 텍스트 뷰 관련 세팅
    private void nickNameTextInit(EditText nickkNameEditTextView) {
        nickkNameEditTextView.setFilters(new InputFilter[]{(source, start, end, dest, dstart, dend) -> {
            Pattern ps = Pattern.compile("^[a-zA-Z0-9가-힣ㄱ-ㅎㅏ-ㅣ\\u318D\\u119E\\u11A2\\u2022\\u2025a\\u00B7\\uFE55]+$");
            Message message = new Message();
            Bundle bundle = new Bundle();
            bundle.putInt("limitCount", dend);
            message.setData(bundle);
            handler.sendMessage(message);
            if (source.equals("") || ps.matcher(source).matches()) {
                return source;

            }
            toastMessage("한글, 영문, 숫자만 입력 가능합니다.");
            return "";
        }, new InputFilter.LengthFilter(10)});
    }

    //todo : 본인 사진 업로드 총 4장의 사진 관려 세팅
    private void imageSettingInit(
            SignUpActivity activity,
            Context context,
            ShapeableImageView firstImage,
            ShapeableImageView secondImage,
            ShapeableImageView thirdImage,
            ShapeableImageView fourthImage

    ) {
        userPhotoList = new HashMap<>();
        userPhotoList.put(0, null);
        userPhotoList.put(1, null);
        userPhotoList.put(2, null);
        userPhotoList.put(3, null);
        pictureMap = new HashMap<>();
        pictureMap.put(0, null);
        pictureMap.put(1, null);
        pictureMap.put(2, null);
        pictureMap.put(3, null);
        userRealPhotoList = new MutableLiveData<>(pictureMap);
        Observer<Map<Integer, String>> userRealPhotoListObserver = integerStringMap -> {
            String p_1 = integerStringMap.get(0);
            String p_2 = integerStringMap.get(1);
            String p_3 = integerStringMap.get(2);
            String p_4 = integerStringMap.get(3);
            if (!Objects.equals(p_1, "")) {
                Utils.glide(context)
                        .load(p_1)
                        .optionalCenterCrop()
                        .sizeMultiplier(0.7f)
                        .into(firstImage);
            } else {
                Utils.glide(context)
                        .load("")
                        .optionalCenterCrop()
                        .sizeMultiplier(0.7f)
                        .into(firstImage);
            }

            if (!Objects.equals(p_2, "")) {
                Utils.glide(context)
                        .load(p_2)
                        .optionalCenterCrop()
                        .sizeMultiplier(0.7f)
                        .into(secondImage);
            } else {
                Utils.glide(context)
                        .load("")
                        .optionalCenterCrop()
                        .sizeMultiplier(0.7f)
                        .into(secondImage);
            }

            if (!Objects.equals(p_3, "")) {
                Utils.glide(context)
                        .load(p_3)
                        .optionalCenterCrop()
                        .sizeMultiplier(0.7f)
                        .into(thirdImage);
            } else {
                Utils.glide(context)
                        .load("")
                        .optionalCenterCrop()
                        .sizeMultiplier(0.7f)
                        .into(thirdImage);
            }

            if (!Objects.equals(p_4, "")) {
                Utils.glide(context)
                        .load(p_4)
                        .optionalCenterCrop()
                        .sizeMultiplier(0.7f)
                        .into(fourthImage);
            } else {
                Utils.glide(context)
                        .load("")
                        .optionalCenterCrop()
                        .sizeMultiplier(0.7f)
                        .into(fourthImage);
            }
        };
        userRealPhotoList.observe(activity, userRealPhotoListObserver);
    }

    //todo : handler Init
    private void handlerInit(Context context, NickNameTextCountViewModel liveDataTextCount) {
        handler = new Handler(context.getMainLooper(), msg -> {
            Bundle bundle = msg.getData();
            int value = bundle.getInt("limitCount");
            String text = "(" + value + "/10)";
            liveDataTextCount.setText(text);
            return false;
        });
    }

    //todo : RestApi init
    private void retroFitInit() {
        retrofitClient = RetrofitClient.getInstance();
        service = RetrofitClient.getServerInterface();

    }


    //todo : firstImage
    private void fileImage_1(SignUpActivity activity, Context context) {

        activityResultLauncher_1 = activity.registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {


                Intent data = result.getData();
                //TODO : result 값이 성공할 경우
                if (result.getResultCode() == RESULT_OK) {

                    if (data == null) {
                        //TODO : 이미지 선택이 없을 경우
                        Snackbar.make(activity.getCurrentFocus(), "현재 선택된 사진이 없습니다.", Snackbar.LENGTH_SHORT).show();
                        Log.e("abc", "data is null");
                    } else {
                        //TODO : 이미지 선택이 하나일 경우
                        assert result.getData() != null;
                        Uri uri = result.getData().getData();
                        String imagePath = Utils.getRealPathFromURI(uri, context);
                        if (pictureMap.get(0) != null) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                pictureMap.replace(0, imagePath);
                                userPhotoList.replace(0, Utils.getImageFile(imagePath));
                            } else {
                                pictureMap.remove(0);
                                pictureMap.put(0, imagePath);
                                userPhotoList.remove(0);
                                userPhotoList.put(0, Utils.getImageFile(imagePath));
                            }
                        } else {
                            pictureMap.put(0, imagePath);
                            userPhotoList.put(0, Utils.getImageFile(imagePath));
                        }
                        userRealPhotoList.setValue(pictureMap);

                    }
                }
            }
        });
    }

    //todo : secondImage
    private void fileImage_2(SignUpActivity activity, Context context) {

        activityResultLauncher_2 = activity.registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {

                Log.d("debug", result.getResultCode() + " : " + result.describeContents());
                Intent data = result.getData();

                //TODO : result 값이 성공할 경우
                if (result.getResultCode() == RESULT_OK) {
                    if (data == null) {
                        //TODO : 이미지 선택이 없을 경우
                        Snackbar.make(activity.getCurrentFocus(), "현재 선택된 사진이 없습니다.", Snackbar.LENGTH_SHORT).show();
                        Log.e("abc", "data is null");
                    } else {
                        //TODO : 이미지 선택이 하나일 경우
                        assert result.getData() != null;
                        Uri uri = result.getData().getData();

                        String imagePath = Utils.getRealPathFromURI(uri, context);
                        if (pictureMap.get(1) != null) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                pictureMap.replace(1, imagePath);
                                userPhotoList.replace(1, Utils.getImageFile(imagePath));
                            } else {
                                pictureMap.remove(1);
                                pictureMap.put(1, imagePath);
                                userPhotoList.remove(1);
                                userPhotoList.put(1, Utils.getImageFile(imagePath));
                            }
                        } else {
                            pictureMap.put(1, imagePath);
                            userPhotoList.put(1, Utils.getImageFile(imagePath));
                        }

                        userRealPhotoList.setValue(pictureMap);

                    }
                }
            }
        });
    }

    //todo : thirdImage
    private void fileImage_3(SignUpActivity activity, Context context) {

        activityResultLauncher_3 = activity.registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {

                Log.d("debug", result.getResultCode() + " : " + result.describeContents());
                Intent data = result.getData();

                //TODO : result 값이 성공할 경우
                if (result.getResultCode() == RESULT_OK) {
                    if (data == null) {
                        //TODO : 이미지 선택이 없을 경우
                        Snackbar.make(activity.getCurrentFocus(), "현재 선택된 사진이 없습니다.", Snackbar.LENGTH_SHORT).show();
                        Log.e("abc", "data is null");
                    } else {
                        //TODO : 이미지 선택이 하나일 경우
                        assert result.getData() != null;
                        Uri uri = result.getData().getData();

                        String imagePath = Utils.getRealPathFromURI(uri, context);
                        if (pictureMap.get(2) != null) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                pictureMap.replace(2, imagePath);
                                userPhotoList.replace(2, Utils.getImageFile(imagePath));
                            } else {
                                pictureMap.remove(2);
                                pictureMap.put(2, imagePath);
                                userPhotoList.remove(2);
                                userPhotoList.put(2, Utils.getImageFile(imagePath));
                            }
                        } else {
                            pictureMap.put(2, imagePath);
                            userPhotoList.put(2, Utils.getImageFile(imagePath));
                        }
                        userRealPhotoList.setValue(pictureMap);
                    }
                }
            }
        });
    }


    //todo : fourthImage
    private void fileImage_4(SignUpActivity activity, Context context) {

        activityResultLauncher_4 = activity.registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {

                Log.d("debug", result.getResultCode() + " : " + result.describeContents());
                Intent data = result.getData();

                //TODO : result 값이 성공할 경우
                if (result.getResultCode() == RESULT_OK) {
                    if (data == null) {
                        //TODO : 이미지 선택이 없을 경우
                        Snackbar.make(activity.getCurrentFocus(), "현재 선택된 사진이 없습니다.", Snackbar.LENGTH_SHORT).show();
                        Log.e("abc", "data is null");
                    } else {
                        //TODO : 이미지 선택이 하나일 경우
                        assert result.getData() != null;
                        Uri uri = result.getData().getData();

                        String imagePath = Utils.getRealPathFromURI(uri, context);
                        if (pictureMap.get(3) != null) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                pictureMap.replace(3, imagePath);
                                userPhotoList.replace(3, Utils.getImageFile(imagePath));
                            } else {
                                pictureMap.remove(3);
                                pictureMap.put(3, imagePath);
                                userPhotoList.remove(3);
                                userPhotoList.put(3, Utils.getImageFile(imagePath));
                            }
                        } else {
                            pictureMap.put(3, imagePath);
                            userPhotoList.put(3, Utils.getImageFile(imagePath));
                        }
                        userRealPhotoList.setValue(pictureMap);
                    }
                }
            }
        });
    }

    //todo : requestPermissionInit
    private void requestPermissionInit(SignUpActivity activity) {
        requestPermissionLauncher =
                activity.registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
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


    //todo : 파일 접근 권한 쓰기
    private boolean permission_WRITE_EXTERNAL_STORAGE(Context context) {

        if (ContextCompat.checkSelfPermission(
                context, Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                PackageManager.PERMISSION_GRANTED) {
            // You can use the API that requires the permission.

            Utils.logCheck("WRITE_EXTERNAL_STORAGE");
            return true;
        } else {
            // You can directly ask for the permission.
            // The registered ActivityResultCallback gets the result of this request.
            requestPermissionLauncher.launch(
                    Manifest.permission.WRITE_EXTERNAL_STORAGE);
            return false;
        }
    }

    //todo : 파일 접근 권한 읽기
    private boolean permission_READ_EXTERNAL_STORAGE(Context context) {

        if (ContextCompat.checkSelfPermission(
                context, Manifest.permission.READ_EXTERNAL_STORAGE) ==
                PackageManager.PERMISSION_GRANTED) {
            // You can use the API that requires the permission.
            Utils.logCheck("READ_EXTERNAL_STORAGE");
            return true;
        } else {
            // You can directly ask for the permission.
            // The registered ActivityResultCallback gets the result of this request.
            requestPermissionLauncher.launch(
                    Manifest.permission.READ_EXTERNAL_STORAGE);
            return false;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    private boolean permission_READ_MEDIA_IMAGES(Context context) {
        if (ContextCompat.checkSelfPermission(
                context, Manifest.permission.READ_MEDIA_IMAGES) ==
                PackageManager.PERMISSION_GRANTED) {
            // You can use the API that requires the permission.
            Utils.logCheck("READ_EXTERNAL_STORAGE");
            return true;
        } else {
            // You can directly ask for the permission.
            // The registered ActivityResultCallback gets the result of this request.
            requestPermissionLauncher.launch(
                    Manifest.permission.READ_MEDIA_IMAGES);
            return false;
        }
    }
}
