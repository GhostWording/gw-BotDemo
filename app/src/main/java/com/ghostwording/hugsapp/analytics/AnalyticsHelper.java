package com.ghostwording.hugsapp.analytics;

import android.content.SharedPreferences;
import android.content.res.Resources;

import com.ghostwording.hugsapp.io.ApiClient;
import com.ghostwording.hugsapp.model.Location;
import com.ghostwording.hugsapp.model.PictureSource;
import com.ghostwording.hugsapp.model.texts.Quote;
import com.ghostwording.hugsapp.utils.Logger;
import com.ghostwording.hugsapp.utils.PrefManager;
import com.ghostwording.hugsapp.utils.Utils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AnalyticsHelper {

    public static String[] RELATIONSHIP_TYPE_STRINGS = {"Single", "InACouple"};
    public static String[] NOTIFICATION_TYPE_STRINGS = {"EveryDay", "EveryTwoDays", "EveryWeek"};
    public static String[] AGE_RANGE_STRINGS = {"Less18", "18-39", "40-64", "64more"};

    public interface Categories {
        String APP = "App";
        String TEXT = "Text";
        String IMAGE = "Image";
        String INTENTION = "Intention";
        String THEME = "Theme";
        String USER = "User";
        String SURVEY = "Survey";
    }

    public interface Events {
        //app
        String APP_LAUNCH = "AppLaunch";
        String RESUME_ACTIVITY = "ResumeActivity";
        String NOTIFICATION_RECEIVED = "NotificationReceived";
        String FIRST_LAUNCH = "FirstLaunch";
        String SET_LANGUAGE = "SetLanguage";
        String USER_GENDER = "Gender";
        String READ_VARIATION = "ReadVariation";
        String APP_INSTALLED_BY_LINK = "AppInstalledByLink";
        String EMAIl = "UserEmail";
        String SEND_MENU_OPENED = "SendMenuOpened";
        String BACK_FROM_IMAGE = "BackFromImage";
        String BACK_FROM_THEMES = "BackFromThemes";
        String OPEN_MBTI = "OpenMBTI";
        String MBTI_CHOOSE = "MBTISelected";
        String SET_FACEBOOK_ID = "SetFacebookId";
        String FACEBOOK_AGE_RANDE = "FacebookAgeRange";
        String FACEBOOK_FIRSTNAME = "FacebookFirstName";
        String FACEBOOK_CONNECTED = "FacebookInstalled";
        String PULL_TO_REFRESH = "PullToRefresh";
        String TRANSLATE_TO = "TranslateTo";
        String NOTIFICATION_SUBSCRIBE = "NotificationSubscribe";
        String SEARCH = "Search";
        String SEARCH_TEXT_SELECTED = "SearchTextSelected";
        String ASK_HUGGY_CLICKED = "AskHuggyClicked";
        String SURVEY_HUGGY_CLICKED = "SurveyBotClicked";
        String HUGGY_USER_INPUT = "HuggyReceivesUserInput";
        String ASK_HUGGY_MESSAGE_CLICKED = "AskHuggyForwardWithClick";
        String HUGGY_SEQUENCE_START = "HuggySequenceStart";
        String HUGGY_SEQUENCE_NEXT = "HuggySequenceNext";
        String HUGGY_ASK_FOR_QUESTION = "HuggyAsksAQuestion";
        String HUGGY_REPLY = "HuggyRepliesToUserInput";
        String HUGGY_COMMAND_SELECTED = "HuggyCommandBarOptionSelected";
        String DEEP_LINK = "DeepLink";
        String CHOOSE_NB_COLUMNS = "ChooseNbColumns";
        String STICKER_PALS_SELECT = "UserPalSelect";
        String DEVELOPER_MODE = "ppDeveloperMode";
        String ONBOARDING_SHOW = "OnboardingBotShowed";
        String ONBOARDING_CLOSED = "OnboardingBotClosed";
        String ONBOARDING_STARTED = "OnboardingBotStarted";

        //GIF TAB
        String GIF_CREATE_CLICKED = "GifCreateClicked";
        String GIF_EDIT_CLICKED = "GifEditClicked";
        String GIF_SEND_CLICKED = "GifSendClicked";
        String PICK_CATEGORY = "PickCategory";
        String GIF_CATEGORIES_OPEN = "GifCategoriesOpen";
        String GIF_CATEGORIES_PREVIEW = "GifCategoriesPreviewOpen";
        String GIF_CATEGORIES_SEND = "GifCategoriesSend";

        String SHARE_VIA_INTENT = "ShareViaIntent";
        String SHARE_MESSENGER = "SendMessenger";
        String SHARE_FACEBOOK_WALL = "ShareOnFbWall";
        String SHARE_WHATS_APP = "ShareWhatsApp";
        String SHARE_APP_WHATS_APP = "AppShareToWhatsApp";
        String SHARE_APP_TWITTER = "AppShareToTwitter";
        String SHARE_SKYPE = "ShareSkype";
        String SHARE_VIBER = "ShareViber";
        String SHARE_WECHAT = "ShareWechat";
        String SHARE_TWITTER = "ShareTwitter";
        String SHARE_SNAPCHAT = "ShareSnapchat";
        String SHARE_INSTAGRAMM = "ShareInstagramm";
        String SHARE_LINE = "ShareLine";
        String LINK_EVENTS = "LinkEvents";
        String SHARE_EMAIL = "ShareEmail";
        String SHARE_SMS = "ShareSMS";
        String QUIZ_SELECT = "QuizSelect";
        String QUIZ_OPENED = "QuizOpened";
        String APP_RATING = "AppRating";
        String WALLPAPER = "UseAsWallpaper";

        String POPULAR_IMAGE_OPEN = "PopularImagesOpen";
        String POPULAR_TEXT_OPEN = "PopularTextsOpen";

        String MOOD_INTENTION = "MoodIntention";
        String MOOD_THEME = "MoodTheme";

        String OPEN_SIDE_MENU = "OpenSideMenu";
        String LOGIN_WITH_FACEBOOK = "LoginWithFacebook";
        String ACCEPT_NOTIFICATIONS = "AcceptNotifications";
        String LOGIN_WITHOUT_FACEBOOK = "LoginWithoutFacebook";
        String EDIT_TEXT = "EditText";
        String OPTION_MENU = "OptionMenu";
        String STORE_RATING = "StoreRating";
        String FACEBOOK_MESSENGER_REPLY = "ReplyFromMessenger";
        String IMAGE_RECOMMEND = "ImageRecommendClicked";
        String TAB_SELECTED = "SelectTab";
        String FACEBOOK_LOGIN_CANCELED = "FacebookLoginCanceled";
        String SEND_LATER = "SendLater";
        String SETUP_REMINDER = "SetupReminder";
        String THANKS_HUGGY = "ThanksHuggy";

        //one time events
        String MAIN_SCREEN_REACHED = "MainScreenReached";
        String FIRST_QUESTION_REACHED = "FirstQuestionReached";
        String FIRST_QUESTION_ANSWERED = "FirstQuestionAnswered";
        String FIRST_SCREEN_ACTION = "FirstMoodItemOrSelectTab";

        //Edit quote
        String PROMOTE_APP = "PromoteAppClicked";
        String COUNTRY = "Country";
        String SEND_WITH_DELAY_CLICKED = "SendWithDelayNotificationClicked";

        //Settings
        String RELATIONSHIP_CHANGED = "ConjugalSituation";
        String NOTIFICATION_CHANGED = "NotificationFrequency";

        //Popular
        String POPULAR_IMAGE_SELECTED = "PopularImageSelected";
        String POPULAR_TEXT_SELECTED = "PopularTextSelected";
        String IMAGE_SELECTED = "ImageSelected";
        String TEXT_ADDED = "TextAdded";

        //Text suggestion
        String USER_FEEDBACK = "UserFeedback";

        //Help Us
        String HELP_US_OPEN = "HelpUsOpen";
        String INVITE_FRIENDS = "InviteFriends";
        String SHARE_APP_ON_FACEBOOK = "ShareAppOnFacebook";

        //StickersPals
        String STICKERS_DIALOG_LOGIN_FACEBOOK = "LoginFacebook";
        String USER_PROFILE_SHOW = "ProfileShow";
        String FILTER_PALS = "UserPalOpenFilters";
        String FILTER_GENDER = "UserPalFilterGender";
        String FILTER_TRAIT = "UserPalFiltering";
        String SEND_MESSAGE = "UserPalStartSending";
        String PALS_SEND_TEXT = "UserPalSendText";
        String PALS_SEND_IMAGE = "UserPalSendImage";
        String PALS_LINK_EVENTS = "UserPalLinkEvents";
        String FILTER_COUNTRY = "UserPalSameCountry";

        String FAVORITES_OPENED = "FavoritesOpened";
        String TRANSLATION_OPENED = "TranslationOpened";
        String FIRST_MBTI_KNOWLEDGE = "InitialMBTIKnowledge";
        String MBTI_YES_NO = "MBTIYesOrNo";
        String FIRST_MOOD_MENU_ITEM_PRESSED = "FirstMoodItemPressed";

        //MBTI
        String MBTI_FB_PROFILE = "SameProfileFbPhoto";
        String MBTI_STICKERS = "SameProfilePopularImages";
        String MBTI_MESSAGES = "SameProfilePopularTexts";
        String MBTI_OPPOSITE_FB = "OppositeProfileFbPhoto";
        String MBTI_OPPOSITE_STICKERS = "OppositeProfilePopularImages";
        String MBTI_OPPOSITE_MESSAGES = "OppositeProfilePopularTexts";

        //USER
        String USER_FLOWER = "UserFlower";
        String USER_ANIMAL = "UserAnimal";
        String USER_LANDSCAPE = "UserLandscape";
        String USER_DESCRIPTION = "UserDescriptionPrototypeId";
        String USER_PARTICIPATE_BATTLE_STICKERS = "ppParticipateBattleStickers";
        String LANGUAGE = "ppLanguage";
        String USER_AGE = "ppAge";
        String USER_GENERATION = "ppGeneration";

        //Unlock stickers
        String STICKERS_UNLOCK_OPENED = "StickersUnlockOpened";
        String THEME_TO_UNLOCK_CHOSEN = "ThemeToUnlockChosen";
        String UNLOCK_BY_GUESSING = "UnlockByGuessing";
        String UNLOCK_WITH_QUESTIONS = "UnlockWithQuestions";
        String THEME_UNLOCKED = "ThemeUnlocked";

        //Facebook messenger integration
        String REPLY_IMAGE_FROM = "ImageReceivedFrom";
        String REPLY_TEXT_FROM = "TextReceivedFrom";

        //Game
        String GAME_CATEGORY = "GameCategory";
        String GAME_CHOOSE_USER = "GameTargetUser";

        //Pick
        String PICK_INTENTION = "PickIntention";
        String PICK_RECIPIENT = "PickRecipient";
        String SELECT_INTENTION = "SelectIntention";

        String DAILY_SUGGESTIONS_REACHED = "DailyIdeasReached";
        String UNLOCK_BY_DONATION = "UnlockByDonating";
        String PLAY_TEXT = "HearMessage";
        String SWIPE_LANGUAGE = "SwipeLanguage";
        String DAILY_SUGGESTION_CLICKED = "DailyIdeasSent";

        String SET_SHARING_MODE = "SetSharingMode";
        String BATTLE_STICKERS_REGISTER = "BattleStickersRegister";
        String SURVEY_CHOICE = "SurveyChoice";

        //ADS
        String AD_REQUESTED = "AdRequested";
        String AD_DISPLAYED = "AdDisplayed";
        String AD_DISMISSED = "AdDismissed";
        String AD_ERROR = "AdError";
        String AD_LOADED = "AdLoaded";
        String AD_CLICKED = "AdClicked";
        String AD_USER_SEE = "UserOpenAppSeeAdvert";
        String AD_USER_CANCEL = "AdReceivedNoTimeShow";
        String AD_SCREEN_SHOW = "TurtleScreenShown";
        String AD_SCREEN_CLOSE = "TurtleScreenHidden";
        String AD_NOT_REQUESTED = "AdNotRequested";

        //GIF
        String GIF_COMPOSER_OPEN = "GifComposerOpen";
        String GIF_PREVIEW_OPEN = "GifPreviewOpen";
        String GIF_TRENDING_OPEN = "GifTrendingPreviewOpen";
        String GIF_IMAGE_SELECTED = "GifImageSelected";
        String GIF_SEND = "GifSend";
        String GIF_TRENDING_SEND = "GifTrendingSend";
        String ADDITIONAL_IMAGE_RECOMMENDATION = "AdditionalImageRecommendation";
        String SWIPE_CARD_LIKE = "SwipeCardLike";
        String SWIPE_CARD_DISLIKE = "SwipeCardDislike";
        String AD_ERROR_NO_FILL = "AddErrorNoFill";
        String AD_FALLBACK_CALLED = "FallbackPlacementCalled";
        String PROMO_TAB_CLICKED = "PromoTabClicked";
        String GIF_CATEGORY_OPEN = "GifCategoriesPreviewOpen";
        String BOT_SKIP_QUESTION = "BotSkipQuestion";
        String POWER_USER = "PowerUser";
        String FAB_ONBOARDING = "FloatingOnBoardingTriggered";
        String HUGS_VIEW_REACHED = "ViewINeedHugs";
        String SEND_HUG_REACHED = "ViewSendAHug";
        String TALK_TO_ME_REACHED = "ViewTalkToMe";
    }

    public interface Parameters {
        String EMBEDDED_IMAGE = "EmbeddedImage";
        String SEPARATE_IMAGE = "SeparateImage";
        String NEW_PICTURE = "NewPicture";
        String OWN_PICTURE = "OwnPicture";
        String USER_PHOTO = "UserPhoto";
        String TEXT_PLUS_IMAGE = "TextPlusImage";
        String TEXT_IN_IMAGE = "TextInImage";
        String YES = "Yes";
        String NO = "No";
        String PROPERTY_PREFIX = "pp";
    }

    public interface ImageTextContexts {
        String NORMAL = "Normal";
        String POPULAR = "Popular";
        String MATCHING = "Matching";
        String STICKERS = "Stickers";
        String POPULAR_FIRST = "MostPopularFirst";
        String DAILY = "DailySuggestions";
    }

    private static String sImageTextContext;
    private static String sSelectedIntentionId = "undefined";
    private static String sScreenName = "";
    private static boolean sIsLast = false;

    public static void setIsLast(boolean isLast) {
        sIsLast = isLast;
    }

    public static boolean isLast() {
        return sIsLast;
    }

    public static void setScreenName(String screenName) {
        sScreenName = screenName;
    }

    public static void setImageTextContext(String imageTextContext) {
        sImageTextContext = imageTextContext;
    }

    public static String getImageTextContext() {
        return sImageTextContext;
    }

    public static void setSelectedIntentionId(String selectedIntentionId) {
        sSelectedIntentionId = selectedIntentionId;
    }

    public static String getSelectedIntentionId() {
        return sSelectedIntentionId;
    }

    public static void sendOneTimeEvent(String eventName) {
        SharedPreferences preferences = PrefManager.instance().getPreferences();
        if (!preferences.getBoolean(eventName, false)) {
            preferences.edit().putBoolean(eventName, true).apply();
            sendEvent(eventName);
        }
    }

    public static void sendOneTimeEvent(String eventName, String additionalParameter) {
        SharedPreferences preferences = PrefManager.instance().getPreferences();
        if (!preferences.getBoolean(eventName, false)) {
            preferences.edit().putBoolean(eventName, true).apply();
            sendEvent(Categories.APP, eventName, additionalParameter);
        }
    }

    public static void sendEvent(String eventName) {
        sendEvent(Categories.APP, eventName, null);
    }

    public static void sendEvent(String eventName, String value) {
        if (eventName.equals(Events.USER_AGE) && (value.equals("Less18") || value.equals("18-39"))) {
            sendEvent(Events.USER_GENERATION, "Young");
        }
        if (eventName.equals(Events.USER_AGE) && (value.equals("40-64") || value.equals("64more"))) {
            sendEvent(Events.USER_GENERATION, "Old");
        }
        sendEvent(Categories.APP, eventName, value);
    }

    public static void sendEvent(String categoryName, String eventName, String label) {
        sendEvent(categoryName, eventName, label, null);
    }

    public static void sendEvent(String categoryName, String eventName, String label, String targetParameter, String additionalParameter) {
        final String EVENT_CATEGORY = "Category";
        final String EVENT_LABEL = "Label";
        final String EVENT_TARGET_PARAMETER = "TargetParameter";

        Logger.i("Analytics event : " + eventName + " : " + label + " " + targetParameter + " " + sImageTextContext);

        //custom server analytics
        EventModel eventModel = new EventModel()
                .setTargetType(categoryName)
                .setActionType(eventName)
                .setRecipientId(additionalParameter)
                .setActionLocation(sScreenName)
                .setTargetId(label)
                .setIntentionId(sSelectedIntentionId)
                .setTargetParameter(targetParameter);
        eventModel.setContext(sImageTextContext);
        GhostAnalytics.instance().logEvent(eventModel);
    }

    public static void sendEvent(String categoryName, String eventName, String label, String targetParameter) {
        sendEvent(categoryName, eventName, label, targetParameter, null);
    }

    public static void sendShareEvent(String event, Quote quote, String imageUri, PictureSource pictureSource, String additionalParameter) {
        if (quote != null) {
            AnalyticsHelper.sendEvent(AnalyticsHelper.Categories.TEXT, event, quote.getTextId(), additionalParameter);
            if (quote.getIntentionId() != null) {
                AnalyticsHelper.setSelectedIntentionId(quote.getIntentionId());
            }
        }
        if (imageUri != null) {
            String imageName = Utils.getFilenameFromUri(imageUri);
            AnalyticsHelper.sendShareImageEvent(event, pictureSource, imageName, additionalParameter);
            if (quote != null) {
                AnalyticsHelper.sendEvent(AnalyticsHelper.Categories.APP, AnalyticsHelper.Events.LINK_EVENTS, quote.getTextId(), imageUri.contains("file") ? Parameters.USER_PHOTO : imageName);
            }
        }
    }

    public static void sendShareImageEvent(String event, PictureSource pictureSource, String selectedImageUri, String additionalParameter) {
        String targetId = null;

        switch (pictureSource) {
            case CAMERA:
                targetId = Parameters.NEW_PICTURE;
                break;
            case GALLERY:
                targetId = Parameters.OWN_PICTURE;
                break;
            case SERVER:
                targetId = selectedImageUri;
                break;
        }

        if (additionalParameter == null) {
            AnalyticsHelper.sendEvent(AnalyticsHelper.Categories.IMAGE, event, targetId);
        } else {
            AnalyticsHelper.sendEvent(AnalyticsHelper.Categories.IMAGE, event, targetId, additionalParameter);
        }
    }

    public static void sendCurrentLocation() {
        ApiClient.getCountryService().getLocationInformation().enqueue(new Callback<Location>() {
            @Override
            public void onResponse(Call<Location> call, Response<Location> response) {
                if (response.isSuccessful()) {
                    PrefManager.instance().setUserCountry(response.body().getCountry());
                    AnalyticsHelper.sendEvent(Categories.APP, Events.COUNTRY, response.body().getCountry(), Resources.getSystem().getConfiguration().locale.getLanguage());
                }
            }

            @Override
            public void onFailure(Call<Location> call, Throwable t) {

            }
        });
    }

}
