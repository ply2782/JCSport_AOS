package com.jc.jcsports.activities.chatFunctions.chatRoom;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;
import android.widget.EditText;

import com.jc.jcsports.R;
import com.jc.jcsports.activities.filelistFunctions.fileModifyFunctions.FileModifyActivity;
import com.jc.jcsports.databinding.ActivityChatRoomBinding;
import com.jc.jcsports.model.FileModel;
import com.jc.jcsports.utils.AbstractActivity;
import com.jc.jcsports.utils.RetrofitClient;
import com.jc.jcsports.utils.RetrofitService;
import com.jc.jcsports.utils.Utils;

import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import io.reactivex.schedulers.Schedulers;
import okhttp3.internal.Util;
import ua.naiksoftware.stomp.Stomp;
import ua.naiksoftware.stomp.StompClient;

public class ChatRoomActivity extends AbstractActivity {

    private ActivityChatRoomBinding binding;
    private RetrofitClient retrofitClient;
    private RetrofitService service;
    private ChatRoomFunctions functions;
    private ThreadPoolExecutor threadPoolExecutor;
    private Handler handler = new Handler();


    @Override
    public void basicSetting() {
        binding = DataBindingUtil.setContentView(ChatRoomActivity.this, R.layout.activity_chat_room);
        binding.setLifecycleOwner(ChatRoomActivity.this);
        binding.setThisActivity(this);
        onTouchListener();
        editTextAnimationInit();
        RetrofitInit();
        threadInit();
        functionsInit();

    }

    private void threadInit() {
        int maxCore = Runtime.getRuntime().availableProcessors();
        threadPoolExecutor = new ThreadPoolExecutor(
                1, //corePoolSize
                maxCore, //maxPoolSize
                0L, //keepAliveTime
                TimeUnit.MILLISECONDS, //unit
                new LinkedBlockingQueue<Runnable>(20), //workQueue
                new ThreadPoolExecutor.DiscardOldestPolicy() //handler
        );
    }

    private int c_Number() {
        Intent intent = getIntent();
        return intent.getIntExtra("c_Number", 0);
    }


    private void functionsInit() {
        functions = new ChatRoomFunctions();
        functions.setC_Number(c_Number());
        functions.setContext(context);
        functions.setC_Activity(this);
        functions.setService(service);
        functions.setThreadPoolExecutor(threadPoolExecutor);
        functions.setRecyclerView(binding.chatRecyclerview);
        functions.setTextInputEditText(binding.messageInputEditTextView);

    }


    public void sendMessage(View v) {
        functions.sendMessage();
        binding.send.setEnabled(false);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                binding.send.setEnabled(true); // 0.5초 후 버튼 활성화
            }
        }, 500);
    }

    private void RetrofitInit() {
        retrofitClient = RetrofitClient.getInstance();
        service = RetrofitClient.getServerInterface();
    }

    public void back(View v) {
        onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        functions.exitMessage();
        functions.stompClose();
        handler.removeCallbacksAndMessages(null);
        functions.handlerRemove();

    }


    private void editTextAnimationInit() {
        binding.messageInputEditTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count > 0) {
                    ObjectAnimator animator = ObjectAnimator.ofFloat(binding.messageInputEditTextView, "alpha", 0.5f, 1f);
                    animator.setDuration(500); // 0.5초 동안 애니메이션 적용
                    animator.start();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private void onTouchListener() {

        binding.send.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                binding.send.setPadding(0, getPxFromDp(5), 0, 0);
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                binding.send.setPadding(0, 0, 0, getPxFromDp(10));
            } else if (event.getAction() == MotionEvent.ACTION_CANCEL) {
                binding.send.setPadding(0, 0, 0, getPxFromDp(10));
            }
            return false;
        });
    }

    private int getPxFromDp(float dp) {
        int px = 0;
        px = (int) (dp * context.getResources().getDisplayMetrics().density);
        return px;
    }


}