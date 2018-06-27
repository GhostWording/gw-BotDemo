package com.ghostwording.hugsapp.model.intentions;

import java.util.Comparator;

public class IntentionAreaComparator implements Comparator<Intention> {

    @Override
    public int compare(Intention lhs, Intention rhs) {
        return lhs.getSortOrderInArea().compareTo(rhs.getSortOrderInArea());
    }

}
