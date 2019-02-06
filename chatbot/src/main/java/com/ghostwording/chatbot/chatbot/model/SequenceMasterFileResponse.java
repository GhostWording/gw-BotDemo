package com.ghostwording.chatbot.chatbot.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SequenceMasterFileResponse {

    @SerializedName("GroupName")
    @Expose
    private String groupName;
    @SerializedName("Order")
    @Expose
    private Integer order;
    @SerializedName("SequencesForLine")
    @Expose
    private List<BotSequence> sequencesForLine = null;

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public List<BotSequence> getSequencesForLine() {
        return sequencesForLine;
    }
}
