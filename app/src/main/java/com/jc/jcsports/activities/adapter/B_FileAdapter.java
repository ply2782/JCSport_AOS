package com.jc.jcsports.activities.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.ui.StyledPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.jc.jcsports.R;
import com.jc.jcsports.databinding.ItemBFileListBinding;
import com.jc.jcsports.model.FileModel;
import com.jc.jcsports.utils.ServerURL;
import com.jc.jcsports.utils.Utils;

import org.jetbrains.annotations.NotNull;

import java.io.BufferedInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class B_FileAdapter extends ListAdapter<FileModel, RecyclerView.ViewHolder> {

    private Context context;


    public B_FileAdapter(@NonNull DiffUtil.ItemCallback<FileModel> diffCallback, Context context) {
        super(diffCallback);
        this.context = context;
    }

    @Override
    public int getItemViewType(int position) {
        return R.layout.item_b_file_list;
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemBFileListBinding binding = ItemBFileListBinding.inflate(inflater, parent, false);
        return new ItemBFileListBindingAdapterViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ItemBFileListBindingAdapterViewHolder) {
            ItemBFileListBindingAdapterViewHolder adapterViewHolder = (ItemBFileListBindingAdapterViewHolder) holder;
            adapterViewHolder.onBind(getItem(position));
        }
    }

    @Override
    public void onViewRecycled(@NonNull RecyclerView.ViewHolder holder) {
        super.onViewRecycled(holder);

    }

    public class ItemBFileListBindingAdapterViewHolder extends RecyclerView.ViewHolder {
        private ItemBFileListBinding binding;
        public StyledPlayerView styledPlayerView;
        public ImageView imageView;

        public ItemBFileListBindingAdapterViewHolder(@NonNull @NotNull ItemBFileListBinding binding_Copy) {
            super(binding_Copy.getRoot());
            binding = binding_Copy;
            binding.setViewHolder(this);
            ViewGroup.LayoutParams lp = binding.fContentView.getLayoutParams();
            playerViewInit(lp);
            imageViewInit(lp);
        }
        private void playerViewInit(ViewGroup.LayoutParams lp){
            styledPlayerView = new StyledPlayerView(context);
            styledPlayerView.onPause();
            styledPlayerView.setLayoutParams(lp);
            styledPlayerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FILL);
            styledPlayerView.setUseArtwork(true);
            styledPlayerView.setUseController(false);
        }

        private void imageViewInit(ViewGroup.LayoutParams lp){
            imageView = new ImageView(context);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setLayoutParams(lp);
        }

        public void onBind(FileModel f_Model) {
            boolean isDelete = f_Model.f_isDelete;
            if (!isDelete) {
                String f_Name = f_Model.f_Name;
                if (f_Model.f_Type.equals("video")) {
                    binding.fContentView.addView(styledPlayerView);
                } else {
                    String f_Url = ServerURL.IMAGE_URL + f_Name;
                    Utils.glide(imageView.getContext())
                            .load(f_Url)
                            .optionalCenterCrop()
                            .sizeMultiplier(0.7f)
                            .into(imageView);
                    binding.fContentView.addView(imageView);
                }
            }
            binding.executePendingBindings();
        }
    }
}
