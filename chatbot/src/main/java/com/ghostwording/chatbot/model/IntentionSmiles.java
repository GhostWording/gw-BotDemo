package com.ghostwording.chatbot.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class IntentionSmiles {

    @SerializedName("smileysForIntentions")
    @Expose
    private List<SmileysForIntention> smileysForIntentions = null;

    public List<SmileysForIntention> getSmileysForIntentions() {
        return smileysForIntentions;
    }

    public void setSmileysForIntentions(List<SmileysForIntention> smileysForIntentions) {
        this.smileysForIntentions = smileysForIntentions;
    }

    public class SmileysForIntention {

        @SerializedName("slug")
        @Expose
        private String slug;
        @SerializedName("smileys")
        @Expose
        private List<String> smileys = null;

        public String getSlug() {
            return slug;
        }

        public void setSlug(String slug) {
            this.slug = slug;
        }

        public List<String> getSmileys() {
            return smileys;
        }

        public void setSmileys(List<String> smileys) {
            this.smileys = smileys;
        }

    }

}