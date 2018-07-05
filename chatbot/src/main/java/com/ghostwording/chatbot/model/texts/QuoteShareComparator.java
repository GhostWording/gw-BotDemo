package com.ghostwording.chatbot.model.texts;

import java.util.Comparator;

public class QuoteShareComparator implements Comparator<Quote> {
    @Override
    public int compare(Quote lhs, Quote rhs) {
        return rhs.getNbSharesForIntention().compareTo(lhs.getNbSharesForIntention());
    }
}
