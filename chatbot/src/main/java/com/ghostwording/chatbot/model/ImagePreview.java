package com.ghostwording.chatbot.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ImagePreview {

    @SerializedName("ImagePath")
    @Expose
    private String imagePath;
    @SerializedName("Pictures")
    @Expose
    private List<String> pictures = null;
    @SerializedName("GifPath")
    @Expose
    private String gifPath;

    public String getGifPath() {
        return gifPath;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public List<String> getPictures() {
        return pictures;
    }

    public void setPictures(List<String> pictures) {
        this.pictures = pictures;
    }

}
