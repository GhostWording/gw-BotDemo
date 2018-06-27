package com.ghostwording.hugsapp.utils;

import android.databinding.BindingAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.ghostwording.hugsapp.widget.RoundedCornersTransformation;

public class BindingAdapters {

    @BindingAdapter({"bind:roundedImageUrl"})
    public static void loadImageWithRoundCorners(ImageView imageView, String imageUrl) {
        Glide.with(imageView.getContext())
                .load(imageUrl)
                .bitmapTransform(new CenterCrop(imageView.getContext()), new RoundedCornersTransformation(imageView.getContext(), 15, 0, RoundedCornersTransformation.CornerType.ALL))
                .crossFade()
                .into(imageView);
    }

    @BindingAdapter({"bind:imageAsset"})
    public static void loadImage(ImageView imageView, final String imageUrl) {
        Glide.with(imageView.getContext())
                .load(imageUrl)
                .crossFade()
                .into(imageView);
    }


}
