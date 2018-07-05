package com.ghostwording.chatbot.model.texts;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TranslationResult {

    @SerializedName("Text")
    @Expose
    private String text;
    @SerializedName("From")
    @Expose
    private String from;
    @SerializedName("To")
    @Expose
    private String to;
    @SerializedName("Translation")
    @Expose
    private String translation;

    /**
     * @return The text
     */
    public String getText() {
        return text;
    }

    /**
     * @param text The Text
     */
    public void setText(String text) {
        this.text = text;
    }

    /**
     * @return The from
     */
    public String getFrom() {
        return from;
    }

    /**
     * @param from The From
     */
    public void setFrom(String from) {
        this.from = from;
    }

    /**
     * @return The to
     */
    public String getTo() {
        return to;
    }

    /**
     * @param to The To
     */
    public void setTo(String to) {
        this.to = to;
    }

    /**
     * @return The translation
     */
    public String getTranslation() {
        return translation;
    }

    /**
     * @param translation The Translation
     */
    public void setTranslation(String translation) {
        this.translation = translation;
    }

}
