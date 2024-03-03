package com.jc.jcsports.activities.userListFunctions;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.animation.AlphaAnimation;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.jc.jcsports.R;
import com.jc.jcsports.activities.adapter.U_UserListAdapter;
import com.jc.jcsports.activities.chatFunctions.chatRoom.ChatRoomFunctions;
import com.jc.jcsports.activities.diffUtils.UserListDiffUtil;
import com.jc.jcsports.bundle.SignUpBundle;
import com.jc.jcsports.model.BulletinModel;
import com.jc.jcsports.model.LoginModel;
import com.jc.jcsports.model.chat.ChatListModel;
import com.jc.jcsports.utils.LoadingDialog;
import com.jc.jcsports.utils.RetrofitService;
import com.jc.jcsports.utils.ServerURL;
import com.jc.jcsports.utils.Utils;
import com.jc.jcsports.utils.ViewModelFactory;
import com.jc.jcsports.viewModel.MyBulletinViewModel;
import com.jc.jcsports.viewModel.UserListViewModel;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.SingleObserver;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class UserListFunctions {
    private int currentPage = 1;
    private Context context;
    private RetrofitService service;
    private U_UserListAdapter adapter;
    private RecyclerView recyclerView;
    private UserListViewModel u_ViewModel;
    private FragmentActivity fragmentActivity;
    private LoadingDialog loadingDialog;
    private Observer<List<LoginModel>> observer;
    private SwipeRefreshLayout swipeRefreshLayout;
    private AlphaAnimation alphaAnimation;
    private LoginModel loginModel;

    public UserListFunctions() {
        animationInit();
        myModelInit();
    }

    public void setSwipeRefreshLayout(SwipeRefreshLayout swipeRefreshLayout) {
        this.swipeRefreshLayout = swipeRefreshLayout;
        swipeRefreshLayoutInit();
        userListInit();
    }

    //todo : 로딩 다이얼로그
    private void loadingDialogInit() {
        loadingDialog = new LoadingDialog(context);
        loadingDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
    }

    public void setFragmentActivity(FragmentActivity fragmentActivity) {
        this.fragmentActivity = fragmentActivity;
        viewModelInit();
        observerInit();
    }

    private void myModelInit() {
        loginModel = (LoginModel) SignUpBundle.getBundle().getSerializable("loginModel");
    }

    public void setContext(Context context) {
        this.context = context;
        loadingDialogInit();
    }

    public void setService(RetrofitService service) {
        this.service = service;
    }

    private void viewModelInit() {
        ViewModelFactory viewModelFactory = new ViewModelFactory();
        u_ViewModel = new ViewModelProvider(fragmentActivity, viewModelFactory).get(UserListViewModel.class);
        u_ViewModel.init();
    }

    private void observerInit() {
        observer = loginModels -> {
            int capacity = 10;
            int itemSumCount = u_ViewModel.getListMutableLiveData().getValue().size();
            int positionStart = ((currentPage * 10) - capacity);
            if (itemSumCount > 0) {
                adapter.submitList(loginModels);
                adapter.notifyItemRangeInserted(positionStart, itemSumCount);
                loadingDialog.dismiss();
            }
        };
        u_ViewModel.getListMutableLiveData().observe(fragmentActivity, observer);
    }


    public void setRecyclerView(RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
        recyclerviewInit();
    }

    private void swipeRefreshLayoutInit() {
        swipeRefreshLayout.setOnRefreshListener(() -> {
            loadingDialog.show();
            userListRefreshInit();
            if (swipeRefreshLayout.isRefreshing()) {
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }


    private void userListInit() {
        loadingDialog.show();
        service.userList(ServerURL.userListController, currentPage, loginModel.s_Number)
                .subscribeOn(Schedulers.single())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onSuccess(@NonNull List<LoginModel> loginModels) {
                        if (loginModels.size() > 0) {
                            u_ViewModel.setLoginModels(loginModels);
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }
                });
    }

    private void userListRefreshInit() {
        currentPage = 1;
        service.userList(ServerURL.userListController, currentPage, loginModel.s_Number)
                .subscribeOn(Schedulers.single())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onSuccess(@NonNull List<LoginModel> loginModels) {
                        int itemCount = adapter.getItemCount();
                        u_ViewModel.remove();
                        if (loginModels.size() > 0) {
                            adapter.notifyItemRangeRemoved(0, itemCount);
                            u_ViewModel.loginModelsInit(loginModels);
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }
                });
    }


    private void animationInit() {
        alphaAnimation = new AlphaAnimation(0.0f, 1.0f);
        alphaAnimation.setDuration(1000);
    }

    public int getPxFromDp(float dp) {
        int px = 0;
        px = (int) (dp * context.getResources().getDisplayMetrics().density);
        return px;
    }

    private void recyclerviewInit() {
        adapter = new U_UserListAdapter(new UserListDiffUtil());
        adapter.setContext(context);
        adapter.setService(service);
        adapter.setMyS_Number(loginModel.s_Number);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        recyclerView.setAnimation(alphaAnimation);
        recyclerView.addItemDecoration(new RecyclerViewDecoration(getPxFromDp(10)));
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
                            currentPage++;
                            userListInit();
                        }
                    }
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

            }
        });
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
