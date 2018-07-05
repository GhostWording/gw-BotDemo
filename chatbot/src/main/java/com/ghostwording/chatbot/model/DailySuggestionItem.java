package com.ghostwording.chatbot.model;

public class DailySuggestionItem {

    public final DailySuggestion dailySuggestion;
    public final String title;

    public DailySuggestionItem(String title) {
        this.title = title;
        dailySuggestion = null;
    }

    public DailySuggestionItem(DailySuggestion dailySuggestion) {
        this.dailySuggestion = dailySuggestion;
        title = null;
    }

}
