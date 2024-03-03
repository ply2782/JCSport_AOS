package com.jc.jcsports.utils.videotrim;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.view.View;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;

import com.jc.jcsports.R;
import com.jc.jcsports.databinding.ActivityVideoEditBinding;
import com.jc.jcsports.utils.AbstractActivity;
import com.jc.jcsports.utils.LoadingDialog;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class VideoEditActivity extends AbstractActivity {


    private ActivityVideoEditBinding binding;
    private MediaMetadataRetriever retriever;
    private NDK ndk;
    private String filePath;
    private LoadingDialog loadingDialog;


    @Override
    public void basicSetting() {
        binding = DataBindingUtil.setContentView(VideoEditActivity.this, R.layout.activity_video_edit);
        binding.setLifecycleOwner(VideoEditActivity.this);
        binding.setThisActivity(this);
        filePath = getIntent().getStringExtra("filePath");
        Uri videoUri = Uri.parse(filePath);
        retrieverInit(videoUri);
        loadingDialogInit();

    }

    //todo : 로딩 다이얼로그
    private void loadingDialogInit() {
        loadingDialog = new LoadingDialog(context);
        loadingDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
    }

    private void videoViewInit(Uri videoUri) {
        binding.videoView.setVideoURI(videoUri);
        binding.videoView.seekTo(1);
        binding.videoView.pause();
        binding.videoView.setOnClickListener(v -> {
            if (binding.videoView.isPlaying()) {
                binding.videoView.pause();
            } else {
                binding.videoView.start();
            }
        });
        binding.timeLineView.setVideo(videoUri);
        retriever.setDataSource(String.valueOf(videoUri));
    }

    private void retrieverInit(Uri videoUri) {
        retriever = new MediaMetadataRetriever();
        retriever.setDataSource(context, videoUri);
        String durationStr = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
        long durationMs = Long.parseLong(durationStr);
        int durationSec = (int) (durationMs / 1000);
        rangeSliderInit(durationSec);
        videoViewInit(videoUri);

    }

    private void rangeSliderInit(int maxDuration) {
        int startDuration = 0, stepDuration = 1, minStepDuration = 1, maxDiff = 30;
        binding.rangeSlider.setValueFrom(startDuration);
        binding.rangeSlider.setValueTo(maxDuration);
        binding.rangeSlider.setStepSize(stepDuration);
        binding.rangeSlider.setCustomThumbDrawable(R.drawable.image_up_arrow);
        binding.rangeSlider.setMinSeparationValue((float) minStepDuration);
        binding.rangeSlider.addOnChangeListener((slider, value, fromUser) -> {
            if (fromUser) {
                float left_Val = slider.getValues().get(0);
                float right_Val = slider.getValues().get(1);
                if (Math.abs((left_Val - right_Val)) > maxDiff) {
                    slider.setValues(left_Val, (left_Val + maxDiff));
                } else {
                    int seekDuration = (int) (left_Val * 1000);
                    binding.videoView.seekTo(seekDuration);
                }
            }
        });
    }

    public void cancel(View v) {
        finish();
    }

    public void compress(View v) {
        loadingDialog.show();
        String outputDir = filePath.substring(0, (filePath.lastIndexOf("/") + 1));
        final String finalOutputDir = outputDir + "compressed_" + System.currentTimeMillis() + ".mp4";
        int startMs = (binding.rangeSlider.getValues().get(0).intValue());
        int endMs = (binding.rangeSlider.getValues().get(1).intValue());
        ndk = new NDK(filePath, finalOutputDir, startMs, endMs);
//        ndk.testMethod(filePath, finalOutputDir);
        Observable.defer(() -> {
                    return Observable.fromCallable(() -> {
                                int compressResult = ndk.compressVideo();
                                if (compressResult == 0) {
                                    return ndk.makeThumbNail(finalOutputDir, outputDir);
                                } else {
                                    return null;
                                }
                            })
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .doFinally(() -> loadingDialog.dismiss());
                })
                .subscribe(result -> {
                    if (result != null) {
                        Intent intent = getIntent();
                        intent.putExtra("thumbNailFilePath", result);
                        intent.putExtra("editedFilePath", finalOutputDir);
                        setResult(RESULT_OK, intent);
                        loadingDialog.dismiss();
                        finish();
                    } else {
                        Toast.makeText(context, "파일 생성 도중 오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
                        loadingDialog.dismiss();
                        finish();
                    }
                }, error -> {
                    Toast.makeText(context, "파일 생성 도중 오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
                    loadingDialog.dismiss();
                    finish();
                });
    }
}