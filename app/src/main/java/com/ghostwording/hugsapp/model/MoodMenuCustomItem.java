package com.ghostwording.hugsapp.model;

import com.ghostwording.hugsapp.model.listeners.ItemSelectedListener;

public class MoodMenuCustomItem {

    public final String customLabel;
    public final String customImageUrl;
    public final ItemSelectedListener itemSelectedListener;

    public MoodMenuCustomItem(String customImageUrl, String customLabel, ItemSelectedListener itemSelectedListener) {
        this.customImageUrl = customImageUrl;
        this.customLabel = customLabel;
        this.itemSelectedListener = itemSelectedListener;
    }

}
