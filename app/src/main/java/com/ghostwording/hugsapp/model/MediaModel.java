package com.ghostwording.hugsapp.model;

public class MediaModel {

    private String imageUrl;
    private GifResponse.GifImage gifImage;

    public MediaModel(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public MediaModel(GifResponse.GifImage gifImage) {
        this.gifImage = gifImage;
    }

    public GifResponse.GifImage getGifImage() {
        return gifImage;
    }

    public String getImageUrl() {
        return imageUrl;
    }

}
