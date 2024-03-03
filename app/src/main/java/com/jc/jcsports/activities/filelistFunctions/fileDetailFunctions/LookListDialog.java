package com.jc.jcsports.activities.filelistFunctions.fileDetailFunctions;

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

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jc.jcsports.R;
import com.jc.jcsports.activities.adapter.B_LookAdapter;
import com.jc.jcsports.activities.adapter.B_ReplyAdapter;
import com.jc.jcsports.activities.diffUtils.LoginModelDiffUtil;
import com.jc.jcsports.activities.diffUtils.ReplyModelDiffUtil;
import com.jc.jcsports.databinding.DialogBLookListBinding;
import com.jc.jcsports.model.LoginModel;
import com.jc.jcsports.model.ReplyModel;
import com.jc.jcsports.utils.RetrofitService;
import com.jc.jcsports.utils.ServerURL;
import com.jc.jcsports.utils.ViewModelFactory;
import com.jc.jcsports.viewModel.B_Look_List_ViewModel;
import com.jc.jcsports.viewModel.ReplyModelViewModel;

import java.util.List;
import java.util.Objects;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.SingleObserver;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class LookListDialog extends Dialog {

    private DialogBLookListBinding binding;
    private Context context;
    private AlphaAnimation alphaAnimation;
    private int b_Number = 0;
    private RetrofitService service;
    private int page = 1;
    private B_LookAdapter lookAdapter;
    private B_Look_List_ViewModel b_look_list_viewModel;
    private FileDetailActivity f_Activity;
    private Observer<List<LoginModel>> observer;


    public void setF_Activity(FileDetailActivity f_Activity) {
        this.f_Activity = f_Activity;
        viewModelInit();
    }

    private void viewModelInit() {
        ViewModelFactory viewModelFactory = new ViewModelFactory();
        b_look_list_viewModel = new ViewModelProvider(f_Activity, viewModelFactory).get(B_Look_List_ViewModel.class);
        b_look_list_viewModel.init();
    }

    public void setB_Number(int b_Number) {
        this.b_Number = b_Number;
    }

    public LookListDialog(@NonNull Context context) {
        super(context);
        this.context = context;
        animationInit();
    }

    public void setService(RetrofitService service) {
        this.service = service;
    }

    private void animationInit() {
        alphaAnimation = new AlphaAnimation(0.0f, 1.0f);
        alphaAnimation.setDuration(1000);
    }


    private void api_B_LookList() {
        service.b_LookList(ServerURL.bulletinController, b_Number, page)
                .subscribeOn(Schedulers.single())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onSuccess(@NonNull List<LoginModel> loginModels) {
                        int itemCount = loginModels.size();
                        if (itemCount > 0) {
                            b_look_list_viewModel.addFileModelList(loginModels);
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }
                });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = LayoutInflater.from(context);
        binding = DataBindingUtil.inflate(inflater, R.layout.dialog_b_look_list, null, false);
        setContentView(binding.getRoot());
        dialogLayoutInfoUpdate(context);
        Objects.requireNonNull(getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setCancelable(true);
        binding.setThisDialog(this);
        look_Init();
        observerInit();
        api_B_LookList();

    }


    private void observerInit() {
        observer = loginModels -> {
            int capacity = 10;
            int itemSumCount = b_look_list_viewModel.getLiveData().getValue().size();
            int positionStart = ((page * 10) - capacity);
            if (itemSumCount > 0) {
                lookAdapter.submitList(loginModels);
                lookAdapter.notifyItemRangeInserted(positionStart, itemSumCount);
            }
        };
        b_look_list_viewModel.getLiveData().observe(f_Activity, observer);
    }


    private void look_Init() {
        lookAdapter = new B_LookAdapter(new LoginModelDiffUtil(), context);
        binding.replyLView.setHasFixedSize(true);
        binding.replyLView.setAnimation(alphaAnimation);
        binding.replyLView.setAdapter(lookAdapter);
        binding.replyLView.addItemDecoration(new RecyclerViewDecoration(20));
        binding.replyLView.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
                            api_B_LookList();
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
        window.setGravity(Gravity.BOTTOM);
        window.setAttributes(params);
    }

    private class RecyclerViewDecoration extends RecyclerView.ItemDecoration {

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
