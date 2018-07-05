package com.ghostwording.chatbot.model;

import com.ghostwording.chatbot.chatbot.model.BotSequence;
import com.ghostwording.chatbot.utils.Utils;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Promotions {

    @SerializedName("apps")
    @Expose
    private List<App> apps = null;

    public List<App> getApps() {
        return apps;
    }

    public void setApps(List<App> apps) {
        this.apps = apps;
    }

    public class App {

        @SerializedName("title")
        @Expose
        private BotSequence.Label title;
        @SerializedName("iconUrl")
        @Expose
        private String iconUrl;
        @SerializedName("storeUrl")
        @Expose
        private String storeUrl;

        public String getTitle() {
            return Utils.getLocalizedLabel(title);
        }

        public String getIconUrl() {
            return iconUrl;
        }

        public void setIconUrl(String iconUrl) {
            this.iconUrl = iconUrl;
        }

        public String getStoreUrl() {
            return storeUrl;
        }

        public void setStoreUrl(String storeUrl) {
            this.storeUrl = storeUrl;
        }

    }
}
