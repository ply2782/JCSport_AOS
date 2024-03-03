package com.jc.jcsports.activities.filelistFunctions;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.animation.AlphaAnimation;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.jc.jcsports.activities.adapter.FileModelAdapter;
import com.jc.jcsports.activities.diffUtils.BulletinModelDiffUtil;
import com.jc.jcsports.bundle.SignUpBundle;
import com.jc.jcsports.model.BulletinModel;
import com.jc.jcsports.model.LoginModel;
import com.jc.jcsports.utils.LoadingDialog;
import com.jc.jcsports.utils.RetrofitClient;
import com.jc.jcsports.utils.RetrofitService;
import com.jc.jcsports.utils.ServerURL;
import com.jc.jcsports.utils.Utils;
import com.jc.jcsports.utils.ViewModelFactory;
import com.jc.jcsports.viewModel.MyBulletinViewModel;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.SingleObserver;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;


public class MyBulletinFunctions {

    private final String TAG = getClass().getName();
    private FileModelAdapter fileModelAdapter;
    private AlphaAnimation alphaAnimation;
    private RetrofitService service;
    private RetrofitClient retrofitClient;
    private MyBulletinViewModel myBulletinViewModel;
    private Observer<List<BulletinModel>> observer;
    private int currentPage = 1;
    private FragmentActivity fragmentActivity;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private LoginModel loginModel;
    private LoadingDialog loadingDialog;
    private Context context;

    public MyBulletinFunctions(FragmentActivity fragmentActivity,
                               Context context,
                               RecyclerView recyclerView,
                               SwipeRefreshLayout swipeRefreshLayout) {
        this.swipeRefreshLayout = swipeRefreshLayout;
        this.fragmentActivity = fragmentActivity;
        this.recyclerView = recyclerView;
        this.context = context;
        loadingDialogInit();
        myModelInit();
        viewModelInit();
        retroFitInit();
        animationInit();
        adapterInit(context);
        recyclerviewInit();
        swipeRefreshLayoutInit();
        observerInit();
        api_getFullItems();

    }

    //todo : 로딩 다이얼로그
    private void loadingDialogInit() {
        loadingDialog = new LoadingDialog(context);
        loadingDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
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
        myBulletinViewModel = new ViewModelProvider(fragmentActivity, viewModelFactory).get(MyBulletinViewModel.class);
        myBulletinViewModel.init();
    }


    private void observerInit() {
        observer = bulletinModels -> {
            int capacity = 10;
            int itemSumCount = myBulletinViewModel.getFileModelMutableLiveData().getValue().size();
            int positionStart = ((currentPage * 10) - capacity);
            if (itemSumCount > 0) {
                fileModelAdapter.submitList(bulletinModels);
                fileModelAdapter.notifyItemRangeInserted(positionStart, itemSumCount);
                loadingDialog.dismiss();
            }
        };
        myBulletinViewModel.getFileModelMutableLiveData().observe(fragmentActivity, observer);
    }


    private void adapterInit(Context context) {
        fileModelAdapter = new FileModelAdapter(new BulletinModelDiffUtil(), context);
    }

    private void animationInit() {
        alphaAnimation = new AlphaAnimation(0.0f, 1.0f);
        alphaAnimation.setDuration(1000);
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
                        int itemSumCount = myBulletinViewModel.getFileModelMutableLiveData().getValue().size();
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


    public void api_getReFreshFullItems() {
        int s_Number = loginModel.s_Number;
        currentPage = 1;
        service.getB_MyItems(ServerURL.bulletinController, s_Number, currentPage)
                .subscribeOn(Schedulers.single())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        Utils.logCheck("Disposable d");
                    }

                    @Override
                    public void onSuccess(@NonNull List<BulletinModel> bulletinModels) {
                        int itemCount = fileModelAdapter.getItemCount();
                        myBulletinViewModel.removeFileModelList();
                        if (bulletinModels.size() > 0) {
                            fileModelAdapter.notifyItemRangeRemoved(0, itemCount);
                            myBulletinViewModel.setFileModelList(bulletinModels);
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Utils.logCheck("Throwable => " + e.getMessage());
                    }
                });
    }

    private void api_getFullItems() {
        int s_Number = loginModel.s_Number;
        loadingDialog.show();
        service.getB_MyItems(ServerURL.bulletinController, s_Number, currentPage)
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
                            myBulletinViewModel.addFileModelList(bulletinModels);
                        }else{
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
