package com.jc.jcsports.activities.chatFunctions.chatRoom;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.EditText;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jc.jcsports.R;
import com.jc.jcsports.activities.adapter.C_ChattingAdapter;
import com.jc.jcsports.bundle.SignUpBundle;
import com.jc.jcsports.model.LoginModel;
import com.jc.jcsports.model.chat.ChattingModel;
import com.jc.jcsports.utils.LoadingDialog;
import com.jc.jcsports.utils.RetrofitService;
import com.jc.jcsports.utils.ServerURL;
import com.jc.jcsports.utils.Utils;
import com.jc.jcsports.utils.ViewModelFactory;
import com.jc.jcsports.viewModel.ChattingViewModel;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ThreadPoolExecutor;

import io.reactivex.CompletableTransformer;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.SingleObserver;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.internal.Util;
import ua.naiksoftware.stomp.Stomp;
import ua.naiksoftware.stomp.StompClient;
import ua.naiksoftware.stomp.dto.StompHeader;

public class ChatRoomFunctions {
    private Type chatModelType;
    private Context context;
    private RetrofitService service;
    private StompClient stompClient;
    private ThreadPoolExecutor threadPoolExecutor;

    private C_ChattingAdapter adapter;
    private ChattingViewModel chattingViewModel;
    private ChatRoomActivity c_Activity;
    private RecyclerView recyclerView;
    private int c_Number;
    private Gson gson;
    private LoginModel loginModel;
    private Observer<List<ChattingModel>> chattingModelObserver;
    private AlphaAnimation alphaAnimation;
    private EditText textInputEditText;
    private int currentPage = 1;
    private Thread t_1, t_2;
    private SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private Timer timer;
    private LoadingDialog loadingDialog;
    private Handler handler;

    //todo : 로딩 다이얼로그
    private void loadingDialogInit() {
        loadingDialog = new LoadingDialog(context);
        loadingDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
    }

    public void setTextInputEditText(EditText textInputEditText) {
        this.textInputEditText = textInputEditText;
    }

    public void setC_Number(int c_Number) {
        this.c_Number = c_Number;
    }

    public void setC_Activity(ChatRoomActivity c_Activity) {
        this.c_Activity = c_Activity;
        viewModelInit();
        observerInit();
        t_1 = new Thread(runnable_1());
        t_2 = new Thread(runnable_2());
    }

    public void setThreadPoolExecutor(ThreadPoolExecutor threadPoolExecutor) {
        this.threadPoolExecutor = threadPoolExecutor;

    }

    public void setContext(Context context) {
        this.context = context;
        loadingDialogInit();

    }

