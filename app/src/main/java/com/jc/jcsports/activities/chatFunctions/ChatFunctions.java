package com.jc.jcsports.activities.chatFunctions;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.animation.AlphaAnimation;
import android.view.animation.BounceInterpolator;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.jc.jcsports.R;
import com.jc.jcsports.activities.adapter.C_ListAdapter;
import com.jc.jcsports.activities.diffUtils.ChatListDiffUtil;
import com.jc.jcsports.bundle.SignUpBundle;
import com.jc.jcsports.model.chat.ChatListModel;
import com.jc.jcsports.model.LoginModel;
import com.jc.jcsports.utils.LoadingDialog;
import com.jc.jcsports.utils.RetrofitClient;
import com.jc.jcsports.utils.RetrofitService;
import com.jc.jcsports.utils.ServerURL;
import com.jc.jcsports.utils.Utils;
import com.jc.jcsports.utils.ViewModelFactory;
import com.jc.jcsports.viewModel.ChatListViewModel;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;

import io.reactivex.rxjava3.core.SingleObserver;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ChatFunctions {

    private C_ListAdapter c_listAdapter;
    private Context context;
    private FragmentActivity f_Activity;
    private SwipeRefreshLayout swipeRefreshLayout;
    private AlphaAnimation alphaAnimation;
    private RetrofitService service;
    private RetrofitClient retrofitClient;
    private RecyclerView recyclerView;
    private int currentPage = 1;
    private ChatListViewModel viewModel;
    private PhItemAnimator phItemAnimator;
    private LoginModel loginModel;
    private Observer<List<ChatListModel>> observer;
    private LoadingDialog loadingDialog;

    public ChatFunctions() {
        adapterInit();
        retroFitInit();
        myModelInit();
    }


    //todo : 로딩 다이얼로그
    private void loadingDialogInit() {
        loadingDialog = new LoadingDialog(context);
        loadingDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
    }

    public void setSwipeRefreshLayout(SwipeRefreshLayout swipeRefreshLayout) {
        this.swipeRefreshLayout = swipeRefreshLayout;
        swipeRefreshLayoutInit();
    }

    public void setRecyclerView(RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
        animationInit();
        recyclerviewInit();
        api_ChatList();
    }


    private void myModelInit() {
        loginModel = (LoginModel) SignUpBundle.getBundle().getSerializable("loginModel");
    }

    //todo : RestApi init
    private void retroFitInit() {
        retrofitClient = RetrofitClient.getInstance();
        service = RetrofitClient.getServerInterface();
    }

    private void viewModelInit() {
        ViewModelFactory viewModelFactory = new ViewModelFactory();
        viewModel = new ViewModelProvider(f_Activity, viewModelFactory).get(ChatListViewModel.class);
        viewModel.init();
    }


    private void api_RefreshChatList() {
        currentPage = 1;
        service.chatList(ServerURL.chatController, currentPage, loginModel.s_Number)
                .subscribeOn(Schedulers.single())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<List<ChatListModel>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onSuccess(@NonNull List<ChatListModel> chatListModels) {
                        int itemCount = c_listAdapter.getItemCount();
                        viewModel.removeItem();
                        if (chatListModels.size() > 0) {
                            c_listAdapter.notifyItemRangeRemoved(0, itemCount);
                            viewModel.setItem(chatListModels);
                        }

                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }
                });
    }

    private void api_ChatList() {
        service.chatList(ServerURL.chatController, currentPage, loginModel.s_Number)
                .subscribeOn(Schedulers.single())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<List<ChatListModel>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onSuccess(@NonNull List<ChatListModel> chatListModels) {
                        if (chatListModels.size() > 0) {
                            viewModel.addItem(chatListModels);
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }
                });
    }

    public void setContext(Context context) {
        this.context = context;
        loadingDialogInit();
    }

    public void setF_Activity(FragmentActivity f_Activity) {
        this.f_Activity = f_Activity;
        viewModelInit();
        observerInit();
    }

    private void adapterInit() {
        c_listAdapter = new C_ListAdapter(new ChatListDiffUtil());
        c_listAdapter.setContext(context);
    }

    private void swipeRefreshLayoutInit() {
        swipeRefreshLayout.setOnRefreshListener(() -> {
            api_RefreshChatList();
            if (swipeRefreshLayout.isRefreshing()) {
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    private void animationInit() {
        alphaAnimation = new AlphaAnimation(0.0f, 1.0f);
        alphaAnimation.setDuration(1000);
    }


    private void observerInit() {
        observer = chatListModel -> {
            int capacity = 10;
            int itemSumCount = viewModel.getModelMutableLiveData().getValue().size();
            int positionStart = ((currentPage * 10) - capacity);
            if (itemSumCount > 0) {
                c_listAdapter.submitList(chatListModel);
                c_listAdapter.notifyItemRangeInserted(positionStart, itemSumCount);
                loadingDialog.dismiss();
            }
        };
        viewModel.getModelMutableLiveData().observe(f_Activity, observer);
    }


    private void recyclerviewInit() {
        phItemAnimator = new PhItemAnimator(context);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(c_listAdapter);
        recyclerView.setAnimation(alphaAnimation);
//        recyclerView.setItemAnimator(phItemAnimator);
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
                            api_ChatList();
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

    public class PhItemAnimator extends SimpleItemAnimator {

        private Context mContext;

        public PhItemAnimator(Context a_context) {

            mContext = a_context;
        }

        @Override
        public boolean animateRemove(RecyclerView.ViewHolder a_holder) {
            return false;
        }

        @Override
        public boolean animateAdd(RecyclerView.ViewHolder a_holder) {
            Animator animator = AnimatorInflater.loadAnimator(mContext, R.animator.zoom_in);
            animator.setInterpolator(new BounceInterpolator());
            animator.setTarget(a_holder.itemView);
            animator.start();
            return true;
        }

        @Override
        public boolean animateMove(RecyclerView.ViewHolder a_holder, int a_fromX, int a_fromY, int a_toX, int a_toY) {
            return false;
        }

        @Override
        public boolean animateChange(RecyclerView.ViewHolder a_oldHolder, RecyclerView.ViewHolder a_newHolder, int a_fromLeft, int a_fromTop, int a_toLeft, int a_toTop) {
            return false;
        }

        @Override
        public void runPendingAnimations() {

        }

        @Override
        public void endAnimation(RecyclerView.ViewHolder a_item) {

        }

        @Override
        public void endAnimations() {

        }

        @Override
        public boolean isRunning() {
            return false;
        }
    }


}
