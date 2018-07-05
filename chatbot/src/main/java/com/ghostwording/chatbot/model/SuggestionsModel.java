package com.ghostwording.chatbot.model;

import java.util.List;

public class SuggestionsModel {

    public final List<DailySuggestion> suggestions;
    public final int numberOfCards;

    public SuggestionsModel(List<DailySuggestion> suggestions, int numberOfCards) {
        this.suggestions = suggestions;
        this.numberOfCards = numberOfCards;
    }

}
