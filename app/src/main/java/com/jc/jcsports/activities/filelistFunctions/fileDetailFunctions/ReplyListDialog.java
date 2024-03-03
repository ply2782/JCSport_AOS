package com.jc.jcsports.activities.filelistFunctions.fileDetailFunctions;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jc.jcsports.R;
import com.jc.jcsports.activities.adapter.B_ReplyAdapter;
import com.jc.jcsports.activities.diffUtils.ReplyModelDiffUtil;
import com.jc.jcsports.databinding.DialogBReplyListBinding;
import com.jc.jcsports.databinding.DialogInsertReplyBinding;
import com.jc.jcsports.model.LoginModel;
import com.jc.jcsports.model.ReplyModel;
import com.jc.jcsports.utils.RetrofitService;
import com.jc.jcsports.utils.ServerURL;
import com.jc.jcsports.utils.Utils;
import com.jc.jcsports.utils.ViewModelFactory;
import com.jc.jcsports.viewModel.B_Reply_Count_ViewModel;
import com.jc.jcsports.viewModel.ReplyModelViewModel;

import java.util.List;
import java.util.Objects;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.SingleObserver;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ReplyListDialog extends Dialog {
    private DialogBReplyListBinding binding;
    private Context context;
    private B_ReplyAdapter b_replyAdapter;
    private AlphaAnimation alphaAnimation;
    private int page = 1;
    private Observer<List<ReplyModel>> observer;
    private B_Reply_Count_ViewModel b_reply_count_viewModel;
    private AlertDialog replyDialog;
    private int b_Number = 0;
    private RetrofitService service;
    private LoginModel loginModel;
    private FileDetailActivity f_Activity;
    private ReplyModelViewModel r_V_Model;

    public void setF_Activity(FileDetailActivity f_Activity) {
        this.f_Activity = f_Activity;
        viewModelInit();
        observerInit();
        insertReplyInit();

    }

    public void setLoginModel(LoginModel loginModel) {
        this.loginModel = loginModel;
    }

    public void setB_Number(int b_Number) {
        this.b_Number = b_Number;
    }

    public void setB_reply_count_viewModel(B_Reply_Count_ViewModel b_reply_count_viewModel) {
        this.b_reply_count_viewModel = b_reply_count_viewModel;
    }

    public void setService(RetrofitService service) {
        this.service = service;
    }

    public ReplyListDialog(@NonNull Context context) {
        super(context);
        this.context = context;
        animationInit();

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = LayoutInflater.from(context);
        binding = DataBindingUtil.inflate(inflater, R.layout.dialog_b_reply_list, null, false);
        setContentView(binding.getRoot());
        dialogLayoutInfoUpdate(context);
        Objects.requireNonNull(getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setCancelable(true);
        binding.setThisDialog(this);
        reply_Init();
        api_Reply();


    }


    //todo : 동적으로 커스텀 다이얼로그 width , height 변경
    private void dialogLayoutInfoUpdate(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display defaultDisplay = windowManager.getDefaultDisplay();
        Point point = new Point();
        defaultDisplay.getSize(point);
        int deviceWidth = point.x; // 디바이스 가로 길이
        WindowManager.LayoutParams params = new WindowManager.LayoutParams();
        params.copyFrom(getWindow().getAttributes());
        params.width = (int) (Math.round((deviceWidth * 1)));
        Window window = getWindow();
        // 열기&닫기 시 애니메이션 설정
        params.windowAnimations = R.style.AnimationPopupStyle;
        // UI 하단 정렬
        window.setGravity( Gravity.BOTTOM );
        window.setAttributes(params);
    }

    private void viewModelInit() {
        ViewModelFactory viewModelFactory = new ViewModelFactory();
        r_V_Model = new ViewModelProvider(f_Activity, viewModelFactory).get(ReplyModelViewModel.class);
        r_V_Model.init();
    }


    private void animationInit() {
        alphaAnimation = new AlphaAnimation(0.0f, 1.0f);
        alphaAnimation.setDuration(1000);
    }

    private void reply_Init() {
        b_replyAdapter = new B_ReplyAdapter(new ReplyModelDiffUtil(), context);
        binding.replyRView.setHasFixedSize(true);
        binding.replyRView.setAnimation(alphaAnimation);
        binding.replyRView.setAdapter(b_replyAdapter);
        binding.replyRView.addItemDecoration(new RecyclerViewDecoration(20));
        binding.replyRView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (!recyclerView.canScrollVertically(1)) {
                    if (newState == 0) {
                        int lastPosition =
                                ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastCompletelyVisibleItemPosition();
                        int totalCount = recyclerView.getAdapter().getItemCount();
                        int capacity = 10;
                        boolean isEnd = !recyclerView.canScrollVertically(1);
                        if ((lastPosition == totalCount - 1) && isEnd && (totalCount % capacity == 0)) {
                            page++;
                            api_Reply();
                        }
                    }
                }
            }

            @Override
            public void onScrolled(@io.reactivex.rxjava3.annotations.NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
    }


    private void api_Reply_Count() {
        int s_Number = loginModel.s_Number;
        service.b_Reply(ServerURL.bulletinController, b_Number, s_Number)
                .subscribeOn(Schedulers.single())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Integer>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onSuccess(@NonNull Integer count) {
                        b_reply_count_viewModel.setCount(count);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Toast.makeText(context, "서버와의 통신이 원할하지 않습니다.", Toast.LENGTH_SHORT).show();
                    }
                });

    }

    private void api_Reply() {
        service.getB_DetailReply(ServerURL.bulletinController, loginModel.s_Number, b_Number, page)
                .subscribeOn(Schedulers.single())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onSuccess(@NonNull List<ReplyModel> replyModels) {
                        int itemCount = replyModels.size();
                        if (itemCount > 0) {
                            r_V_Model.addReplyModelList(replyModels);
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }
                });
    }


    private void observerInit() {
        observer = replyModelList -> {
            int capacity = 10;
            int itemSumCount = r_V_Model.getListMutableLiveData().getValue().size();
            int positionStart = ((page * 10) - capacity);
            if (itemSumCount > 0) {
                b_replyAdapter.submitList(replyModelList);
                b_replyAdapter.notifyItemRangeInserted(positionStart, itemSumCount);
            }
        };
        r_V_Model.getListMutableLiveData().observe(f_Activity, observer);
    }

    private void api_Reply_Refresh() {
        page = 1;
        service.getB_DetailReply(ServerURL.bulletinController, loginModel.s_Number, b_Number, page)
                .subscribeOn(Schedulers.single())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onSuccess(@NonNull List<ReplyModel> replyModels) {
                        int itemCount = b_replyAdapter.getItemCount();
                        r_V_Model.removeReplyModels();
                        b_replyAdapter.notifyItemRangeRemoved(0, itemCount);
                        r_V_Model.setReplyModelList(replyModels);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }
                });
    }


    public void Reply(View v) {
        insertReplyDialogShow();
    }


    public void insertReplyDialogShow() {
        replyDialog.show();
    }



    private void insertReplyInit() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        DialogInsertReplyBinding binding = DataBindingUtil.inflate(inflater, R.layout.dialog_insert_reply, null, false);
        binding.setLoginModel(loginModel);
        builder.setView(binding.getRoot());
        replyDialog = builder.create();
        replyDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        replyDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        binding.completeButton.setOnClickListener(v -> {
            String r_Content = binding.rContentView.getText().toString();
            api_InsertReply(r_Content);
        });
    }

    private void api_InsertReply(String r_Content) {
        int s_Number = loginModel.s_Number;
        service.createB_Reply(ServerURL.bulletinController, b_Number, page, s_Number, r_Content)
                .subscribeOn(Schedulers.single())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onSuccess(@NonNull List<ReplyModel> r_Models) {
                        Toast.makeText(context, "댓글 작성 완료하였습니다.", Toast.LENGTH_SHORT).show();
                        replyDialog.dismiss();
                        api_Reply_Count();
                        api_Reply_Refresh();
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Utils.logCheck("error +> " + e.getMessage());
                        Toast.makeText(context, "댓글 작성 중 오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
                        replyDialog.dismiss();
                    }
                });
    }

    public class RecyclerViewDecoration extends RecyclerView.ItemDecoration {

        private final int divHeight;

        public RecyclerViewDecoration(int divHeight) {
            this.divHeight = divHeight;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);
            outRect.top = divHeight;
        }
    }
}
