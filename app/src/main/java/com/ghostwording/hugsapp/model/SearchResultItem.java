package com.ghostwording.hugsapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SearchResultItem {

    @SerializedName("Rank")
    @Expose
    private Integer rank;
    @SerializedName("IntentionId")
    @Expose
    private String intentionId;
    @SerializedName("PrototypeId")
    @Expose
    private String prototypeId;
    @SerializedName("TextId")
    @Expose
    private String textId;
    @SerializedName("Content")
    @Expose
    private String content;

    /**
     * @return The rank
     */
    public Integer getRank() {
        return rank;
    }

    /**
     * @param rank The Rank
     */
    public void setRank(Integer rank) {
        this.rank = rank;
    }

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

}
