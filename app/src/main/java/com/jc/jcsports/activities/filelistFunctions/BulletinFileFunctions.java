package com.jc.jcsports.activities.filelistFunctions;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.gson.Gson;
import com.jc.jcsports.R;
import com.jc.jcsports.activities.adapter.FileModelAdapter;

import com.jc.jcsports.activities.diffUtils.BulletinModelDiffUtil;
import com.jc.jcsports.model.BulletinModel;
import com.jc.jcsports.model.FileModel;
import com.jc.jcsports.utils.LoadingDialog;
import com.jc.jcsports.utils.RetrofitClient;
import com.jc.jcsports.utils.RetrofitService;
import com.jc.jcsports.utils.ServerURL;
import com.jc.jcsports.utils.Utils;
import com.jc.jcsports.utils.ViewModelFactory;
import com.jc.jcsports.viewModel.BulletinViewModel;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.SingleObserver;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class BulletinFileFunctions {

    private FileModelAdapter fileModelAdapter;
    private AlphaAnimation alphaAnimation;
    private RetrofitService service;
    private RetrofitClient retrofitClient;
    private BulletinViewModel bulletinViewModel;
    private Observer<List<BulletinModel>> observer;
    private Gson gson;
    public int currentPage = 1;
    private Context context;
    private FragmentActivity fragmentActivity;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private LoadingDialog loadingDialog;

    public BulletinFileFunctions(FragmentActivity fragmentActivity,
                                 Context context, RecyclerView recyclerView,
                                 SwipeRefreshLayout swipeRefreshLayout
    ) {

        this.fragmentActivity = fragmentActivity;
        this.swipeRefreshLayout = swipeRefreshLayout;
        this.recyclerView = recyclerView;
        this.context = context;
        loadingDialogInit();
        viewModelInit();
        swipeRefreshLayoutInit();
        animationInit();
        adapterInit(context);
        recyclerviewInit();
        retroFitInit();
        observerInit();
        api_getFullItems();
        gsonInit();
    }


    //todo : 로딩 다이얼로그
    private void loadingDialogInit() {
        loadingDialog = new LoadingDialog(context);
        loadingDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
    }


    private void adapterInit(Context context) {
        fileModelAdapter = new FileModelAdapter(new BulletinModelDiffUtil(), context);
    }

    private void gsonInit() {
        gson = new Gson();
    }


    private void swipeRefreshLayoutInit() {
        swipeRefreshLayout.setOnRefreshListener(() -> {
            loadingDialog.show();
            api_getReFreshFullItems();
            if (swipeRefreshLayout.isRefreshing()) {
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    public void api_Modify() {
        service.get_ChangedB_Items(ServerURL.bulletinController, currentPage)
                .subscribeOn(Schedulers.single())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        Utils.logCheck("Disposable d");
                    }

                    @Override
                    public void onSuccess(@NonNull List<BulletinModel> bulletinModels) {
                        int itemSumCount = bulletinViewModel.getFileModelMutableLiveData().getValue().size();
                        if (itemSumCount > 0) {
                            fileModelAdapter.submitList(bulletinModels);
                            fileModelAdapter.notifyItemRangeChanged(0, itemSumCount);
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Utils.logCheck("Throwable => " + e.getMessage());

                    }
                });

    }

    private void animationInit() {
        alphaAnimation = new AlphaAnimation(0.0f, 1.0f);
        alphaAnimation.setDuration(1000);
    }

    private void recyclerviewInit() {
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(fileModelAdapter);
        recyclerView.setAnimation(alphaAnimation);
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
                            Utils.logCheck("paging is started...");
                            currentPage++;
                            api_getFullItems();
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

    //todo : RestApi init
    private void retroFitInit() {
        retrofitClient = RetrofitClient.getInstance();
        service = RetrofitClient.getServerInterface();
    }

    private void viewModelInit() {
        ViewModelFactory viewModelFactory = new ViewModelFactory();
        bulletinViewModel = new ViewModelProvider(fragmentActivity, viewModelFactory).get(BulletinViewModel.class);
        bulletinViewModel.init();
    }


    private void observerInit() {
        observer = thumbInfoModels -> {
            int capacity = 10;
            int itemSumCount = bulletinViewModel.getFileModelMutableLiveData().getValue().size();
            int positionStart = ((currentPage * 10) - capacity);
            if (itemSumCount > 0) {
                fileModelAdapter.submitList(thumbInfoModels);
                fileModelAdapter.notifyItemRangeInserted(positionStart, itemSumCount);
            }
            Utils.logCheck("observer  ");
            loadingDialog.dismiss();
        };
        bulletinViewModel.getFileModelMutableLiveData().observe(fragmentActivity, observer);
    }


    public void api_getReFreshFullItems() {
        currentPage = 1;
        service.getB_AllItems(ServerURL.bulletinController, currentPage)
                .subscribeOn(Schedulers.single())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        Utils.logCheck("Disposable d");
                    }

                    @Override
                    public void onSuccess(@NonNull List<BulletinModel> thumbInfoModelList) {
                        int itemCount = fileModelAdapter.getItemCount();
                        bulletinViewModel.removeFileModelList();
                        if (thumbInfoModelList.size() > 0) {
                            fileModelAdapter.notifyItemRangeRemoved(0, itemCount);
                            bulletinViewModel.setFileModelList(thumbInfoModelList);
                        }else{
                            loadingDialog.dismiss();
                        }
                        Utils.logCheck("itemCount => "+ itemCount);

                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Utils.logCheck("Throwable => " + e.getMessage());
                        loadingDialog.dismiss();

                    }
                });
    }

    public void api_getFullItems() {
        service.getB_AllItems(ServerURL.bulletinController, currentPage)
                .subscribeOn(Schedulers.single())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        Utils.logCheck("Disposable d");
                    }

                    @Override
                    public void onSuccess(@NonNull List<BulletinModel> bulletinModels) {
                        if (bulletinModels.size() > 0) {
                            loadingDialog.show();
                            bulletinViewModel.addFileModelList(bulletinModels);
                        } else {
                            loadingDialog.dismiss();
                        }

                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Utils.logCheck("Throwable => " + e.getMessage());

                    }
                });
    }
}
