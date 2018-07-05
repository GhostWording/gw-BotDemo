package com.ghostwording.chatbot.chatbot;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import com.ghostwording.chatbot.ChatBotApplication;
import com.ghostwording.chatbot.R;
import com.ghostwording.chatbot.analytics.AnalyticsHelper;
import com.ghostwording.chatbot.chatbot.model.ChatMessage;
import com.ghostwording.chatbot.model.DailySuggestion;
import com.ghostwording.chatbot.model.PopularImages;
import com.ghostwording.chatbot.model.UserProfile;
import com.ghostwording.chatbot.model.recipients.Recipient;
import com.ghostwording.chatbot.model.texts.PopularTexts;
import com.ghostwording.chatbot.model.texts.Quote;
import com.ghostwording.chatbot.textimagepreviews.PicturesRecommendationActivity;
import com.ghostwording.chatbot.utils.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BotQuestionsManager {

    private static BotQuestionsManager instance;

    public static synchronized BotQuestionsManager instance() {
        if (instance == null) {
            instance = new BotQuestionsManager();
        }
        return instance;
    }

    private List<String> usedQuestions = new ArrayList<>();
    private List<String> usedSuggestions = new ArrayList<>();
    private List<String> usedUsers = new ArrayList<>();
    private List<String> usedImages = new ArrayList<>();
    private List<Quote> questionsList = new ArrayList<>();

    private Recipient botRecipient;

    private BotQuestionsManager() {
        botRecipient = new Recipient();
        botRecipient.setId("BotFriends");
    }

    public void setBotQuestions(List<Quote> questionsList) {
        this.questionsList = questionsList;
    }

    public boolean messageContainQuote(ChatMessage.BotMessage botMessage, List<Quote> quotes) {
        for (Quote quote : quotes) {
            if (botMessage.getTextId().equals(quote.getTextId())) {
                return true;
            }
        }
        return false;
    }

    public Recipient getBotRecipient() {
        return botRecipient;
    }

    public String getStringRandomQuestion() {
        String result = ChatBotApplication.instance().getString(R.string.no_results_ask_huggy);
        if (questionsList.size() > 0) {
            Quote randomQuestion = questionsList.get(new Random().nextInt(questionsList.size()));
            questionsList.remove(randomQuestion);
            AnalyticsHelper.sendEvent(AnalyticsHelper.Categories.APP, AnalyticsHelper.Events.HUGGY_ASK_FOR_QUESTION, randomQuestion.getPrototypeId(), randomQuestion.getContent());
            AnalyticsHelper.setImageTextContext("Question");
            result = randomQuestion.getContent();
        }
        return result;
    }

    public void openMessagePreview(AppCompatActivity activity, Quote quote) {
        Intent intent = new Intent(activity, PicturesRecommendationActivity.class);
        intent.putExtra(PicturesRecommendationActivity.QUOTE_ID, quote.getTextId());
        intent.putExtra(PicturesRecommendationActivity.QUOTE_PROTOTYPE_ID, quote.getPrototypeId());
        intent.putExtra(PicturesRecommendationActivity.QUOTE_TEXT, quote.getContent());
        activity.startActivity(intent);
    }

    public void openMessagePreview(AppCompatActivity activity, ChatMessage.BotMessage botMessage) {
        Quote quote = new Quote();
        quote.setTextId(botMessage.getTextId());
        quote.setPrototypeId(botMessage.getPrototypeId());
        quote.setContent(botMessage.getContent());
        Utils.shareSticker(activity, botMessage.getImageLink(), quote);
    }

    public String getFilteredPicture(List<String> pictures) {
        for (String item : pictures) {
            if (!isUsedPicture(item)) {
                return item;
            }
        }
        return null;
    }

    public String getFilteredPictureFromPopularPictures(List<PopularImages> pictures) {
        for (PopularImages item : pictures) {
            if (!isUsedPicture(item.getImages().get(0).getImageLink())) {
                return item.getImages().get(0).getImageLink();
            }
        }
        return null;
    }

    public Quote getFilteredText(List<PopularTexts.Text> quotes) {
        for (PopularTexts.Text quote : quotes) {
            if (!isUsedPicture(quote.quote.getTextId())) {
                return quote.quote;
            }
        }
        return null;
    }

    public Quote getFilteredTextPopular(List<PopularTexts> texts) {
        for (Quote quote : texts.get(0).getTexts()) {
            if (!isUsedPicture(quote.getTextId())) {
                return quote;
            }
        }
        return null;
    }

    public UserProfile getFilteredUserProfile(List<UserProfile> userProfiles) {
        if (userProfiles != null) {
            for (UserProfile item : userProfiles) {
                if (!isUsedUser(item)) {
                    return item;
                }
            }
        }
        return null;
    }

    public ChatMessage.BotMessage getFilteredMessage(List<ChatMessage.BotMessage> messages) {
        if (messages != null) {
            for (ChatMessage.BotMessage botMessage : messages) {
                if (!isUsedMessage(botMessage)) {
                    return botMessage;
                }
            }
        }
        return null;
    }

    public ChatMessage.BotMessage getFilteredRecommendation(List<DailySuggestion> suggestions) {
        if (suggestions != null) {
            for (DailySuggestion dailySuggestion : suggestions) {
                if (!isUsedSuggestion(dailySuggestion)) {
                    usedSuggestions.add(dailySuggestion.getTextId());
                    return new ChatMessage.BotMessage(dailySuggestion);
                }
            }
        }
        return null;
    }

    private boolean isUsedUser(UserProfile userProfile) {
        for (String usedUserId : usedUsers) {
            if (usedUserId.equals(userProfile.getDeviceId())) {
                return true;
            }
        }
        usedUsers.add(userProfile.getDeviceId());
        return false;
    }

    private boolean isUsedMessage(ChatMessage.BotMessage message) {
        for (String questionId : usedQuestions) {
            if (message.getTextId().equals(questionId)) {
                return true;
            }
        }
        usedQuestions.add(message.getTextId());
        return false;
    }

    private boolean isUsedSuggestion(DailySuggestion dailySuggestion) {
        for (String questionId : usedSuggestions) {
            if (dailySuggestion.getTextId().equals(questionId)) {
                return true;
            }
        }
        return false;
    }

    private boolean isUsedPicture(String picture) {
        for (String item : usedImages) {
            if (picture.equals(item)) {
                return true;
            }
        }
        usedImages.add(picture);
        return false;
    }

}