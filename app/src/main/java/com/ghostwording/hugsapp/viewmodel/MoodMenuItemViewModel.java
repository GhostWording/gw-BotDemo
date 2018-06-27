package com.ghostwording.hugsapp.viewmodel;

import com.ghostwording.hugsapp.model.MoodMenuItem;

public class MoodMenuItemViewModel {

    private MoodMenuItem item;
    private ItemSelectedListener itemSelectedListener;

    public MoodMenuItemViewModel(MoodMenuItem item, ItemSelectedListener itemSelectedListener) {
        this.item = item;
        this.itemSelectedListener = itemSelectedListener;
    }

    public String getImageAsset() {
        if (item.getTheme() != null) {
            return item.getTheme().getImagePath();
        }
        if (item.getIntention() != null) {
            return item.getIntention().getMediaUrl();
        }
        if (item.getRecipient() != null) {
            return item.getRecipient().getImageUrl();
        }
        if (item.getMoodMenuCustomItem() != null) {
            return item.getMoodMenuCustomItem().customImageUrl;
        }
        return "";
    }

    public String getCaption() {
        if (item.getTheme() != null) {
            return item.getTheme().getLabel();
        }
        if (item.getIntention() != null) {
            return item.getIntention().getLabel();
        }
        if (item.getRecipient() != null) {
            return item.getRecipient().getLocalizedLabel();
        }
        if (item.getMoodMenuCustomItem() != null) {
            return item.getMoodMenuCustomItem().customLabel;
        }
        return "";
    }

    public interface ItemSelectedListener {
        void onItemSelected(MoodMenuItem item);
    }

}
