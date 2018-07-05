package com.ghostwording.chatbot.model.messaging;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class IncomingMessage {

    @SerializedName("Sender")
    @Expose
    private Sender sender;
    @SerializedName("Recipient")
    @Expose
    private Recipient recipient;
    @SerializedName("Message")
    @Expose
    private Message message;
    @SerializedName("BotMessage")
    @Expose
    private Message botMessage;
    @SerializedName("IsTest")
    @Expose
    private Boolean isTest;
    @SerializedName("Culture")
    @Expose
    private Object culture;

    /**
     * @return The sender
     */
    public Sender getSender() {
        return sender;
    }

    /**
     * @param sender The Sender
     */
    public void setSender(Sender sender) {
        this.sender = sender;
    }

    /**
     * @return The recipient
     */
    public Recipient getRecipient() {
        return recipient;
    }

    /**
     * @param recipient The Recipient
     */
    public void setRecipient(Recipient recipient) {
        this.recipient = recipient;
    }

    /**
     * @return The message
     */
    public Message getMessage() {
        return message;
    }

    /**
     * @param message The Message
     */
    public void setMessage(Message message) {
        this.message = message;
    }

    /**
     * @return The botMessage
     */
    public Message getBotMessage() {
        return botMessage;
    }

    /**
     * @return The isTest
     */
    public Boolean getIsTest() {
        return isTest;
    }

    /**
     * @param isTest The IsTest
     */
    public void setIsTest(Boolean isTest) {
        this.isTest = isTest;
    }

    /**
     * @return The culture
     */
    public Object getCulture() {
        return culture;
    }

    /**
     * @param culture The Culture
     */
    public void setCulture(Object culture) {
        this.culture = culture;
    }

    public class Message {

        @SerializedName("MessageId")
        @Expose
        private String messageId;
        @SerializedName("TextId")
        @Expose
        private Object textId;
        @SerializedName("Gender")
        @Expose
        private String gender;
        @SerializedName("Type")
        @Expose
        private String type;
        @SerializedName("IsUserCustom")
        @Expose
        private Boolean isUserCustom;
        @SerializedName("Content")
        @Expose
        private String content;
        @SerializedName("ImageName")
        @Expose
        private String imageName;
        @SerializedName("LocalTimestamp")
        @Expose
        private long localTimestamp;
        @SerializedName("ServerTime")
        @Expose
        private String serverTime;
        @SerializedName("Actions")
        @Expose
        private List<Action> actions = new ArrayList<>();

        public String getServerTime() {
            return serverTime;
        }

        /**
         * @return The messageId
         */
        public String getMessageId() {
            return messageId;
        }

        /**
         * @param messageId The MessageId
         */
        public void setMessageId(String messageId) {
            this.messageId = messageId;
        }

        /**
         * @return The textId
         */
        public Object getTextId() {
            return textId;
        }

        /**
         * @param textId The TextId
         */
        public void setTextId(Object textId) {
            this.textId = textId;
        }

        /**
         * @return The gender
         */
        public String getGender() {
            return gender;
        }

        /**
         * @param gender The Gender
         */
        public void setGender(String gender) {
            this.gender = gender;
        }

        /**
         * @return The type
         */
        public String getType() {
            return type;
        }

        /**
         * @param type The Type
         */
        public void setType(String type) {
            this.type = type;
        }

        /**
         * @return The isUserCustom
         */
        public Boolean getIsUserCustom() {
            return isUserCustom;
        }

        /**
         * @param isUserCustom The IsUserCustom
         */
        public void setIsUserCustom(Boolean isUserCustom) {
            this.isUserCustom = isUserCustom;
        }

        /**
         * @return The content
         */
        public String getContent() {
            return content;
        }

        /**
         * @param content The Content
         */
        public void setContent(String content) {
            this.content = content;
        }

        /**
         * @return The imageName
         */
        public String getImageName() {
            return imageName;
        }

        /**
         * @param imageName The ImageName
         */
        public void setImageName(String imageName) {
            this.imageName = imageName;
        }

        /**
         * @return The localTimestamp
         */
        public long getLocalTimestamp() {
            return localTimestamp;
        }

        /**
         * @param localTimestamp The LocalTimestamp
         */
        public void setLocalTimestamp(Integer localTimestamp) {
            this.localTimestamp = localTimestamp;
        }

        /**
         * @return The actions
         */
        public List<Action> getActions() {
            return actions;
        }

        /**
         * @param actions The Actions
         */
        public void setActions(List<Action> actions) {
            this.actions = actions;
        }

    }

    public class Action {

        @SerializedName("Name")
        @Expose
        private String name;
        @SerializedName("Value")
        @Expose
        private Object value;
        @SerializedName("Time")
        @Expose
        private String time;

        /**
         * @return The name
         */
        public String getName() {
            return name;
        }

        /**
         * @param name The Name
         */
        public void setName(String name) {
            this.name = name;
        }

        /**
         * @return The value
         */
        public Object getValue() {
            return value;
        }

        /**
         * @param value The Value
         */
        public void setValue(Object value) {
            this.value = value;
        }

        /**
         * @return The time
         */
        public String getTime() {
            return time;
        }

        /**
         * @param time The Time
         */
        public void setTime(String time) {
            this.time = time;
        }

    }

    public class Recipient {

        @SerializedName("DeviceId")
        @Expose
        private String deviceId;
        @SerializedName("FacebookId")
        @Expose
        private String facebookId;

        /**
         * @return The deviceId
         */
        public String getDeviceId() {
            return deviceId;
        }

        /**
         * @param deviceId The DeviceId
         */
        public void setDeviceId(String deviceId) {
            this.deviceId = deviceId;
        }

        /**
         * @return The facebookId
         */
        public String getFacebookId() {
            return facebookId;
        }

        /**
         * @param facebookId The FacebookId
         */
        public void setFacebookId(String facebookId) {
            this.facebookId = facebookId;
        }

    }

    public class Sender {

        @SerializedName("DeviceId")
        @Expose
        private String deviceId;
        @SerializedName("FacebookId")
        @Expose
        private String facebookId;

        /**
         * @return The deviceId
         */
        public String getDeviceId() {
            return deviceId;
        }

        /**
         * @param deviceId The DeviceId
         */
        public void setDeviceId(String deviceId) {
            this.deviceId = deviceId;
        }

        /**
         * @return The facebookId
         */
        public String getFacebookId() {
            return facebookId;
        }

        /**
         * @param facebookId The FacebookId
         */
        public void setFacebookId(String facebookId) {
            this.facebookId = facebookId;
        }

    }

}
