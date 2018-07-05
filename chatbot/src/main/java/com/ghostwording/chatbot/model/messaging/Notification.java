package com.ghostwording.chatbot.model.messaging;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Notification {

    @SerializedName("Area")
    @Expose
    private String area;
    @SerializedName("Type")
    @Expose
    private String type;
    @SerializedName("Action")
    @Expose
    private String action;
    @SerializedName("Title")
    @Expose
    private String title;
    @SerializedName("Sender")
    @Expose
    private String sender;
    @SerializedName("Icon")
    @Expose
    private String icon;
    @SerializedName("Description")
    @Expose
    private String description;
    @SerializedName("ServerTime")
    @Expose
    private String serverTime;
    @SerializedName("Info")
    @Expose
    private Info info;

    /**
     * @return The area
     */
    public String getArea() {
        return area;
    }

    /**
     * @param area The Area
     */
    public void setArea(String area) {
        this.area = area;
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
     * @return The action
     */
    public String getAction() {
        return action;
    }

    /**
     * @param action The Action
     */
    public void setAction(String action) {
        this.action = action;
    }

    /**
     * @return The title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title The Title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return The sender
     */
    public String getSender() {
        return sender;
    }

    /**
     * @param sender The Sender
     */
    public void setSender(String sender) {
        this.sender = sender;
    }

    /**
     * @return The icon
     */
    public String getIcon() {
        return icon;
    }

    /**
     * @param icon The Icon
     */
    public void setIcon(String icon) {
        this.icon = icon;
    }

    /**
     * @return The description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description The Description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return The serverTime
     */
    public String getServerTime() {
        return serverTime;
    }

    /**
     * @param serverTime The ServerTime
     */
    public void setServerTime(String serverTime) {
        this.serverTime = serverTime;
    }

    /**
     * @return The info
     */
    public Info getInfo() {
        return info;
    }

    /**
     * @param info The Info
     */
    public void setInfo(Info info) {
        this.info = info;
    }

    public class Info {

        @SerializedName("MessageId")
        @Expose
        private String messageId;
        @SerializedName("SecondMessageId")
        @Expose
        private String secondMessageId;
        @SerializedName("Value")
        @Expose
        private Object value;

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
         * @return The secondMessageId
         */
        public String getSecondMessageId() {
            return secondMessageId;
        }

        /**
         * @param secondMessageId The SecondMessageId
         */
        public void setSecondMessageId(String secondMessageId) {
            this.secondMessageId = secondMessageId;
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
    }
}

