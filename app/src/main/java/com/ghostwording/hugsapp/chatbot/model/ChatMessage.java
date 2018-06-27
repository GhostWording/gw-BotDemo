package com.ghostwording.hugsapp.chatbot.model;

import com.ghostwording.hugsapp.model.DailySuggestion;
import com.ghostwording.hugsapp.model.GifResponse;
import com.ghostwording.hugsapp.model.SuggestionsModel;
import com.ghostwording.hugsapp.model.UserProfile;
import com.ghostwording.hugsapp.model.YoutubeVideo;
import com.ghostwording.hugsapp.model.texts.Quote;
import com.ghostwording.hugsapp.utils.AppConfiguration;
import com.ghostwording.hugsapp.utils.PrefManager;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import static com.ghostwording.hugsapp.analytics.AnalyticsHelper.Events.USER_AGE;

public class ChatMessage {

    private BotMessage botMessage;
    private String message;
    private GifImageModel gifMessage;
    private UserProfile userProfile;
    private YoutubeVideo youtubeVideo;
    private Link link;
    private BotSequence botSequence;
    private SuggestionsModel suggestionsModel;
    private CarouselMessage carouselMessage;
    public final boolean isSelf;

    public SuggestionsModel getSuggestionsModel() {
        return suggestionsModel;
    }

    public BotSequence getBotSequence() {
        return botSequence;
    }

    public GifImageModel getGifMessage() {
        return gifMessage;
    }

    public UserProfile getUserProfile() {
        return userProfile;
    }

    public String getMessage() {
        return message;
    }

    public BotMessage getBotMessage() {
        return botMessage;
    }

    public YoutubeVideo getYoutubeVideo() {
        return youtubeVideo;
    }

    public CarouselMessage getCarouselMessage() {
        return carouselMessage;
    }

    public ChatMessage(CarouselMessage carouselMessage) {
        isSelf = false;
        this.carouselMessage = carouselMessage;
    }

    public ChatMessage(SuggestionsModel suggestionsModel) {
        isSelf = false;
        this.suggestionsModel = suggestionsModel;
    }

    public ChatMessage(BotSequence botSequence, boolean isSelf) {
        this.isSelf = isSelf;
        this.botSequence = botSequence;
    }

    public ChatMessage(YoutubeVideo youtubeVideo) {
        this.youtubeVideo = youtubeVideo;
        isSelf = false;
    }

    public ChatMessage(Link link) {
        this.link = link;
        this.isSelf = false;
    }

    public ChatMessage(String message, boolean isSelf) {
        this.message = message;
        this.isSelf = isSelf;
    }

    public ChatMessage(BotMessage botMessage) {
        this.botMessage = botMessage;
        isSelf = false;
    }

    public ChatMessage(GifImageModel gifImageModel) {
        gifMessage = gifImageModel;
        isSelf = false;
    }

    public ChatMessage(GifResponse.GifImage gifImage) {
        gifMessage = new GifImageModel(gifImage);
        isSelf = false;
    }

    public ChatMessage(UserProfile userProfile) {
        this.userProfile = userProfile;
        isSelf = false;
    }

    public Link getLink() {
        return link;
    }

    public static class BotMessage {
        @SerializedName("IntentionId")
        @Expose
        private String intentionId;
        @SerializedName("PrototypeId")
        @Expose
        private String prototypeId;
        @SerializedName("TextId")
        @Expose
        private String textId;
        @SerializedName("Content")
        @Expose
        private String content;
        @SerializedName("ImageName")
        @Expose
        private String imageName;
        @SerializedName("ImageLink")
        @Expose
        private String imageLink;
        @SerializedName("Context")
        @Expose
        private Context context;

        public BotMessage() {

        }

        public BotMessage(String imageLink) {
            this.imageLink = imageLink;
        }

        public BotMessage(DailySuggestion dailySuggestion) {
            textId = dailySuggestion.getTextId();
            prototypeId = dailySuggestion.getPrototypeId();
            imageLink = dailySuggestion.getImageLink();
            content = dailySuggestion.getContent();
        }

        public BotMessage(Quote quote) {
            intentionId = quote.getIntentionId();
            prototypeId = quote.getPrototypeId();
            textId = quote.getTextId();
            content = quote.getContent();
        }

        /**
         * @return The intentionId
         */
        public String getIntentionId() {
            return intentionId;
        }

        /**
         * @param intentionId The IntentionId
         */
        public void setIntentionId(String intentionId) {
            this.intentionId = intentionId;
        }

        /**
         * @return The prototypeId
         */
        public String getPrototypeId() {
            return prototypeId;
        }

        /**
         * @param prototypeId The PrototypeId
         */
        public void setPrototypeId(String prototypeId) {
            this.prototypeId = prototypeId;
        }

