package com.ghostwording.chatbot.chatbot.model;

import com.ghostwording.chatbot.model.DailySuggestion;
import com.ghostwording.chatbot.model.GifResponse;
import com.ghostwording.chatbot.model.SuggestionsModel;
import com.ghostwording.chatbot.model.UserProfile;
import com.ghostwording.chatbot.model.YoutubeVideo;
import com.ghostwording.chatbot.model.texts.Quote;
import com.ghostwording.chatbot.utils.AppConfiguration;
import com.ghostwording.chatbot.utils.PrefManager;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import static com.ghostwording.chatbot.analytics.AnalyticsHelper.Events.USER_AGE;

public class ChatMessage {

    public enum ChatMessageType {
        CAROUSEL, IMAGE, IMAGE_FULL_SCREEN, GIF_IMAGE, GIF_IMAGE_FULL_SCREEN, VIDEO, LINK, TEXT, SEQUENCE, CARD, QUOTE
    }

    private BotMessage botMessage;
    private String message;
    private GifImageModel gifMessage;
    private YoutubeVideo youtubeVideo;
    private Link link;
    private BotSequence botSequence;
    private SuggestionsModel suggestionsModel;
    private CarouselMessage carouselMessage;
    private ImageMessage imageMessage;
    private Quote quote;
    private BotSequence.Step step;

    public final boolean isSelf;
    public final ChatMessageType messageType;

    public ImageMessage getImageMessage() {
        return imageMessage;
    }

    public SuggestionsModel getSuggestionsModel() {
        return suggestionsModel;
    }

    public BotSequence getBotSequence() {
        return botSequence;
    }

    public GifImageModel getGifMessage() {
        return gifMessage;
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

    public Quote getQuote() {
        return quote;
    }

    public ChatMessage(CarouselMessage carouselMessage) {
        isSelf = false;
        messageType = ChatMessageType.CAROUSEL;
        this.carouselMessage = carouselMessage;
    }

    public ChatMessage(Quote quote) {
        isSelf = false;
        messageType = ChatMessageType.QUOTE;
        this.quote = quote;
    }

    public ChatMessage(SuggestionsModel suggestionsModel) {
        isSelf = false;
        messageType = ChatMessageType.CAROUSEL;
        this.suggestionsModel = suggestionsModel;
    }

    public ChatMessage(YoutubeVideo youtubeVideo) {
        this.youtubeVideo = youtubeVideo;
        messageType = ChatMessageType.VIDEO;
        isSelf = false;
    }

    public ChatMessage(Link link) {
        messageType = ChatMessageType.LINK;
        this.link = link;
        this.isSelf = false;
    }

    public ChatMessage(String message, boolean isSelf) {
        messageType = ChatMessageType.TEXT;
        this.message = message;
        this.isSelf = isSelf;
    }

    public ChatMessage(ImageMessage imageMessage) {
        if (imageMessage.fullWidth) {
            messageType = ChatMessageType.IMAGE_FULL_SCREEN;
        } else {
            messageType = ChatMessageType.IMAGE;
        }
        this.imageMessage = imageMessage;
        isSelf = false;
    }

    public ChatMessage(BotSequence botSequence, boolean isSelf) {
        messageType = ChatMessageType.SEQUENCE;
        this.isSelf = isSelf;
        this.botSequence = botSequence;
    }

    public ChatMessage(BotMessage botMessage) {
        this.botMessage = botMessage;
        messageType = ChatMessageType.CARD;
        isSelf = false;
    }

    public ChatMessage(GifImageModel gifImageModel) {
        if (gifImageModel.isFullWidth()) {
            messageType = ChatMessageType.GIF_IMAGE_FULL_SCREEN;
        } else {
            messageType = ChatMessageType.GIF_IMAGE;
        }
        gifMessage = gifImageModel;
        isSelf = false;
    }

    public BotSequence.Step getStep() {
        return step;
    }

    public void setStep(BotSequence.Step step) {
        this.step = step;
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

        public BotMessage(DailySuggestion dailySuggestion) {
            textId = dailySuggestion.getTextId();
            prototypeId = dailySuggestion.getPrototypeId();
            imageLink = dailySuggestion.getImageLink();
            content = dailySuggestion.getContent();
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