    public void setRecyclerView(RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
        adapterInit();
        try {
            t_1.start();
            t_1.join();
            t_1.interrupt();
            t_2.start();
            t_2.join();
            t_2.interrupt();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void setService(RetrofitService service) {
        this.service = service;
    }

    public ChatRoomFunctions() {
        animationInit();
        myModelInit();
        utilsInit();
        handlerInit();
    }

    private void handlerInit() {
        handler = new Handler(Looper.getMainLooper());
    }

    public void handlerRemove() {
        handler.removeCallbacksAndMessages(null);
    }

    private void timerInit() {
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                scrollToBottom();
                timer.cancel();
            }
        }, 100);
    }

    private void utilsInit() {
        gson = new Gson();
        chatModelType = new TypeToken<ChattingModel>() {
        }.getType();
    }

    private void myModelInit() {

        loginModel = (LoginModel) SignUpBundle.getBundle().getSerializable("loginModel");

    }

    private void viewModelInit() {
        ViewModelFactory viewModelFactory = new ViewModelFactory();
        chattingViewModel = new ViewModelProvider(c_Activity, viewModelFactory).get(ChattingViewModel.class);
        chattingViewModel.init();
    }


    private void animationInit() {
        alphaAnimation = new AlphaAnimation(0.0f, 1.0f);
        alphaAnimation.setDuration(1000);
    }

    private void initChat() {
        service.chattingList(ServerURL.chatController, currentPage, c_Number, loginModel.s_Number)
                .subscribeOn(io.reactivex.rxjava3.schedulers.Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onSuccess(@NonNull List<ChattingModel> chatListModels) {
                        if (chatListModels.size() > 0) {
                            c_Activity.runOnUiThread(() -> {
                                loadingDialog.show();
                            });
                            chattingViewModel.addAllItem(chatListModels);
                        }

                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        c_Activity.runOnUiThread(() -> {
                            if (loadingDialog.isShowing()) {
                                loadingDialog.dismiss();
                            }
                        });

                    }
                });
    }

    private Runnable runnable_1() {
        Runnable runnable = () -> initChat();
        return runnable;
    }

    private Runnable runnable_2() {
        Runnable runnable = () -> connectionSocket();
        return runnable;
    }


    public int getPxFromDp(float dp) {
        int px = 0;
        px = (int) (dp * context.getResources().getDisplayMetrics().density);
        return px;
    }

    private void adapterInit() {
        adapter = new C_ChattingAdapter(context);
        recyclerView.setAdapter(adapter);
        recyclerView.setAnimation(alphaAnimation);
        RecyclerView.ItemAnimator itemAnimator = new SimpleItemAnimator() {
            @Override
            public boolean animateRemove(RecyclerView.ViewHolder holder) {
                return false;
            }

            @Override
            public boolean animateAdd(RecyclerView.ViewHolder holder) {
                Animator animator = AnimatorInflater.loadAnimator(context, R.animator.animator_chat_item);
                animator.setTarget(holder.itemView);
                animator.start();
                return true;
            }

            @Override
            public boolean animateMove(RecyclerView.ViewHolder holder, int fromX, int fromY, int toX, int toY) {
                return false;
            }

            @Override
            public boolean animateChange(RecyclerView.ViewHolder oldHolder, RecyclerView.ViewHolder newHolder, int fromLeft, int fromTop, int toLeft, int toTop) {
                return false;
            }

            @Override
            public void runPendingAnimations() {

            }

            @Override
            public void endAnimation(@androidx.annotation.NonNull RecyclerView.ViewHolder item) {

            }

            @Override
            public void endAnimations() {

            }

            @Override
            public boolean isRunning() {
                return false;
            }
        };
        recyclerView.setItemAnimator(itemAnimator);
        recyclerView.addItemDecoration(new RecyclerViewDecoration(getPxFromDp(10)));
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                /*if (!recyclerView.canScrollVertically(1)) {
                    if (newState == 0) {
                        int lastPosition =
                                ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastCompletelyVisibleItemPosition();
                        int totalCount = recyclerView.getAdapter().getItemCount();
                        int capacity = 20;
                        boolean isEnd = !recyclerView.canScrollVertically(1);
                        if ((lastPosition == totalCount - 1) && isEnd && (totalCount % capacity == 0)) {
                            currentPage++;
                            initChat();

                        }
                    }
                }*/
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy < 0) { // 스크롤이 위로 올라가면서 새로운 아이템을 로드해야 하는 경우
                    if (recyclerView.canScrollVertically(1)) {
                        int lastPosition =
                                ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastCompletelyVisibleItemPosition();
                        int totalCount = recyclerView.getAdapter().getItemCount();
                        int capacity = 20;
                        if ((lastPosition == totalCount - 1)) {
                            currentPage++;
                            initChat();
                        }
                    }
                }
            }
        });
    }


    public void stompClose() {
        stompClient.disconnect();
    }


    private void observerInit() {
        chattingModelObserver = chattingModels -> {
            int itemCount = chattingModels.size();
            if (itemCount > 0) {
                adapter.setC_New(chattingModels);
                c_Activity.runOnUiThread(() -> {
                    if (loadingDialog.isShowing()) {
                        loadingDialog.dismiss();
                    }
                });
            }
        };
        chattingViewModel.getModelMutableLiveData().observe(c_Activity, chattingModelObserver);
    }


    public void sendMessage() {
        ChattingModel chattingModel = new ChattingModel();
        chattingModel.c_Number = c_Number;
        chattingModel.s_Number = loginModel.s_Number;
        LoginModel l_Model = new LoginModel();
        l_Model.s_NickName = loginModel.s_NickName;

        l_Model.s_Number = loginModel.s_Number;
        l_Model.s_SocialToken = loginModel.s_SocialToken;
        l_Model.s_Time = loginModel.s_Time;
        l_Model.s_FirebaseToken = loginModel.s_FirebaseToken;
        l_Model.s_Gender = loginModel.s_Gender;
        l_Model.s_Birth = loginModel.s_Birth;
        l_Model.s_IsLogin = loginModel.s_IsLogin;
        l_Model.s_AgeRange = loginModel.s_AgeRange;
        chattingModel.s_LoginModel = l_Model;
        chattingModel.c_MessageTime = timeFormat.format(new Date());
        chattingModel.c_MessageType = ChattingModel.MessageType.MESSAGE;
        chattingModel.c_Message = textInputEditText.getText().toString();
        stompClient.send("/pub/sendMessage", gson.toJson(chattingModel))
                .compose(applySchedulers())
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.from(threadPoolExecutor))
                .subscribe(() -> {
                    Utils.logCheck("REST echo send successfully");
                    timerInit();

                }, throwable -> {
                    Utils.logCheck("Error send REST echo => " + throwable);
                });
        textInputEditText.getText().clear();
    }

    private void scrollToBottom() {
        recyclerView.smoothScrollToPosition(0);
    }

    public void enterMessage() {
        ChattingModel chattingModel = new ChattingModel();
        chattingModel.c_Number = c_Number;
        chattingModel.c_MessageType = ChattingModel.MessageType.ENTER;
        chattingModel.s_Number = loginModel.s_Number;
        chattingModel.c_MessageTime = timeFormat.format(new Date());
        LoginModel l_Model = new LoginModel();
        l_Model.s_NickName = loginModel.s_NickName;
        l_Model.s_Number = loginModel.s_Number;
        l_Model.s_SocialToken = loginModel.s_SocialToken;
        l_Model.s_Time = loginModel.s_Time;
        l_Model.s_FirebaseToken = loginModel.s_FirebaseToken;
        l_Model.s_Gender = loginModel.s_Gender;
        l_Model.s_Birth = loginModel.s_Birth;
        l_Model.s_IsLogin = loginModel.s_IsLogin;
        l_Model.s_AgeRange = loginModel.s_AgeRange;
        chattingModel.s_LoginModel = l_Model;
        stompClient.send("/pub/chat/enterUser", gson.toJson(chattingModel))
                .compose(applySchedulers())
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.from(threadPoolExecutor))
                .subscribe(() -> {
                    Utils.logCheck("REST echo send successfully");
                }, throwable -> {
                    Utils.logCheck("Error send REST echo => " + throwable);
                });
    }


    public void exitMessage() {
        ChattingModel chattingModel = new ChattingModel();
        chattingModel.c_Number = c_Number;
        chattingModel.c_MessageType = ChattingModel.MessageType.EXIT;
        chattingModel.s_Number = loginModel.s_Number;
        chattingModel.c_MessageTime = timeFormat.format(new Date());
        LoginModel l_Model = new LoginModel();
        l_Model.s_NickName = loginModel.s_NickName;
        l_Model.s_Number = loginModel.s_Number;
        l_Model.s_SocialToken = loginModel.s_SocialToken;
        l_Model.s_Time = loginModel.s_Time;
        l_Model.s_FirebaseToken = loginModel.s_FirebaseToken;
        l_Model.s_Gender = loginModel.s_Gender;
        l_Model.s_Birth = loginModel.s_Birth;
        l_Model.s_IsLogin = loginModel.s_IsLogin;
        l_Model.s_AgeRange = loginModel.s_AgeRange;
        chattingModel.s_LoginModel = l_Model;
        stompClient.send("/pub/chat/exitUser", gson.toJson(chattingModel))
                .compose(applySchedulers())
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.from(threadPoolExecutor))
                .subscribe(() -> {
                    Utils.logCheck("REST echo send successfully");
                }, throwable -> {
                    Utils.logCheck("Error send REST echo => " + throwable);
                });
    }

    //TODO : 메시지 받기
    @SuppressLint("CheckResult")
    public void receiveMessage() {
        stompClient.topic("/sub/chatRoom/" + c_Number)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.from(threadPoolExecutor))
                .subscribe(topicMessage -> {
                    ChattingModel chattingModel = gson.fromJson(topicMessage.getPayload(), chatModelType);
                    chattingViewModel.addItem(chattingModel);
                }, throwable -> {
                    Utils.logCheck("Error on subscribe topic => " + throwable);
                });
        Utils.logCheck("receiveMessage() 실행..");
    }


    protected CompletableTransformer applySchedulers() {
        return upstream -> upstream
                .unsubscribeOn(Schedulers.newThread())
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.from(threadPoolExecutor));
    }


    @SuppressLint("CheckResult")
    public void connectionSocket() {
        /*String url = "ws://ply2782ply2782.cafe24.com:8080/chatting/websocket";*/
        String url = "ws://192.168.25.35:8080/chat/websocket";
        stompClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP, url);
        List<StompHeader> headers = new ArrayList<>();
        headers.add(new StompHeader("c_Number", "" + c_Number));
        headers.add(new StompHeader("myS_Number", "" + loginModel.s_Number));
        stompClient.connect(headers);
        stompClient.lifecycle()
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.from(threadPoolExecutor))
                .subscribe(lifecycleEvent -> {
                    switch (lifecycleEvent.getType()) {
                        case OPENED:
                            Utils.logCheck("OPENED");
                            //TODO : 채팅은 리시브 부터 먼저 받아야 함
                            receiveMessage();
                            enterMessage();
                            break;
                        case ERROR:
                            Utils.logCheck("Stomp connection error => " + lifecycleEvent.getException());

                            break;
                        case CLOSED:
                            Utils.logCheck("Stomp connection closed");
                            break;
                        case FAILED_SERVER_HEARTBEAT:
                            break;
                    }
                });
        Utils.logCheck("connectionSocket() 실행..");
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
