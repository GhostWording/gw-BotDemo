package com.ghostwording.hugsapp.model;

import com.ghostwording.hugsapp.chatbot.model.BotSequence;
import com.ghostwording.hugsapp.utils.Utils;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PromoContent {

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

        @SerializedName("header")
        @Expose
        private BotSequence.Label header;
        @SerializedName("iconUrl")
        @Expose
        private String iconUrl;
        @SerializedName("button")
        @Expose
        private BotSequence.Label button;
        @SerializedName("storeUrl")
        @Expose
        private String storeUrl;

        public String getHeader() {
            return Utils.getLocalizedLabel(header);
        }

        public String getIconUrl() {
            return iconUrl;
        }

        public void setIconUrl(String iconUrl) {
            this.iconUrl = iconUrl;
        }

        public String getButton() {
            return Utils.getLocalizedLabel(button);
        }

        public String getStoreUrl() {
            return storeUrl;
        }

        public void setStoreUrl(String storeUrl) {
            this.storeUrl = storeUrl;
        }

    }

}
