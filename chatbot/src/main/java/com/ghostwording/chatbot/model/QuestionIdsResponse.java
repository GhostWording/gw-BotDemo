package com.ghostwording.chatbot.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class QuestionIdsResponse {

    @SerializedName("Ids")
    @Expose
    private List<String> ids = new ArrayList<String>();

    /**
     * @return The ids
     */
    public List<String> getIds() {
        return ids;
    }

    /**
     * @param ids The Ids
     */
    public void setIds(List<String> ids) {
        this.ids = ids;
    }

}
