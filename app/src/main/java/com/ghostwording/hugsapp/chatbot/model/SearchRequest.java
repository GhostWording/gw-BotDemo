package com.ghostwording.hugsapp.chatbot.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SearchRequest {

    @SerializedName("Text")
    @Expose
    public final String text;

    public SearchRequest(String text) {
        this.text = text;
    }

}
