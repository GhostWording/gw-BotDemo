package com.ghostwording.chatbot.viewmodel;

import com.ghostwording.chatbot.model.texts.Quote;

public class QuoteRecommendationViewModel {

    private Quote quote;
    private int position;
    private boolean displayShares;

    public QuoteRecommendationViewModel(Quote quote, int position, boolean displayShares) {
        this.quote = quote;
        this.position = position;
        this.displayShares = displayShares;
    }

    public String getQuoteText() {
        return quote.getContent();
    }

    public String getShared() {
        if (displayShares) {
            return String.valueOf(quote.getNbSharesForIntention());
        } else {
            return null;
        }
    }

    public String getRating() {
        return String.valueOf(position + 1);
    }

    public boolean isRatingEnabled() {
        return getShared() != null && quote.getNbSharesForIntention() != null;
    }

}
