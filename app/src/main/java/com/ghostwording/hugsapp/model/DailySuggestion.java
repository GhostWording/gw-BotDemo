package com.ghostwording.hugsapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DailySuggestion {

    @SerializedName("IntentionId")
    @Expose
    private String intentionId;
    @SerializedName("PrototypeId")
    @Expose
    private String prototypeId;
    @SerializedName("TextId")
    @Expose
    private String textId;
    @SerializedName("SourceTextId")
    @Expose
    private String sourceTextId;
    @SerializedName("Content")
    @Expose
    private String content;
    @SerializedName("ImageName")
    @Expose
    private String imageName;
    @SerializedName("ImageLink")
    @Expose
    private String imageLink;

    /**
     * @return The intentionId
     */
    public String getIntentionId() {
        return intentionId;
    }

    /**
     * @param intentionId The IntentionId
     */
    public void setIntentionId(String intentionId) {
        this.intentionId = intentionId;
    }

    /**
     * @return The prototypeId
     */
    public String getPrototypeId() {
        return prototypeId;
    }

    /**
     * @param prototypeId The PrototypeId
     */
    public void setPrototypeId(String prototypeId) {
        this.prototypeId = prototypeId;
    }

    /**
     * @return The textId
     */
    public String getTextId() {
        return textId;
    }

    /**
     * @param textId The TextId
     */
    public void setTextId(String textId) {
        this.textId = textId;
    }

    /**
     * @return The sourceTextId
     */
    public String getSourceTextId() {
        return sourceTextId;
    }

    /**
     * @param sourceTextId The SourceTextId
     */
    public void setSourceTextId(String sourceTextId) {
        this.sourceTextId = sourceTextId;
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
     * @return The imageLink
     */
    public String getImageLink() {
        return imageLink;
    }

    /**
     * @param imageLink The ImageLink
     */
    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }

}
