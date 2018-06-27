package com.ghostwording.hugsapp.model;

import com.ghostwording.hugsapp.model.intentions.Intention;
import com.ghostwording.hugsapp.model.recipients.Recipient;

public class MoodMenuItem {

    private ThemeResponse.Theme theme;
    private Intention intention;
    private Recipient recipient;
    private MoodMenuCustomItem moodMenuCustomItem;

    public MoodMenuItem(ThemeResponse.Theme theme) {
        this.theme = theme;
    }

    public MoodMenuItem(Intention intention) {
        this.intention = intention;
    }

    public MoodMenuItem(Recipient recipient) {
        this.recipient = recipient;
    }

    public MoodMenuItem(MoodMenuCustomItem customItem) {
        this.moodMenuCustomItem = customItem;
    }

    public ThemeResponse.Theme getTheme() {
        return theme;
    }

    public Intention getIntention() {
        return intention;
    }

    public Recipient getRecipient() {
        return recipient;
    }

    public MoodMenuCustomItem getMoodMenuCustomItem() {
        return moodMenuCustomItem;
    }

    public String getId() {
        if (intention != null) {
            return intention.getIntentionId();
        }
        if (theme != null) {
            return theme.getPath();
        }
        if (recipient != null) {
            return recipient.getId();
        }
        return null;
    }

    public String getLabel() {
        if (theme != null) {
            return theme.getLabel();
        }
        if (intention != null) {
            return intention.getLabel();
        }
        if (recipient != null) {
            return recipient.getLocalizedLabel();
        }
        if (moodMenuCustomItem != null) {
            return moodMenuCustomItem.customLabel;
        }
        return null;
    }

    public String getImageAsset() {
        if (theme != null) {
            return theme.getImagePath();
        }
        if (intention != null) {
            return intention.getMediaUrl();
        }
        if (recipient != null) {
            return recipient.getImageUrl();
        }
        if (moodMenuCustomItem != null) {
            return moodMenuCustomItem.customImageUrl;
        }
        return null;
    }

    public String getImagePath() {
        if (intention != null) {
            return intention.getImagePath();
        }
        if (theme != null) {
            return theme.getPath();
        }
        return null;
    }

}
