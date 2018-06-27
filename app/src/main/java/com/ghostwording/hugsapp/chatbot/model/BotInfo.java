package com.ghostwording.hugsapp.chatbot.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BotInfo {

    @SerializedName("botName")
    @Expose
    private String botName;
    @SerializedName("botIconName")
    @Expose
    private String botIconName;
    @SerializedName("botTitle")
    @Expose
    private BotTitle botTitle;

    public String getBotName() {
        return botName;
    }

    public void setBotName(String botName) {
        this.botName = botName;
    }

    public String getBotIconName() {
        return botIconName;
    }

    public void setBotIconName(String botIconName) {
        this.botIconName = botIconName;
    }

    public BotTitle getBotTitle() {
        return botTitle;
    }

    public void setBotTitle(BotTitle botTitle) {
        this.botTitle = botTitle;
    }

    public String getTitle() {
        return botTitle.en;
    }


    public class BotTitle {

        @SerializedName("en")
        @Expose
        private String en;
        @SerializedName("fr")
        @Expose
        private String fr;
        @SerializedName("es")
        @Expose
        private String es;

        public String getEn() {
            return en;
        }

        public void setEn(String en) {
            this.en = en;
        }

        public String getFr() {
            return fr;
        }

        public void setFr(String fr) {
            this.fr = fr;
        }

        public String getEs() {
            return es;
        }

        public void setEs(String es) {
            this.es = es;
        }

    }

}
