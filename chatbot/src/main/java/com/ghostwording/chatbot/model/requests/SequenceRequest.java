package com.ghostwording.chatbot.model.requests;

import com.ghostwording.chatbot.utils.AppConfiguration;
import com.ghostwording.chatbot.utils.PrefManager;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SequenceRequest {

    @SerializedName("BotName")
    @Expose
    public final String botName;

    @SerializedName("DeviceId")
    @Expose
    public final String deviceId = AppConfiguration.getDeviceId();

    @SerializedName("FacebookId")
    @Expose
    public final String facebookId = PrefManager.instance().getFacebookId();

    @SerializedName("LastSequenceId")
    @Expose
    public final String lastSequenceId;

    public SequenceRequest(String botName) {
        this.botName = botName;
        this.lastSequenceId = PrefManager.instance().getLastSequenceId(botName);
    }
}
