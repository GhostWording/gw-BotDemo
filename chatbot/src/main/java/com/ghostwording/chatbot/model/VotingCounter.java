package com.ghostwording.chatbot.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class VotingCounter {

    @SerializedName("counterName")
    @Expose
    private String counterName;
    @SerializedName("value")
    @Expose
    private Integer value;

    public String getCounterName() {
        return counterName;
    }

    public void setCounterName(String counterName) {
        this.counterName = counterName;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

}
