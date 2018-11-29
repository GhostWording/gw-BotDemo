package com.ghostwording.chatbot.viewmodel;

import androidx.databinding.ObservableField;

import com.ghostwording.chatbot.model.PopularImages;

public class PictureViewModel {

    public final ObservableField<String> imageAsset = new ObservableField<>();
    private PopularImages.Image image;
    private int position;

    public PictureViewModel(PopularImages.Image image, int position) {
        this.image = image;
        this.position = position;
        this.imageAsset.set(image.getImageLink());
    }

    public String getShared() {
        return String.valueOf(image.getNbSharesForIntention());
    }

    public String getRating() {
        return String.valueOf(position);
    }

    public boolean isRatingEnabled() {
        return image.getNbSharesForIntention() != null;
    }

}
