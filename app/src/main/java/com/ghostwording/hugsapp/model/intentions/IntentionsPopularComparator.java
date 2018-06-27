package com.ghostwording.hugsapp.model.intentions;

import com.ghostwording.hugsapp.utils.PrefManager;

import java.util.Comparator;

public class IntentionsPopularComparator implements Comparator<Intention> {

    @Override
    public int compare(Intention lhs, Intention rhs) {
        return PrefManager.instance().getIntentionChooseCount(rhs.getIntentionId()).compareTo(PrefManager.instance().getIntentionChooseCount(lhs.getIntentionId()));
    }

}
