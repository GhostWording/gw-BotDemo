package com.ghostwording.chatbot.chatbot.model;

public class ImageMessage {

    public final String imageUrl;
    public final boolean fullWidth;

    public ImageMessage(String imageUrl, boolean fullWidth) {
        this.imageUrl = imageUrl;
        this.fullWidth = fullWidth;
    }

}
