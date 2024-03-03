package com.jc.jcsports.utils.videotrim;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.LongSparseArray;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.jc.jcsports.R;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class TimeLineView extends View {

    private Uri mVideoUri;
    private int mHeightView;
    private LongSparseArray<Bitmap> mBitmapList = null;
    private Handler handler = new Handler();
    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    public TimeLineView(Context context) {
        super(context);
    }

    public TimeLineView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);

    }

    public TimeLineView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mHeightView = getContext().getResources().getDimensionPixelOffset(R.dimen.frames_video_height);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        final int minW = getPaddingLeft() + getPaddingRight() + getSuggestedMinimumWidth();
        int w = resolveSizeAndState(minW, widthMeasureSpec, 1);

        final int minH = getPaddingBottom() + getPaddingTop() + mHeightView;
        int h = resolveSizeAndState(minH, heightMeasureSpec, 1);

        setMeasuredDimension(w, h);
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);

        if (mBitmapList != null) {
            canvas.save();
            int x = 0;

            for (int i = 0; i < mBitmapList.size(); i++) {
                Bitmap bitmap = mBitmapList.get(i);

                if (bitmap != null) {
                    canvas.drawBitmap(bitmap, x, 0, null);
                    x = x + bitmap.getWidth();
                }
            }
        }
    }

    public void setVideo(@NonNull Uri data) {
        mVideoUri = data;
    }

    @Override
    protected void onSizeChanged(final int w, int h, final int oldW, int oldH) {
        super.onSizeChanged(w, h, oldW, oldH);
        if (w != oldW) {
            getBitmap(w);
        }
    }


    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        // Dispose the composite disposable to avoid memory leaks
        if (mCompositeDisposable != null) {
            mCompositeDisposable.dispose();
            mCompositeDisposable = null;
        }
    }

    private int getPxFromDp(float dp) {
        int px = 0;
        px = (int) (dp * getResources().getDisplayMetrics().density);
        return px;
    }

    private void getBitmap(final int viewWidth) {
        mCompositeDisposable.add(
                Observable.fromCallable(() -> {
                            LongSparseArray<Bitmap> thumbnailList = new LongSparseArray<>();

                            MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
                            mediaMetadataRetriever.setDataSource(getContext(), mVideoUri);

                            long videoLengthInMs = Integer.parseInt(mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)) * 1000;

                            int numThumbs = (int) Math.ceil(((float) viewWidth) / mHeightView);
                            final int thumbWidth = viewWidth / numThumbs;
                            final int thumbHeight = mHeightView;

                            final long interval = videoLengthInMs / numThumbs;


                            for (int i = 0; i < numThumbs; ++i) {
                                Bitmap bitmap = mediaMetadataRetriever.getFrameAtTime((i * interval), MediaMetadataRetriever.OPTION_CLOSEST_SYNC);
                                // TODO: bitmap might be null here, hence throwing NullPointerException. You were right
                                try {
                                    bitmap = Bitmap.createScaledBitmap(bitmap, thumbWidth, thumbHeight, false);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                thumbnailList.put(i, bitmap);
                            }

                            mediaMetadataRetriever.release();

                            return thumbnailList;
                        })
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(this::returnBitmaps, Throwable::printStackTrace)
        );
    }


    private void returnBitmaps(final LongSparseArray<Bitmap> thumbnailList) {
        handler.postDelayed(() -> {
            mBitmapList = thumbnailList;
            invalidate();
        }, 0L);
    }
}



