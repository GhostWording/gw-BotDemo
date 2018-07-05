package com.ghostwording.chatbot.model.intentions;

import com.ghostwording.chatbot.utils.PrefManager;

import java.util.Comparator;

public class IntentionsPopularComparator implements Comparator<Intention> {

    @Override
    public int compare(Intention lhs, Intention rhs) {
        return PrefManager.instance().getIntentionChooseCount(rhs.getIntentionId()).compareTo(PrefManager.instance().getIntentionChooseCount(lhs.getIntentionId()));
    }

}
