package com.ghostwording.hugsapp.model.texts;

import java.util.Comparator;
import java.util.Locale;

public class QuoteLanguageComparator implements Comparator<Quote> {
    @Override
    public int compare(Quote lhs, Quote rhs) {
        if (lhs.getCulture().toLowerCase().contains(Locale.getDefault().getLanguage())) {
            return -1;
        }
        return 0;
    }

}