        /**
         * @return The textId
         */
        public String getTextId() {
            return textId;
        }

        /**
         * @param textId The TextId
         */
        public void setTextId(String textId) {
            this.textId = textId;
        }

        /**
         * @return The content
         */
        public String getContent() {
            return content;
        }

        /**
         * @param content The Content
         */
        public void setContent(String content) {
            this.content = content;
        }

        /**
         * @return The imageName
         */
        public String getImageName() {
            return imageName;
        }

        /**
         * @param imageName The ImageName
         */
        public void setImageName(String imageName) {
            this.imageName = imageName;
        }

        /**
         * @return The imageLink
         */
        public String getImageLink() {
            return imageLink;
        }

        /**
         * @param imageLink The ImageLink
         */
        public void setImageLink(String imageLink) {
            this.imageLink = imageLink;
        }

        /**
         * @return The context
         */
        public Context getContext() {
            return context;
        }

        /**
         * @param context The Context
         */
        public void setContext(Context context) {
            this.context = context;
        }

    }

    public class Context {

        @SerializedName("status")
        @Expose
        private String status;
        @SerializedName("type")
        @Expose
        private String type;
        @SerializedName("image")
        @Expose
        private String image;

        /**
         * @return The status
         */
        public String getStatus() {
            return status;
        }

        /**
         * @param status The status
         */
        public void setStatus(String status) {
            this.status = status;
        }

        /**
         * @return The type
         */
        public String getType() {
            return type;
        }

        /**
         * @param type The type
         */
        public void setType(String type) {
            this.type = type;
        }

        /**
         * @return The image
         */
        public String getImage() {
            return image;
        }

        /**
         * @param image The image
         */
        public void setImage(String image) {
            this.image = image;
        }

    }

    public static class VoteMessage {
        private static final String VALUE = "{value}";
        private static final int MIN_DIFFERENCE = 6;

        public final BotSequence question;
        public final BotSequence answer;
        public final int percentageOverAll;
        public final int countVotes;
        private int percentGender;
        private int percentCountry;
        private int percentMaturity;

        public VoteMessage(BotSequence question, BotSequence answer, int percentage, int countVotes) {
            this.question = question;
            this.answer = answer;
            this.percentageOverAll = percentage;
            this.countVotes = countVotes;
        }

        public boolean isShowCountryPercent() {
            return Math.abs(percentageOverAll - percentCountry) >= MIN_DIFFERENCE;
        }

        public boolean isShowGenderPercent() {
            return Math.abs(percentageOverAll - percentGender) >= MIN_DIFFERENCE;
        }

        public boolean isShowMaturityPercent() {
            return Math.abs(percentageOverAll - percentMaturity) >= MIN_DIFFERENCE && PrefManager.instance().getUserAnswer(USER_AGE) != null;
        }

        public void setPercentGender(Integer percentGender) {
            this.percentGender = percentGender;
        }

        public void setPercentCountry(Integer percentCountry) {
            this.percentCountry = percentCountry;
        }

        public void setPercentMaturity(Integer percentMaturity) {
            this.percentMaturity = percentMaturity;
        }

        public String getPercentOverall() {
            return AppConfiguration.getSurveyBotStrings().getSurveyBot().get(1).getLabel().replace(VALUE, String.valueOf(percentageOverAll));
        }

        public String getPercentGender() {
            if (PrefManager.instance().getSelectedGender() == 0) {
                return AppConfiguration.getSurveyBotStrings().getSurveyBot().get(2).getLabel().replace(VALUE, String.valueOf(percentGender));
            } else {
                return AppConfiguration.getSurveyBotStrings().getSurveyBot().get(3).getLabel().replace(VALUE, String.valueOf(percentGender));
            }
        }

        public String getPercentCountry() {
            return AppConfiguration.getSurveyBotStrings().getSurveyBot().get(4).getLabel()
                    .replace(VALUE, String.valueOf(percentCountry))
                    .replace("{country}", PrefManager.instance().getUserCountry());
        }

        public String getPercentMaturity() {
            if (PrefManager.instance().isUserOld()) {
                return AppConfiguration.getSurveyBotStrings().getSurveyBot().get(6).getLabel().replace(VALUE, String.valueOf(percentMaturity));
            } else {
                return AppConfiguration.getSurveyBotStrings().getSurveyBot().get(5).getLabel().replace(VALUE, String.valueOf(percentMaturity));
            }
        }

        public String getCountVotes() {
            return AppConfiguration.getSurveyBotStrings().getSurveyBot().get(0).getLabel().replace(VALUE, String.valueOf(countVotes));
        }
    }

    public static class Link {

        public final BotSequence.Step link;

        public Link(BotSequence.Step link) {
            this.link = link;
        }

    }
}
