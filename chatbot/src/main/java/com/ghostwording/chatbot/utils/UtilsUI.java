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
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.ghostwording.chatbot.R;
import com.ghostwording.chatbot.chatbot.ChatAdapter;
import com.ghostwording.chatbot.chatbot.model.BotSequence;
import com.ghostwording.chatbot.io.ApiClient;
import com.ghostwording.chatbot.io.service.PictureService;
import com.ghostwording.chatbot.model.GifResponse;
import com.ghostwording.chatbot.widget.RoundedCornersTransformation;
import com.google.android.material.snackbar.Snackbar;

import java.io.File;

import androidx.annotation.StringRes;

import pl.droidsonroids.gif.GifImageView;
import retrofit2.Call;
import retrofit2.Response;

public class UtilsUI {

    private static BotSequence.Step sChatHead;

    public static void setImageChatHead(BotSequence.Step step, ChatAdapter chatAdapter) {
        sChatHead = step;
        chatAdapter.notifyDataSetChanged();
    }

    public static void clearImageChatHead(ChatAdapter chatAdapter) {
        if (sChatHead != null) {
            sChatHead = null;
            chatAdapter.notifyDataSetChanged();
        }
    }

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

    public static void showBotAvatar(View view) {
        try {
            ImageView ivImage = view.findViewById(R.id.iv_avatar_image);
            GifImageView gifImageView = view.findViewById(R.id.iv_avatar_gif);
            ProgressBar progressBar = view.findViewById(R.id.progress_bar);
            if (ivImage != null) {
                if (sChatHead != null) {
                    if (sChatHead.getParameters().getImageParameter().getSource().equals("Giphy")) {
                        ivImage.setVisibility(View.INVISIBLE);
                        gifImageView.setVisibility(View.VISIBLE);
                        String gifUrl = String.format(AppConfiguration.GIPHY_URL_TEMPLATE, sChatHead.getParameters().getImageParameter().getPath());
                        progressBar.setVisibility(View.VISIBLE);
                        Glide.with(gifImageView.getContext()).load(gifUrl)
                                .downloadOnly(new SimpleTarget<File>() {
                                    @Override
                                    public void onResourceReady(File resource, GlideAnimation<? super File> glideAnimation) {
                                        progressBar.setVisibility(View.GONE);
                                        gifImageView.setImageURI(Uri.parse("file://" + resource.getAbsolutePath()));
                                    }
                                });
                    } else {
                        ivImage.setVisibility(View.VISIBLE);
                        gifImageView.setVisibility(View.INVISIBLE);
                        String imageUrl = sChatHead.getParameters().getImageParameter().getPath();
                        if (sChatHead.getParameters().getImageParameter().getSource().equals("Internal")) {
                            imageUrl = PictureService.HOST_URL + imageUrl;
                        }
                        progressBar.setVisibility(View.VISIBLE);
                        Glide.with(ivImage.getContext()).load(imageUrl)
                                .listener(new RequestListener<String, GlideDrawable>() {
                                    @Override
                                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                                        progressBar.setVisibility(View.GONE);
                                        return false;
                                    }

                                    @Override
                                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                        progressBar.setVisibility(View.GONE);
                                        return false;
                                    }
                                }).crossFade().into(ivImage);
                    }
                } else {
                    progressBar.setVisibility(View.GONE);
                    ivImage.setImageResource(R.drawable.ic_huggy_avatar);
                    ivImage.setVisibility(View.VISIBLE);
                    gifImageView.setVisibility(View.INVISIBLE);
                }
            }
        } catch (Exception ex) {
            Logger.e(ex.toString());
        }
    }


}
