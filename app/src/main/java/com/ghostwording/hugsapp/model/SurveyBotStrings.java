package com.ghostwording.hugsapp.model;

import com.ghostwording.hugsapp.chatbot.model.BotSequence;
import com.ghostwording.hugsapp.utils.Utils;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SurveyBotStrings {

    @SerializedName("SurveyBot")
    @Expose
    private List<SurveyBot> surveyBot = null;

    public List<SurveyBot> getSurveyBot() {
        return surveyBot;
    }

    public void setSurveyBot(List<SurveyBot> surveyBot) {
        this.surveyBot = surveyBot;
    }

    public class SurveyBot {

        @SerializedName("ID")
        @Expose
        private String iD;
        @SerializedName("Label")
        @Expose
        private BotSequence.Label label = null;
        @SerializedName("key")
        @Expose
        private String key;

        public String getLabel() {
            return Utils.getLocalizedLabel(label);
        }

        public String getID() {
            return iD;
        }

        public void setID(String iD) {
            this.iD = iD;
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

    }

}
