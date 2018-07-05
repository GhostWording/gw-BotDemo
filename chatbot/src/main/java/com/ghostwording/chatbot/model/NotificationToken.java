package com.ghostwording.chatbot.model;

import com.ghostwording.chatbot.utils.AppConfiguration;
import com.ghostwording.chatbot.utils.PrefManager;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NotificationToken {

    @SerializedName("appName")
    @Expose
    private String appName = AppConfiguration.getApplicationName();
    @SerializedName("notificationToken")
    @Expose
    private String notificationToken;
    @SerializedName("deviceId")
    @Expose
    private String deviceId = AppConfiguration.getDeviceId();
    @SerializedName("facebookId")
    @Expose
    private String facebookId = PrefManager.instance().getFacebookId();

    public String getNotificationToken() {
        return notificationToken;
    }

    public void setNotificationToken(String notificationToken) {
        this.notificationToken = notificationToken;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getFacebookId() {
        return facebookId;
    }

    public void setFacebookId(String facebookId) {
        this.facebookId = facebookId;
    }

    public String getAppName() {
        return appName;
    }
}
