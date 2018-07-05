package com.ghostwording.chatbot.model.messaging;

import com.ghostwording.chatbot.utils.AppConfiguration;
import com.ghostwording.chatbot.utils.PrefManager;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class SendMessage {

    public interface MessageType {
        String GAME = "game";
        String DIRECT = "direct";
    }

    @SerializedName("sender")
    @Expose
    public final Sender sender = new Sender();

    @SerializedName("recipient")
    @Expose
    public final Recipient recipient;

    @SerializedName("message")
    @Expose
    public final Message message;

    @SerializedName("IsTest")
    @Expose
    public final Boolean isTest = false;

    @SerializedName("Area")
    @Expose
    public final String area = AppConfiguration.getAppAreaId();

    @SerializedName("AppName")
    @Expose
    public final String appName = AppConfiguration.getApplicationName();

    public SendMessage(Recipient recipient, Message message) {
        this.recipient = recipient;
        this.message = message;
    }

    public static class Message {
        @SerializedName("textId")
        @Expose
        public final String textId;

        @SerializedName("type")
        @Expose
        public final String type;

        @SerializedName("imageName")
        @Expose
        public final String imageName;

        @SerializedName("localTimestamp")
        @Expose
        public final long localTimestamp = new Date().getTime();

        public Message(String type, String textId, String imageName) {
            this.type = type;
            this.textId = textId;
            this.imageName = imageName;
        }
    }

    public static class Recipient {
        @SerializedName("deviceId")
        @Expose
        public final String deviceId;

        @SerializedName("facebookId")
        @Expose
        public final String facebookId;

        public Recipient(String deviceId, String facebookId) {
            this.deviceId = deviceId;
            this.facebookId = facebookId;
        }
    }

    public class Sender {
        @SerializedName("deviceId")
        @Expose
        public final String deviceId = AppConfiguration.getDeviceId();

        @SerializedName("facebookId")
        @Expose
        public final String facebookId = PrefManager.instance().getFacebookId();
    }

}
