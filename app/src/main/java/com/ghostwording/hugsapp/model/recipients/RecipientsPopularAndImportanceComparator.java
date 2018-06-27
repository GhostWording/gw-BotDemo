package com.ghostwording.hugsapp.model.recipients;

import com.ghostwording.hugsapp.utils.PrefManager;

import java.util.Comparator;

public class RecipientsPopularAndImportanceComparator implements Comparator<Recipient> {

    @Override
    public int compare(Recipient lhs, Recipient rhs) {
        int comparePopularity = PrefManager.instance().getRecipientChooseCount(rhs.getId()).compareTo(PrefManager.instance().getRecipientChooseCount(lhs.getId()));
        return comparePopularity == 0 ? lhs.getImportance().compareTo(rhs.getImportance()) : comparePopularity;
    }
}
