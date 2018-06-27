package com.ghostwording.hugsapp.adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.ghostwording.hugsapp.MainActivity;
import com.ghostwording.hugsapp.R;
import com.ghostwording.hugsapp.analytics.AnalyticsHelper;
import com.ghostwording.hugsapp.databinding.ItemGifBinding;
import com.ghostwording.hugsapp.dialog.ShareDialog;
import com.ghostwording.hugsapp.dialog.ShareGifDialog;
import com.ghostwording.hugsapp.model.GifResponse;
import com.ghostwording.hugsapp.model.MediaModel;
import com.ghostwording.hugsapp.textimagepreviews.TextsRecommendationActivity;
import com.ghostwording.hugsapp.utils.PostCardRenderer;
import com.ghostwording.hugsapp.utils.Utils;
import com.ghostwording.hugsapp.utils.UtilsUI;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import pl.droidsonroids.gif.GifImageView;

public class MediaAdapter extends RecyclerView.Adapter<MediaAdapter.BindingHolder> {

    private List<MediaModel> itemsList = new ArrayList<>();
    private FragmentActivity activity;
    private int width;

    public MediaAdapter(FragmentActivity activity, List<MediaModel> gifImages) {
        this.itemsList = gifImages;
        this.activity = activity;
        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        width = displayMetrics.widthPixels;
    }

    @Override
    public BindingHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 0) {
            return new BindingHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_gif, parent, false));
        } else {
            return new BindingHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_image, parent, false));
        }
    }

    @Override
    public int getItemViewType(int position) {
        return itemsList.get(position).getGifImage() != null ? 0 : 1;
    }

    @Override
    public void onBindViewHolder(BindingHolder holder, final int position) {
        final MediaModel mediaModel = itemsList.get(position);
        if (mediaModel.getGifImage() != null) {
            GifResponse.GifImage gifImage = mediaModel.getGifImage();
            final String gifImageUrl = gifImage.getImages().getFixedHeight().getUrl();
            double heightCoef = width / (Integer.parseInt(gifImage.getImages().getFixedHeight().getWidth()) * 1.0);
            holder.ivImage.getLayoutParams().height = (int) (Integer.parseInt(gifImage.getImages().getFixedHeight().getHeight()) * heightCoef);
            holder.progressBar.setVisibility(View.VISIBLE);
            Glide.with(activity).load(gifImageUrl)
                    .downloadOnly(new SimpleTarget<File>() {
                        @Override
                        public void onResourceReady(File resource, GlideAnimation<? super File> glideAnimation) {
                            holder.progressBar.setVisibility(View.GONE);
                            holder.ivImage.setImageURI(Uri.parse("file://" + resource.getAbsolutePath()));
                        }
                    });

            holder.ivImage.setOnClickListener(view -> onSendClicked(gifImageUrl, gifImage.getId()));
            holder.btnSend.setOnClickListener(view -> onSendClicked(gifImageUrl, gifImage.getId()));
        } else {
            Glide.with(activity).load(mediaModel.getImageUrl())
                    .centerCrop().crossFade().into(holder.ivImage);
            holder.ivImage.setOnClickListener(view -> onSendImage(mediaModel.getImageUrl()));
            holder.btnSend.setOnClickListener(view -> onSendImage(mediaModel.getImageUrl()));
        }
    }

    private void onSendImage(String imageUrl) {
        Intent intent = new Intent(activity, TextsRecommendationActivity.class);
        intent.putExtra(TextsRecommendationActivity.IMAGE_URL, imageUrl);
        activity.startActivity(intent);
    }

    private void onSendClicked(String imageUrl, String gifId) {
        final ProgressDialog progressDialog = UtilsUI.showProgressDialog(activity);
        progressDialog.setMessage(activity.getString(R.string.processing_image));
        PostCardRenderer.requestDownloadFile(activity, imageUrl, Utils.GIF_FILENAME).subscribe(success -> {
            progressDialog.dismiss();
            if (success) {
                AnalyticsHelper.sendEvent(AnalyticsHelper.Events.GIF_SEND, gifId);
                ShareGifDialog.show(activity, PostCardRenderer.getShareGifUri(activity).toString());
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemsList.size();
    }

    public static class BindingHolder extends RecyclerView.ViewHolder {
        private ImageView ivImage;
        private View btnSend;
        private ProgressBar progressBar;

        public BindingHolder(View rowView) {
            super(rowView);
            progressBar = rowView.findViewById(R.id.progress_bar);
            ivImage = rowView.findViewById(R.id.iv_image);
            btnSend = rowView.findViewById(R.id.btn_send);
        }
    }

}

