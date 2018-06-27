package com.ghostwording.hugsapp.model.recipients;

import java.util.Comparator;

public class RecipientsComparator implements Comparator<Recipient> {

    @Override
    public int compare(Recipient lhs, Recipient rhs) {
        return lhs.getImportance().compareTo(rhs.getImportance());
    }
}
