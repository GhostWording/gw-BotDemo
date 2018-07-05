package com.ghostwording.chatbot.utils;

import com.ghostwording.chatbot.model.PopularImages;
import com.ghostwording.chatbot.model.WeightAble;
import com.ghostwording.chatbot.model.recipients.Recipient;
import com.ghostwording.chatbot.model.texts.Quote;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;

public class UtilsMessages {

    public interface Gender {
        int MALE = 0;
        int FEMALE = 1;
    }

    private static final String MALE_INDICATOR = "H";
    private static final String FEMALE_INDICATOR = "F";

    public static List<Quote> filterPopularTexts(List<Quote> popularTexts) {
        List<Quote> result = new ArrayList<>();
        for (Quote popularText : popularTexts) {
            if (popularText.getNbSharesForIntention() > 1) {
                result.add(popularText);
            }
        }
        return result;
    }

    public static void filterAndUpdateRecipients(List<Recipient> recipients) {
        if (PrefManager.instance().getSelectedGender() == UtilsMessages.Gender.MALE) {
            removeRecipient(recipients, RecipientKnownTypes.LOVEINTEREST_M);
            removeRecipient(recipients, RecipientKnownTypes.SWEETHEART_M);
        }
        if (PrefManager.instance().getSelectedGender() == UtilsMessages.Gender.FEMALE) {
            removeRecipient(recipients, RecipientKnownTypes.LOVEINTEREST_F);
            removeRecipient(recipients, RecipientKnownTypes.SWEETHEART_F);
        }

        Collections.sort(recipients, new Comparator<Recipient>() {
            @Override
            public int compare(Recipient lhs, Recipient rhs) {
                return lhs.getImportance().compareTo(rhs.getImportance());
            }
        });
    }

    public static List<PopularImages.Image> filterPopularImages(List<PopularImages.Image> popularImages) {
        List<PopularImages.Image> result = new ArrayList<>();
        for (PopularImages.Image popularTexts : popularImages) {
            if (popularTexts.getNbSharesForIntention() > 1) {
                result.add(popularTexts);
            }
        }
        return result;
    }

    public static List<Quote> getWeightedRandomQuotes(List<Quote> quotes, int limit) {
        List<Quote> result = new ArrayList<>();
        List<WeightAble> weightAbles = new ArrayList<>();
        weightAbles.addAll(quotes);
        for (int i = 0; i < Math.min(quotes.size(), limit); i++) {
            int randomIndex = UtilsMessages.getRandomIndexBasedOnWeight(weightAbles);
            result.add(quotes.get(randomIndex));
            weightAbles.remove(randomIndex);
            quotes.remove(randomIndex);
        }
        return result;
    }

    public static int getRandomIndexBasedOnWeight(List<WeightAble> weightAbleList) {
        int totalWeight = 0;
        for (WeightAble weightAble : weightAbleList) {
            totalWeight += weightAble.getWeight();
        }
        int randomIndex = -1;
        double random = Math.random() * totalWeight;
        for (int i = 0; i < weightAbleList.size(); ++i) {
            random -= weightAbleList.get(i).getWeight();
            if (random <= 0.0d) {
                randomIndex = i;
                break;
            }
        }
        return randomIndex;
    }

    public static List<Quote> filterQuotes(List<Quote> quotes, int gender) {
        return filterQuotes(quotes, gender, true);
    }

    public static List<Quote> filterQuotes(List<Quote> quotes, int gender, boolean isOnePrototype) {
        final String POLITE_VOUS = "V";

        List<Quote> result = new ArrayList<>();
        if (quotes == null) {
            return result;
        }

        HashSet<String> prototypeIds = new HashSet<>();

        for (Quote quote : quotes) {
            //filter out excluded polite form
            if (quote.getPoliteForm().equals(POLITE_VOUS)) {
                continue;
            }

            //filter messages with user defined relationType
            String userDefinedRelationType = PrefManager.instance().getRelationType();
            if (userDefinedRelationType != null) {
                if (!isContainsTag(quote.getTagIds(), userDefinedRelationType)) {
                    continue;
                }
                //if recipient is gender related then don't allow messages for opposite gender
                if (PrefManager.instance().getRecipientGender() != null) {
                    if (PrefManager.instance().getRecipientGender().equals(MALE_INDICATOR) && quote.getTarget().equals(FEMALE_INDICATOR)) {
                        continue;
                    }
                    if (PrefManager.instance().getRecipientGender().equals(FEMALE_INDICATOR) && quote.getTarget().equals(MALE_INDICATOR)) {
                        continue;
                    }
                }
            }

            //filter by sender gender
            if (gender == Gender.MALE && quote.getSender().equals(FEMALE_INDICATOR)) {
                continue;
            }
            if (gender == Gender.FEMALE && quote.getSender().equals(MALE_INDICATOR)) {
                continue;
            }

            //filter by recipient gender if needed
            if (gender == Gender.MALE && quote.getTarget().equals(MALE_INDICATOR)) {
                continue;
            }
            if (gender == Gender.FEMALE && quote.getTarget().equals(FEMALE_INDICATOR)) {
                continue;
            }

            //only one prototypeId text allowed
            if (!prototypeIds.contains(quote.getPrototypeId()) || !isOnePrototype) {
                prototypeIds.add(quote.getPrototypeId());
                result.add(quote);
            }
        }
        return result;
    }

    private static boolean isContainsTag(List<String> tags, String tag) {
        for (String mTag : tags) {
            if (mTag.contains(tag)) {
                return true;
            }
        }
        return false;
    }

    private static void removeRecipient(List<Recipient> recipients, String recipientId) {
        for (int i = 0; i < recipients.size(); i++) {
            if (recipients.get(i).getId().equals(recipientId)) {
                recipients.remove(i);
                break;
            }
        }
    }

    public class RecipientKnownTypes {
        public static final String SWEETHEART_M = "SweetheartM";
        public static final String SWEETHEART_F = "SweetheartF";
        public static final String LOVEINTEREST_F = "LoveInterestF";
        public static final String LOVEINTEREST_M = "LoveInterestM";
    }


}
