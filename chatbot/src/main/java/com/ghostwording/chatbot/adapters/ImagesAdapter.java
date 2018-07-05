package com.ghostwording.chatbot.adapters;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.ghostwording.chatbot.R;
import com.ghostwording.chatbot.analytics.AnalyticsHelper;
import com.ghostwording.chatbot.model.PopularImages;
import com.ghostwording.chatbot.model.listeners.ImageSelectedListener;
import com.ghostwording.chatbot.textimagepreviews.TextsRecommendationActivity;
import com.ghostwording.chatbot.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class ImagesAdapter extends RecyclerView.Adapter<ImagesAdapter.BindingHolder> {

    private List<String> pictures = new ArrayList<>();
    private Activity activity;
    private ImageSelectedListener imageSelectedListener;

    public ImagesAdapter(Activity activity, List<String> pictures) {
        this.activity = activity;
        this.pictures = pictures;
    }

    public ImagesAdapter(List<PopularImages.Image> images, Activity activity) {
        this.activity = activity;
        for (PopularImages.Image image : images) {
            pictures.add(image.getImageLink());
        }
    }

    public ImagesAdapter(Activity activity, List<String> pictures, ImageSelectedListener imageSelectedListener) {
        this(activity, pictures);
        this.imageSelectedListener = imageSelectedListener;
    }

    public void setImageSelectedListener(ImageSelectedListener imageSelectedListener) {
        this.imageSelectedListener = imageSelectedListener;
    }

    @Override
    public ImagesAdapter.BindingHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ImagesAdapter.BindingHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sticker_image, parent, false));
    }

    @Override
    public void onBindViewHolder(final BindingHolder holder, final int position) {
        final String imageUrl = pictures.get(position);
        if (imageUrl != null) {
            Glide.with(holder.ivImage.getContext()).load(imageUrl).diskCacheStrategy(DiskCacheStrategy.SOURCE).crossFade().into(holder.ivImage);
            holder.ivImage.setOnClickListener(v -> {
                AnalyticsHelper.sendEvent(AnalyticsHelper.Categories.IMAGE, AnalyticsHelper.Events.IMAGE_SELECTED, Utils.getFilenameFromUri(imageUrl));
                if (imageSelectedListener != null) {
                    imageSelectedListener.onImageSelected(imageUrl, null);
                } else {
                    Intent intent = new Intent(activity, TextsRecommendationActivity.class);
                    intent.putExtra(TextsRecommendationActivity.IMAGE_URL, imageUrl);
                    activity.startActivity(intent);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return pictures.size();
    }

    public static class BindingHolder extends RecyclerView.ViewHolder {
        private ImageView ivImage;
        private View rowView;

        public BindingHolder(View rowView) {
            super(rowView);
            this.rowView = rowView;
            ivImage = rowView.findViewById(R.id.iv_image);
        }
    }

}

