package com.ghostwording.chatbot.chatbot.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CarouselMessage {

    @SerializedName("Elements")
    @Expose
    private List<BotSequence.CarouselElements> elements = null;

    public List<BotSequence.CarouselElements> getElements() {
        return elements;
    }

}