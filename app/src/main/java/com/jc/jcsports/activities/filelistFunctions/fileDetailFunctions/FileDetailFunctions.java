package com.jc.jcsports.activities.filelistFunctions.fileDetailFunctions;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.Player;
import com.jc.jcsports.R;
import com.jc.jcsports.activities.adapter.B_FileAdapter;
import com.jc.jcsports.activities.adapter.B_ReplyAdapter;
import com.jc.jcsports.activities.diffUtils.FileModelDiffUtil;
import com.jc.jcsports.activities.diffUtils.ReplyModelDiffUtil;
import com.jc.jcsports.activities.filelistFunctions.fileModifyFunctions.FileModifyActivity;
import com.jc.jcsports.bundle.SignUpBundle;
import com.jc.jcsports.databinding.DialogInsertReplyBinding;
import com.jc.jcsports.model.FileModel;
import com.jc.jcsports.model.LoginModel;
import com.jc.jcsports.model.ReplyModel;
import com.jc.jcsports.utils.RetrofitService;
import com.jc.jcsports.utils.ServerURL;
import com.jc.jcsports.utils.Utils;
import com.jc.jcsports.utils.ViewModelFactory;
import com.jc.jcsports.viewModel.B_Like_Count_ViewModel;
import com.jc.jcsports.viewModel.B_Reply_Count_ViewModel;
import com.jc.jcsports.viewModel.ReplyModelViewModel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.SingleObserver;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class FileDetailFunctions {

    private AlertDialog alertDialog;
    private Button modify_B, remove_B;
    private Context context;
    private FileDetailActivity fileDetailActivity;
    private ViewPager2 viewPager2;
    private B_FileAdapter b_fileAdapter;
    private int b_Number = 0;
    private RetrofitService service;
    private LoginModel loginModel;
    private TextView bTextView;
    private List<FileModel> f_ModelList;
    private B_Like_Count_ViewModel b_Like_Count_ViewModel;
    private B_Reply_Count_ViewModel b_reply_count_viewModel;
    private ReplyListDialog r_Dialog;
    private LookListDialog l_Dialog;
    private ExoPlayer player;
    private List<MediaItem> mediaItemList;

    public FileDetailFunctions(FileDetailActivity fileDetailActivity,
                               Context context,
                               ViewPager2 viewPager2, int b_Number, RetrofitService service,
                               TextView bTextView) {
        this.context = context;
        this.fileDetailActivity = fileDetailActivity;
        this.viewPager2 = viewPager2;
        this.b_Number = b_Number;
        this.service = service;
        this.bTextView = bTextView;
        loginModel = (LoginModel) SignUpBundle.getBundle().getSerializable("loginModel");

        customDialogInit();
        f_ViewPagerInit();
        api_File();
        showLookDialogInit();

    }

    public void startVideo() {
        if (player != null) {
            player.play();
        }
    }

    public void stopVideo() {
        if (player != null) {
            player.pause();
            player.seekTo(0);
        }

    }

    public void exoplayerRelease() {
        if (player != null) {
            player.release();
            player = null;
        }
    }

    private void exoplayerInit() {
        if (player == null) {
            player = new ExoPlayer.Builder(context).build();
            player.setVideoScalingMode(C.VIDEO_SCALING_MODE_SCALE_TO_FIT);
            player.setRepeatMode(Player.REPEAT_MODE_ALL);
        }

    }


    public void setB_reply_count_viewModel(B_Reply_Count_ViewModel b_reply_count_viewModel) {
        this.b_reply_count_viewModel = b_reply_count_viewModel;
        showReplyDialogInit();
    }


    public void setB_Like_Count_ViewModel(B_Like_Count_ViewModel b_Like_Count_ViewModel) {
        this.b_Like_Count_ViewModel = b_Like_Count_ViewModel;
    }


    private void api_Delete_B_Detail() {
        service.delete_B_Detail(ServerURL.bulletinController, b_Number)
                .subscribeOn(Schedulers.single())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onSuccess(@NonNull Boolean aBoolean) {
                        if (aBoolean) {
                            alertDialog.dismiss();
                            Intent intent = new Intent();
                            intent.setAction("b_Detail");
                            intent.putExtra("b_Delete", true);
                            fileDetailActivity.sendBroadcast(intent);
                            fileDetailActivity.finish();
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }
                });
    }


    public void api_File() {
        f_ModelList = new ArrayList<>();
        service.getB_DetailFiles(ServerURL.bulletinController, b_Number)
                .subscribeOn(Schedulers.single())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onSuccess(@NonNull List<FileModel> fileModelList) {
                        Utils.logCheck("fileModelList => "+ fileModelList.toString());
                        f_ModelList.addAll(fileModelList);

                        List<FileModel> f_Model_Copy = new ArrayList<>();
                        for (FileModel f : fileModelList) {
                            if (!f.f_isDelete) {
                                f_Model_Copy.add(f);
                            }
                        }
                        mediaItemInit(fileModelList);
                        b_fileAdapter.submitList(f_Model_Copy);
                        int itemCount = fileModelList.size();
                        b_fileAdapter.notifyItemRangeInserted(0, itemCount);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }
                });
    }

    private void mediaItemInit(List<FileModel> fileModelList) {
        mediaItemList = new ArrayList<>();
        for (FileModel f : fileModelList) {
            if (!f.f_isDelete) {
                String url = ServerURL.VIDEO_URL + f.f_Name;
                MediaItem mediaItem = MediaItem.fromUri(url);
                mediaItemList.add(mediaItem);

            }
        }
    }

    private void f_ViewPagerInit() {
        b_fileAdapter = new B_FileAdapter(new FileModelDiffUtil(), context);
        viewPager2.setAdapter(b_fileAdapter);
        viewPager2.setOffscreenPageLimit(1);
        viewPager2.setPageTransformer(new ZoomInPageTransformer());
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                RecyclerView recyclerView = (RecyclerView) viewPager2.getChildAt(0);
                RecyclerView.ViewHolder viewHolder = recyclerView.findViewHolderForAdapterPosition(position);
                if (viewHolder instanceof B_FileAdapter.ItemBFileListBindingAdapterViewHolder) {
                    B_FileAdapter.ItemBFileListBindingAdapterViewHolder viewPagerViewHolder = (B_FileAdapter.ItemBFileListBindingAdapterViewHolder) viewHolder;
                    exoplayerRelease();
                    exoplayerInit();
                    player.setMediaItem(mediaItemList.get(position));
                    player.prepare();
                    viewPagerViewHolder.styledPlayerView.setPlayer(player);
                    player.setPlayWhenReady(true);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
            }
        });
    }

    public void back() {
        fileDetailActivity.onBackPressed();
    }


    public void showReplyDialogInit() {
        r_Dialog = new ReplyListDialog(context);
        r_Dialog.setLoginModel(loginModel);
        r_Dialog.setB_Number(b_Number);
        r_Dialog.setB_reply_count_viewModel(b_reply_count_viewModel);
        r_Dialog.setF_Activity(fileDetailActivity);
        r_Dialog.setService(service);
    }


    public void showLookDialogInit() {
        l_Dialog = new LookListDialog(context);
        l_Dialog.setF_Activity(fileDetailActivity);
        l_Dialog.setB_Number(b_Number);
        l_Dialog.setService(service);
    }


    public void showLookListDialog() {
        l_Dialog.show();
    }

    public void showReplyDialog() {
        r_Dialog.show();
    }


    public void showDialog() {
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


    public void b_Like() {
        int s_Number = loginModel.s_Number;
        service.b_Like(ServerURL.bulletinController, b_Number, s_Number)
                .subscribeOn(Schedulers.single())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Integer>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onSuccess(@NonNull Integer count) {
                        b_Like_Count_ViewModel.setCount(count);
                        Toast.makeText(context, "좋아요 !!", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Toast.makeText(context, "서버와의 통신이 원할하지 않습니다.", Toast.LENGTH_SHORT).show();
                    }
                });
    }


    //todo : 사진 , 동영상 선택 다이얼로그 커스텀 초기화
    private void customDialogInit() {
        View dialogView = fileDetailActivity.getLayoutInflater().inflate(R.layout.dialog_show_file_menu, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(dialogView);
        alertDialog = builder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        modify_B = dialogView.findViewById(R.id.imageButton);
        modify_B.setText("수정");
        modify_B.setOnClickListener(v -> {
            Intent intent = new Intent(context, FileModifyActivity.class);
            intent.putExtra("f_Model", (Serializable) f_ModelList);
            intent.putExtra("b_Content", bTextView.getText().toString());
            intent.putExtra("b_Number", b_Number);
            context.startActivity(intent);
            alertDialog.dismiss();
        });
        remove_B = dialogView.findViewById(R.id.videoButton);
        remove_B.setText("삭제");
        remove_B.setOnClickListener(v -> {

            api_Delete_B_Detail();
        });
    }


    private class ZoomInPageTransformer implements ViewPager2.PageTransformer {
        private static final float MIN_SCALE = 0.85f; // scale // sizes
        private float imageMargin = context.getResources().getDimensionPixelOffset(R.dimen.image_margin);
        private float imageSize = context.getResources().getDimensionPixelSize(R.dimen.image_size);
        private float screenWidth = context.getResources().getDisplayMetrics().widthPixels;
        private float offsetPx = screenWidth - imageMargin - imageSize;

        @Override
        public void transformPage(@NonNull View view, float position) {
            view.setTranslationX(position * -offsetPx);
            if (position < -1) return;
            if (position <= 1) { // animation views
                float scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position * getEase(Math.abs(position))));
                view.setScaleX(scaleFactor);
                view.setScaleY(scaleFactor); // If you use to LinearLayout in list_item, Use after codes. //
                int height = view.getHeight();
                float y = -((float) height - (scaleFactor * height)) / 4f;
                view.setTranslationY(y);
            } else { // side views
                view.setScaleX(MIN_SCALE);
                view.setScaleY(MIN_SCALE);
            }
        }

        private float getEase(float position) {
            float sqt = position * position;
            return sqt / (2.0f * (sqt - position) + 1.0f);
        }
    }


}
