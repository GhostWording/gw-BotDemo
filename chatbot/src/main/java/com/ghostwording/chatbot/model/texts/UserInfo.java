package com.ghostwording.chatbot.model.texts;

import com.ghostwording.chatbot.utils.AppConfiguration;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserInfo {

    @SerializedName("BotName")
    @Expose
    private String botName = "Stickers";

    @SerializedName("DeviceId")
    @Expose
    public final String deviceId = AppConfiguration.getDeviceId();

    public UserInfo(String botName) {
        this.botName = botName;
    }

}
