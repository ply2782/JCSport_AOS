package com.jc.jcsports.binding;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.text.InputFilter;
import android.text.Spanned;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.databinding.BindingAdapter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.jc.jcsports.utils.ServerURL;
import com.jc.jcsports.utils.Utils;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Pattern;

public class BindingClass {

    @BindingAdapter({"imageLoad"})
    public static void imageLoad(ImageView imageView, String imageUrl) {
        if (imageUrl != null) {
            Utils.glide(imageView.getContext())
                    .load(imageUrl)
                    .optionalCenterCrop()
                    .sizeMultiplier(0.7f)
                    .into(imageView);
        } else {
            imageView.setImageDrawable(null);
        }
    }

    @BindingAdapter({"imageLoad"})
    public static void imageLoad(ShapeableImageView shapeableImageView, String imageUrl) {
        if (imageUrl != null) {
            Utils.glide(shapeableImageView.getContext())
                    .load(imageUrl)
                    .optionalCenterCrop()
                    .sizeMultiplier(0.7f)
                    .into(shapeableImageView);
        } else {
            shapeableImageView.setImageDrawable(null);
        }
    }


    @BindingAdapter({"imageLoad"})
    public static void imageLoad(ImageButton imageButton, String imageUrl) {
        if (imageUrl != null) {

            Utils.glide(imageButton.getContext())
                    .load(imageUrl)
                    .optionalCenterCrop()
                    .sizeMultiplier(0.7f)
                    .into(imageButton);
        } else {
            imageButton.setImageDrawable(null);
        }
    }

}
