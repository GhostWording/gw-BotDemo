package com.ghostwording.chatbot.model.requests;

import com.ghostwording.chatbot.utils.AppConfiguration;
import com.ghostwording.chatbot.utils.PrefManager;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class UserProperty {

    @SerializedName("BotName")
    @Expose
    public final String botName = "Stickers";

    @SerializedName("DeviceId")
    @Expose
    public final String deviceId = AppConfiguration.getDeviceId();

    @SerializedName("FacebookId")
    @Expose
    public final String facebookId = PrefManager.instance().getFacebookId();

    @SerializedName("PropertyName")
    @Expose
    public final String propertyName;

    @SerializedName("PropertyValue")
    @Expose
    public final String propertyValue;

    public UserProperty(String propertyName, String propertyValue) {
        this.propertyName = propertyName;
        this.propertyValue = propertyValue;
    }

}
