package com.ghostwording.chatbot.utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.ghostwording.chatbot.widget.RoundedCornersTransformation;
import com.google.android.material.snackbar.Snackbar;

import java.io.File;

import androidx.annotation.StringRes;
import pl.droidsonroids.gif.GifImageView;

public class UtilsUI {

    public static ProgressDialog createProgressDialog(Activity activity, @StringRes int message, boolean isShow) {
        ProgressDialog progressDialog = new ProgressDialog(activity);
        progressDialog.setMessage(activity.getString(message));
        if (isShow) {
            progressDialog.show();
        }
        return progressDialog;
    }

    public static void showErrorInSnackBar(Activity activity, String errorMessage) {
        if (activity == null) return;
        Snackbar.make(activity.findViewById(android.R.id.content).getRootView(),
                errorMessage,
                Snackbar.LENGTH_SHORT).show();
    }

    public static ProgressDialog showProgressDialog(Context context) {
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);
        progressDialog.show();
        return progressDialog;
    }

    public static void loadImage(ImageView imageView, String imageUrl) {
        try {
            Glide.with(imageView.getContext()).load(imageUrl).crossFade().into(imageView);
        } catch (Exception ex) {
            Logger.e(ex.toString());
        }
    }

    public static void loadImageCacheSource(ImageView imageView, String imageUrl) {
        try {
            Glide.with(imageView.getContext()).load(imageUrl).diskCacheStrategy(DiskCacheStrategy.SOURCE).crossFade().into(imageView);
        } catch (Exception ex) {
            Logger.e(ex.toString());
        }
    }

    public static void loadImageCenterCrop(ImageView imageView, String imageUrl) {
        try {
            Glide.with(imageView.getContext()).load(imageUrl).centerCrop().into(imageView);
        } catch (Exception ex) {
            Logger.e(ex.toString());
        }
    }

    public static void loadImageRoundedCorners(ImageView imageView, String imageUrl) {
        try {
            Glide.with(imageView.getContext())
                    .load(imageUrl)
                    .bitmapTransform(new CenterCrop(imageView.getContext()), new RoundedCornersTransformation(imageView.getContext(), 13, 0, RoundedCornersTransformation.CornerType.TOP))
                    .into(imageView);
        } catch (Exception ex) {
            Logger.e(ex.toString());
        }
    }

    public static void loadGifImage(GifImageView imageView, String imageUrl, ProgressBar progressBar) {
        try {
            if (progressBar != null) {
                progressBar.setVisibility(View.VISIBLE);
            }
            imageView.setImageBitmap(null);
            Glide.with(imageView.getContext()).load(imageUrl)
                    .downloadOnly(new SimpleTarget<File>() {
                        @Override
                        public void onResourceReady(File resource, GlideAnimation<? super File> glideAnimation) {
                            if (progressBar != null) {
                                progressBar.setVisibility(View.GONE);
                            }
                            imageView.setImageURI(Uri.parse("file://" + resource.getAbsolutePath()));
                        }
                    });
        } catch (Exception ex) {
            Logger.e(ex.toString());
        }
    }


}
