package com.ghostwording.chatbot.utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.StringRes;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.ghostwording.chatbot.R;
import com.ghostwording.chatbot.chatbot.ChatAdapter;
import com.ghostwording.chatbot.chatbot.model.BotSequence;
import com.ghostwording.chatbot.io.service.PictureService;
import com.ghostwording.chatbot.widget.RoundedCornersTransformation;
import com.google.android.material.snackbar.Snackbar;

import java.io.File;

import pl.droidsonroids.gif.GifImageView;

public class UtilsUI {

    private static BotSequence.Step sChatHead;
    private static int sChatHeadPosition;

    public static void setImageChatHead(BotSequence.Step step, ChatAdapter chatAdapter) {
        sChatHead = step;
        sChatHeadPosition = chatAdapter.getItemCount() - 2;
        chatAdapter.notifyItemChanged(sChatHeadPosition);
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

    public static void showBotAvatar(View view, int position, Integer avatarImage) {
        try {
            ImageView ivImage = view.findViewById(R.id.iv_avatar_image);
            if (ivImage != null) {
                if (sChatHead != null && position == sChatHeadPosition) {
                    if (sChatHead.getParameters().getImageParameter().getSource().equals("Giphy")) {
                        String gifUrl = String.format(AppConfiguration.GIPHY_URL_TEMPLATE, sChatHead.getParameters().getImageParameter().getPath());
                        Glide.with(ivImage.getContext()).load(gifUrl).into(ivImage);
                    } else {
                        String imageUrl = sChatHead.getParameters().getImageParameter().getPath();
                        if (sChatHead.getParameters().getImageParameter().getSource().equals("Internal")) {
                            imageUrl = PictureService.ALTERNATIVE_HOST_URL + imageUrl;
                        }
                        Glide.with(ivImage.getContext()).load(imageUrl).into(ivImage);
                    }
                } else {
                    ivImage.setImageResource(avatarImage);
                }
            }
        } catch (Exception ex) {
            Logger.e(ex.toString());
        }
    }


}
