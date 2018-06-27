package com.ghostwording.hugsapp.chatbot.model;

import com.ghostwording.hugsapp.model.GifResponse;

public class GifImageModel {

    public final String id;
    public final String imageUrl;
    public final String width;
    public final String height;

    public GifImageModel(GifResponse.GifImage gifImage) {
        id = gifImage.getId();
        imageUrl = gifImage.getImages().getFixedHeight().getUrl();
        width = gifImage.getImages().getFixedHeight().getWidth();
        height = gifImage.getImages().getFixedHeight().getHeight();
    }

    public GifImageModel(String imageUrl) {
        id = null;
        width = "300";
        height = width;
        this.imageUrl = imageUrl;
    }

}
