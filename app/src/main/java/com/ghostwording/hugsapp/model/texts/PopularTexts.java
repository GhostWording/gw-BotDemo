package com.ghostwording.hugsapp.model.texts;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class PopularTexts {

    @SerializedName("Texts")
    @Expose
    private List<Text> Texts = new ArrayList<>();

    @SerializedName("IntentionId")
    @Expose
    private String IntentionId;
    @SerializedName("IntentionLabel")
    @Expose
    private String IntentionLabel;

    /**
     * @return The Texts
     */
    public List<Quote> getTexts() {
        List<Quote> result = new ArrayList<>();
        for (Text text : Texts) {
            Quote quote = text.quote;
            if (quote != null) {
                quote.setScoring(text.scoring);
                result.add(quote);
            }
        }
        return result;
    }

    /**
     * @return The IntentionId
     */
    public String getIntentionId() {
        return IntentionId;
    }

    /**
     * @param IntentionId The IntentionId
     */
    public void setIntentionId(String IntentionId) {
        this.IntentionId = IntentionId;
    }

    /**
     * @return The IntentionLabel
     */
    public String getIntentionLabel() {
        return IntentionLabel;
    }

    /**
     * @param IntentionLabel The IntentionLabel
     */
    public void setIntentionLabel(String IntentionLabel) {
        this.IntentionLabel = IntentionLabel;
    }

    public class Text {
        @SerializedName("Scoring")
        @Expose
        public Quote.Scoring scoring;

        @SerializedName("Text")
        @Expose
        public Quote quote;
    }

}
