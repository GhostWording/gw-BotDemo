package com.ghostwording.hugsapp.model.messaging;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MessageAction {

    @SerializedName("facebookId")
    @Expose
    public final String facebookId;

    @SerializedName("firstMessage")
    @Expose
    public final String firstMessage;

    @SerializedName("secondMessage")
    @Expose
    public final String secondMessage;

    @SerializedName("preferred")
    @Expose
    public final String preferred;

    @SerializedName("isBot")
    @Expose
    public final String isBot;

    public static MessageAction createViewedAction(String facebookId, String firstMessage, String secondMessage) {
        return new MessageAction(facebookId, firstMessage, secondMessage, null, null);
    }

    public static MessageAction createPreferedAction(String facebookId, String firstMessage, String secondMessage, String preferred) {
        return new MessageAction(facebookId, firstMessage, secondMessage, preferred, null);
    }

    public static MessageAction createGuessAction(String facebookId, String firstMessage, String secondMessage, String isBot) {
        return new MessageAction(facebookId, firstMessage, secondMessage, null, isBot);
    }

    private MessageAction(String facebookId, String firstMessage, String secondMessage, String preferred, String isBot) {
        this.facebookId = facebookId;
        this.firstMessage = firstMessage;
        this.secondMessage = secondMessage;
        this.preferred = preferred;
        this.isBot = isBot;
    }

}
