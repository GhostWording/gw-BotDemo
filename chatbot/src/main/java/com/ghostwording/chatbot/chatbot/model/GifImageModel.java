package com.ghostwording.chatbot.chatbot.model;

import com.ghostwording.chatbot.model.GifResponse;

public class GifImageModel {

    public final String id;
    public final String imageUrl;
    public final String width;
    public final String height;
    private boolean fullWidth = false;

    public GifImageModel(String imageUrl) {
        id = null;
        width = "300";
        height = width;
        this.imageUrl = imageUrl;
    }

    public GifImageModel(GifResponse.GifImage gifImage) {
        id = gifImage.getId();
        imageUrl = gifImage.getImages().getFixedHeight().getUrl();
        width = gifImage.getImages().getFixedHeight().getWidth();
        height = gifImage.getImages().getFixedHeight().getHeight();
    }

    public void setFullWidth(boolean fullWidth) {
        this.fullWidth = fullWidth;
    }

    public boolean isFullWidth() {
        return fullWidth;
    }

}