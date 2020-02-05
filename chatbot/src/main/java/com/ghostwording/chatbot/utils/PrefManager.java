package com.ghostwording.chatbot.utils;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.ghostwording.chatbot.analytics.AnalyticsHelper;
import com.ghostwording.chatbot.chatbot.model.BotSequence;
import com.ghostwording.chatbot.io.ApiClient;
import com.ghostwording.chatbot.io.DataManager;
import com.ghostwording.chatbot.model.NotificationsModel;
import com.ghostwording.chatbot.model.recipients.Recipient;
import com.ghostwording.chatbot.model.requests.UserProperty;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;

public class PrefManager {

    private static final String PREF_NAME = "QuotePreferences";

    private static final String KEY_RECEIVE_NOTIFICATIONS = "receive_notifications";
    private static final String KEY_APP_FIRST_LAUNCH = "first_launch";
    private static final String KEY_SHOW_PREVIEW = "show_preview";
    private static final String KEY_AGE = "age";
    private static final String KEY_GENDER = "gender";
    private static final String KEY_LOG_IN = "login";
    private static final String KEY_NOTIFICATIONS = "notifications_storage";
    private static final String KEY_NOTIFICATION_FREQUENCY = "notification_frequency";
    private static final String KEY_RELATIONSHIP_SETTINGS = "relationship_settings";
    private static final String KEY_APP_FIRST_LAUNCH_TIME = "first_launch_time";
    private static final String KEY_EXPERIMENT_ID = "experiment_id";
    private static final String KEY_VARIATION_ID = "experiment_variation_id";
    private static final String KEY_FACEBOOK_ID = "facebook_id";
    private static final String KEY_NEW_INSTALL = "is_new_install";
    private static final String KEY_APP_VERSION = "app_version";
    private static final String KEY_LAST_HUG_TIME = "last_hug_time";
    private static final String KEY_RELATION_TYPE = "relation_type";
    private static final String KEY_RECIPIENT_TYPE = "recipient_type";
    private static final String KEY_RECIPIENT_GENDER = "recipient_gender";
    private static final String KEY_ACTIVE_RECIPIENTS = "active_recipients";
    private static final String KEY_RECIPIENT_PREFIX = "recipient_";
    private static final String KEY_RECIPIENT_PREFIX_NOTIFICATION = "recipient_notification_";
    private static final String KEY_INTENTION_PREFIX = "intention_";
    private static final String KEY_RECIPIENT_POLITE_FORM = "recipient_polite_form";
    private static final String KEY_POPULAR_FEATURE_ENABLED = "popular_feature_enabled";
    private static final String KEY_MBTI_GROUP = "mbti_group";
    private static final String KEY_FIRST_TIME_PREFIX = "first_time_";
    private static final String KEY_APP_RATING = "key_app_rating";
    private static final String KEY_USER_COUNTRY = "key_user_country";
    private static final String KEY_BLOCK_THEME_PREFIX = "locked_theme_";
    private static final String KEY_USER_ANIMAL = "user_animal";
    private static final String KEY_USER_LANDSCAPE = "user_landscape";
    private static final String KEY_USER_FLOWER = "user_flower";
    private static final String KEY_USER_DESCRIPTION = "user_description";
    private static final String KEY_ALL_THEMES_UNLOCKED = "all_themes_unlocked";
    private static final String KEY_QUESTION_PREFIX = "mbti_question_";
    private static final String KEY_GAME_TUTORIAL_WAS_SHOWN = "game_tutorial_was_shown";
    private static final String KEY_SHARING_MODE = "sharing_mode";
    private static final String KEY_FIRST_TIME_POPULAR = "first_time_popular";
    private static final String KEY_LAST_LANGUAGE = "last_language";
    private static final String KEY_DAILY_MOTHER = "daily_mother";
    private static final String KEY_DAILY_FRIEND = "daily_friend";
    private static final String KEY_DAILY_FACEBOOK_FRIEND = "daily_facebook_friend";
    private static final String KEY_DAILY_SWEETHEART = "daily_sweetheart";
    private static final String KEY_HUMOR_MODE = "humour_mode";
    private static final String KEY_FIRST_NAME = "first_name";
    private static final String KEY_FILTER_QUESTION = "filter_question";
    private static final String KEY_FILTER_COUNTRY = "filter_country";
    private static final String KEY_FILTER_WOMAN = "filter_woman";
    private static final String KEY_FILTER_MAN = "filter_man";
    private static final String KEY_GALLERY_MODE = "gallery_mode";
    private static final String KEY_COUNT_SHARED = "count_shared";
    private static final String KEY_COUNT_ANSWERED = "count_answered";
    private static final String KEY_LAST_INCOMING_MESSAGE_TIME = "last_incoming_message_time";
    private static final String KEY_LAST_HUGGY_SHOW_TIME = "last_huggy_show_time_updated";
    private static final String KEY_SEND_DAILY = "send_daily";
    private static final String KEY_MESSAGE_PREFIX = "message_";
    private static final String KEY_COUNTER_PREFIX = "counter_";
    private static final String KEY_LAST_SEQUENCE_ID_PREFIX = "last_sequence_id_";
    private static final String KEY_HUGGY_SHOW = "huggy_show";

    private static PrefManager instance;
    private SharedPreferences preferences;

    public static void createInstance(Context context) {
        if (instance == null) {
            instance = new PrefManager(context);
        }
    }

    public static PrefManager instance() {
        return instance;
    }

    private PrefManager(Context context) {
        preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);

        //check if user update app
        String storedAppVersion = preferences.getString(KEY_APP_VERSION, null);
        if (storedAppVersion == null) {
            preferences.edit().putString(KEY_APP_VERSION, AppConfiguration.getAppVersionNumber()).apply();
        } else {
            if (!storedAppVersion.equals(AppConfiguration.getAppVersionNumber())) {
                preferences.edit().putBoolean(KEY_NEW_INSTALL, false).apply();
            }
        }
    }

    public String getGenderString() {
        return preferences.getInt(KEY_GENDER, -1) == 0 ? "Men" : "Women";
    }

    public String getGenderStringMaleFormat() {
        return preferences.getInt(KEY_GENDER, -1) == 0 ? "male" : "female";
    }

    public Boolean isHuggyShown() {
        return preferences.getBoolean(KEY_HUGGY_SHOW, false);
    }

    public void setHuggyShown(boolean huggyShown) {
        preferences.edit().putBoolean(KEY_HUGGY_SHOW, huggyShown).apply();
    }

    public String getUserMaturity() {
        return isUserOld() ? "40AndOver" : "LessThan40";
    }

    public boolean isMessageReaded(String messageId) {
        return preferences.getBoolean(KEY_MESSAGE_PREFIX + messageId, false);
    }

    public void setMessageReaded(String messageId) {
        preferences.edit().putBoolean(KEY_MESSAGE_PREFIX + messageId, true).apply();
    }

    public boolean isSendDaily() {
        return preferences.getBoolean(KEY_SEND_DAILY, false);
    }

    public void setSendDaily(boolean isSendDaily) {
        preferences.edit().putBoolean(KEY_SEND_DAILY, isSendDaily).apply();
    }

    public boolean isFilterMan() {
        return preferences.getBoolean(KEY_FILTER_MAN, false);
    }

    public void setFilterMan(boolean isFilterMan) {
        preferences.edit().putBoolean(KEY_FILTER_MAN, isFilterMan).apply();
    }

    public boolean isGalleryMode() {
        return preferences.getBoolean(KEY_GALLERY_MODE, false);
    }

    public void setGalleryMode(boolean isGalleryMode) {
        preferences.edit().putBoolean(KEY_GALLERY_MODE, isGalleryMode).apply();
        AnalyticsHelper.sendEvent(AnalyticsHelper.Events.CHOOSE_NB_COLUMNS, isGalleryMode ? "3" : "2");
    }

    public int getCountShared() {
        return preferences.getInt(KEY_COUNT_SHARED, 0);
    }

    public void increaseCountShared() {
        int countShared = preferences.getInt(KEY_COUNT_SHARED, 0) + 1;
        preferences.edit().putInt(KEY_COUNT_SHARED, countShared).apply();
    }

    public int increaseCounter(String counterName) {
        int count = preferences.getInt(KEY_COUNTER_PREFIX + counterName, 0) + 1;
        preferences.edit().putInt(KEY_COUNTER_PREFIX + counterName, count).apply();
        return count;
    }

    public void resetCounter(String counterName) {
        preferences.edit().putInt(KEY_COUNTER_PREFIX + counterName, 0).apply();
    }

    public int getCounter(String counterName) {
        return preferences.getInt(KEY_COUNTER_PREFIX + counterName, 0);
    }

    public boolean isFilterWoman() {
        return preferences.getBoolean(KEY_FILTER_WOMAN, false);
    }

    public void setFilterWoman(boolean isFilterWoman) {
        preferences.edit().putBoolean(KEY_FILTER_WOMAN, isFilterWoman).apply();
    }

    public boolean isFilterCountry() {
        return preferences.getBoolean(KEY_FILTER_COUNTRY, false);
    }

    public void setFilterCountry(boolean isFilterCountry) {
        preferences.edit().putBoolean(KEY_FILTER_COUNTRY, isFilterCountry).apply();
    }

    public String getLastIncomingMessageTime() {
        return preferences.getString(KEY_LAST_INCOMING_MESSAGE_TIME, null);
    }

    public void setLastIncomingMessageTime(String lastIncomingMessageTime) {
        preferences.edit().putString(KEY_LAST_INCOMING_MESSAGE_TIME, lastIncomingMessageTime).apply();
    }

    public int getCountAnsweredQuestions() {
        return preferences.getInt(KEY_COUNT_ANSWERED, 0);
    }

    public String getUserAnswer(String questionId) {
        return preferences.getString(KEY_QUESTION_PREFIX + questionId, null);
    }

    public String getLastSequenceId(String botName) {
        return preferences.getString(KEY_LAST_SEQUENCE_ID_PREFIX + botName, null);
    }

    public void setLastSequenceId(String botName, String lastSequenceId) {
        preferences.edit().putString(KEY_LAST_SEQUENCE_ID_PREFIX + botName, lastSequenceId).apply();
    }

    public boolean isUserOld() {
        String userAge = preferences.getString(KEY_QUESTION_PREFIX + AnalyticsHelper.Events.USER_AGE, null);
        if (userAge != null) {
            return userAge.equals(AnalyticsHelper.AGE_RANGE_STRINGS[2]) || userAge.equals(AnalyticsHelper.AGE_RANGE_STRINGS[3]);
        }
        return false;
    }

    public void setUserAnswer(String questionId, String answer) {
        DataManager.postUserProperty(new UserProperty(AppConfiguration.getBotName(), questionId, answer)).subscribe();
        preferences.edit().putInt(KEY_COUNT_ANSWERED, preferences.getInt(KEY_COUNT_ANSWERED, 0) + 1).apply();
        preferences.edit().putString(KEY_QUESTION_PREFIX + questionId, answer).commit();
    }

    public int getQuestionFilter(String questionId) {
        return preferences.getInt(KEY_FILTER_QUESTION + questionId, 0);
    }

    public void setQuestionFilter(String questionId, int answer) {
        preferences.edit().putInt(KEY_FILTER_QUESTION + questionId, answer).commit();
    }

    public boolean isAllThemesUnlocked() {
        return preferences.getBoolean(KEY_ALL_THEMES_UNLOCKED, false);
    }

    public void setAllThemesUnlocked(boolean isAllThemesUnlocked) {
        preferences.edit().putBoolean(KEY_ALL_THEMES_UNLOCKED, isAllThemesUnlocked).apply();
    }

    public String getUserDescription() {
        return preferences.getString(KEY_USER_DESCRIPTION, null);
    }

    public void setUserDescription(String userDescription) {
        preferences.edit().putString(KEY_USER_DESCRIPTION, userDescription).apply();
    }

    public String getFirstName() {
        return preferences.getString(KEY_FIRST_NAME, "");
    }

    public void setFirstName(String firstName) {
        preferences.edit().putString(KEY_FIRST_NAME, firstName).apply();
    }

    public String getUserAnimal() {
        return preferences.getString(KEY_USER_ANIMAL, null);
    }

    public void setUserAnimal(String userAnimal) {
        preferences.edit().putString(KEY_USER_ANIMAL, userAnimal).apply();
    }

    public String getUserLandscape() {
        return preferences.getString(KEY_USER_LANDSCAPE, null);
    }

    public void setUserLandscape(String landscape) {
        preferences.edit().putString(KEY_USER_LANDSCAPE, landscape).apply();
    }

    public String getUserFlower() {
        return preferences.getString(KEY_USER_FLOWER, null);
    }

    public void setUserFlower(String flower) {
        preferences.edit().putString(KEY_USER_FLOWER, flower).apply();
    }

    public boolean isShowPreview() {
        return preferences.getBoolean(KEY_SHOW_PREVIEW, true);
    }

    public void setShowPreview(boolean showPreview) {
        preferences.edit().putBoolean(KEY_SHOW_PREVIEW, showPreview).apply();
    }

    public boolean isPopularWasShown(String intentionId) {
        boolean result = preferences.getBoolean(KEY_FIRST_TIME_POPULAR + intentionId, false);
        if (!result) {
            preferences.edit().putBoolean(KEY_FIRST_TIME_POPULAR + intentionId, true).apply();
        }
        return result;
    }

    public boolean isIntentionBlocked(String intentionId) {
        return preferences.getBoolean(KEY_BLOCK_THEME_PREFIX + intentionId, false);
    }

    public void blockIntention(String intentionId) {
        preferences.edit().putBoolean(KEY_BLOCK_THEME_PREFIX + intentionId, true).apply();
    }

    public boolean isGameTutorialWasShown() {
        boolean result = preferences.getBoolean(KEY_GAME_TUTORIAL_WAS_SHOWN, false);
        if (!result) {
            preferences.edit().putBoolean(KEY_GAME_TUTORIAL_WAS_SHOWN, true).apply();
        }
        return result;
    }

    public boolean isIntentionUnlocked(String intentionId) {
        return preferences.getBoolean(intentionId, false);
    }

    public String getUserCountry() {
        return preferences.getString(KEY_USER_COUNTRY, "");
    }

    public void setUserCountry(String country) {
        preferences.edit().putString(KEY_USER_COUNTRY, country).apply();
    }

    public void updateLastHugTime() {
        preferences.edit().putLong(KEY_LAST_HUG_TIME, System.currentTimeMillis()).commit();
    }

    public long getLastHugTime() {
        return preferences.getLong(KEY_LAST_HUG_TIME, 0);
    }

    public void updateAskHuggyTime() {
        preferences.edit().putLong(KEY_LAST_HUGGY_SHOW_TIME, System.currentTimeMillis()).apply();
    }

    public long getAskHuggyLastShowTime() {
        return preferences.getLong(KEY_LAST_HUGGY_SHOW_TIME, 0);
    }

    public SharedPreferences getPreferences() {
        return preferences;
    }

    public boolean isShareFeatureEnabled() {
        return preferences.getBoolean(KEY_POPULAR_FEATURE_ENABLED, false);
    }

    public void setShareFeatureEnabled(boolean isEnabled) {
        preferences.edit().putBoolean(KEY_POPULAR_FEATURE_ENABLED, isEnabled).apply();
    }

    public boolean isAppFirstLaunch() {
        boolean result = preferences.getBoolean(KEY_APP_FIRST_LAUNCH, true);
        preferences.edit().putBoolean(KEY_APP_FIRST_LAUNCH, false).apply();
        if (result) {
            preferences.edit().putLong(KEY_APP_FIRST_LAUNCH_TIME, System.currentTimeMillis()).apply();
        }
        return result;
    }

    public boolean isFirstTimeAction(String additionalKey) {
        String key = KEY_FIRST_TIME_PREFIX + additionalKey;
        boolean result = preferences.getBoolean(key, true);
        preferences.edit().putBoolean(key, false).apply();
        return result;
    }

    public int getLastLanguageIndex() {
        return preferences.getInt(KEY_LAST_LANGUAGE, 0);
    }

    public void setLastLanguageIndex(int lastLanguageIndex) {
        preferences.edit().putInt(KEY_LAST_LANGUAGE, lastLanguageIndex).apply();
    }

    public int getShareMode() {
        return preferences.getInt(KEY_SHARING_MODE, 0);
    }

    public void setShareMode(int shareMode) {
        preferences.edit().putInt(KEY_SHARING_MODE, shareMode).apply();
    }

    public long getAppFirstLaunchTime() {
        return preferences.getLong(KEY_APP_FIRST_LAUNCH_TIME, 0);
    }

    public int getAppRating() {
        return preferences.getInt(KEY_APP_RATING, 0);
    }

    public void setAppRating(int appRating) {
        preferences.edit().putInt(KEY_APP_RATING, appRating).apply();
    }

    public String getRecipientType() {
        return preferences.getString(KEY_RECIPIENT_TYPE, null);
    }

    public void setRecipientType(String recipientType) {
        increaseChooseRecipientCount(recipientType);
        preferences.edit().putString(KEY_RECIPIENT_TYPE, recipientType).apply();
    }

    public String getRecipientGender() {
        return preferences.getString(KEY_RECIPIENT_GENDER, null);
    }

    public void setRecipientGender(String genderModifier) {
        preferences.edit().putString(KEY_RECIPIENT_GENDER, genderModifier).apply();
    }

    public int getMbtiGroup() {
        return preferences.getInt(KEY_MBTI_GROUP, -1);
    }

    public void setMbtiGroup(int mbtiGroup) {
        preferences.edit().putInt(KEY_MBTI_GROUP, mbtiGroup).apply();
    }

    public int getNotificationFrequency() {
        return preferences.getInt(KEY_NOTIFICATION_FREQUENCY, 0);
    }

    public void setNotificationFrequency(int notificationFrequency) {
        preferences.edit().putInt(KEY_NOTIFICATION_FREQUENCY, notificationFrequency).commit();
    }

    public String getRelationType() {
        return preferences.getString(KEY_RELATION_TYPE, null);
    }

    public void setRelationType(String relationType) {
        preferences.edit().putString(KEY_RELATION_TYPE, relationType).apply();
    }

    public boolean isProfileFilled() {
        return getUserFlower() != null && getUserDescription() != null && getUserAnimal() != null && getUserLandscape() != null;
    }

    public void setRecipient(Recipient recipient) {
        if (recipient == null) {
            setRelationType(null);
            setRecipientType(null);
            setRecipientPoliteForm(null);
            setRecipientGender(null);
        } else {
            setRelationType(recipient.getRelationTypeTag());
            setRecipientType(recipient.getId());
            setRecipientPoliteForm(recipient.getPoliteForm());
            setRecipientGender(recipient.getGender());
        }
    }

    public String getRecipientPoliteForm() {
        return preferences.getString(KEY_RECIPIENT_POLITE_FORM, "");
    }

    public void setRecipientPoliteForm(String politeForm) {
        preferences.edit().putString(KEY_RECIPIENT_POLITE_FORM, politeForm).apply();
    }

    public NotificationsModel getNotifications() {
        return new Gson().fromJson(preferences.getString(KEY_NOTIFICATIONS, null), NotificationsModel.class);
    }

    public void saveNotifications(NotificationsModel notificationsModel) {
        if (notificationsModel != null) {
            preferences.edit().putString(KEY_NOTIFICATIONS, new Gson().toJson(notificationsModel)).apply();
        }
    }

    public void saveBotSequence(String botName, BotSequence sequence) {
        preferences.edit().putString("s_" + botName, new Gson().toJson(sequence)).apply();
    }

    public BotSequence getBotSequence(String botName) {
        String sequenceString = preferences.getString("s_" + botName, null);
        if (sequenceString != null) {
            Logger.e(sequenceString);
            return new Gson().fromJson(sequenceString, BotSequence.class);
        }
        return null;
    }

    public boolean isBotUsed(String botName) {
        return preferences.getBoolean("b_" + botName, false);
    }

    public void setBotUsed(String botName) {
        preferences.edit().putBoolean("b_" + botName, true).apply();
    }

    public void setFacebookId(String facebookId) {
        preferences.edit().putString(KEY_FACEBOOK_ID, facebookId).apply();
    }

    public String getFacebookId() {
        return preferences.getString(KEY_FACEBOOK_ID, null);
    }

    public boolean isNewInstall() {
        return preferences.getBoolean(KEY_NEW_INSTALL, true);
    }

    public List<Recipient> getRecipients() {
        return new Gson().fromJson(preferences.getString(KEY_ACTIVE_RECIPIENTS, null), new TypeToken<List<Recipient>>() {
        }.getType());
    }

    public int getSelectedAge() {
        return preferences.getInt(KEY_AGE, -1);
    }

    public void setSelectedAge(int age) {
        preferences.edit().putInt(KEY_AGE, age).apply();
    }

    public int getSelectedRelationship() {
        return preferences.getInt(KEY_RELATIONSHIP_SETTINGS, -1);
    }

    public void setSelectedRelationship(int relationship) {
        if (relationship >= 0) {
            AnalyticsHelper.sendEvent("ppSingle", relationship == 0 ? "true" : "false");
        }
        preferences.edit().putInt(KEY_RELATIONSHIP_SETTINGS, relationship).apply();
    }

    public int getSelectedGender() {
        return preferences.getInt(KEY_GENDER, 0);
    }

    public void setSelectedGender(int gender) {
        preferences.edit().putInt(KEY_GENDER, gender).apply();
        String genderString = gender == UtilsMessages.Gender.MALE ? "male" : "female";
        AnalyticsHelper.sendEvent(AnalyticsHelper.Categories.APP, AnalyticsHelper.Events.USER_GENDER, genderString);
    }

    public boolean isNotificationsEnabled() {
        return preferences.getBoolean(KEY_RECEIVE_NOTIFICATIONS, true);
    }

    public void setNotificationsEnabled(boolean isEnabled) {
        preferences.edit().putBoolean(KEY_RECEIVE_NOTIFICATIONS, isEnabled).apply();
    }

    public boolean isUserLogin() {
        return preferences.getBoolean(KEY_LOG_IN, false);
    }

    public void setIsUserLogin(boolean isUserLogin) {
        preferences.edit().putBoolean(KEY_LOG_IN, isUserLogin).apply();
    }

    public void setExperimentId(String experimentId) {
        preferences.edit().putString(KEY_EXPERIMENT_ID, experimentId).apply();
    }

    public String getExperimentId() {
        return preferences.getString(KEY_EXPERIMENT_ID, null);
    }

    public void setVariationId(int variationId) {
        preferences.edit().putInt(KEY_VARIATION_ID, variationId).apply();
    }

    public int getVariationId() {
        return preferences.getInt(KEY_VARIATION_ID, -1);
    }

    public void setRecipientNotifiactionEnabled(String recipientId, boolean isEnabled) {
        preferences.edit().putBoolean(KEY_RECIPIENT_PREFIX_NOTIFICATION + recipientId, isEnabled).apply();
    }

    public boolean isRecipientNotificationEnabled(String recipientId) {
        return preferences.getBoolean(KEY_RECIPIENT_PREFIX_NOTIFICATION + recipientId, false);
    }

    public void increaseChooseRecipientCount(String recipientId) {
        int oldValue = preferences.getInt(KEY_RECIPIENT_PREFIX + recipientId, 0);
        preferences.edit().putInt(KEY_RECIPIENT_PREFIX + recipientId, oldValue + 1).apply();
    }

    public Integer getRecipientChooseCount(String recipientId) {
        return preferences.getInt(KEY_RECIPIENT_PREFIX + recipientId, 0);
    }

    public void increaseChooseIntentionCount(String intentionId) {
        int oldValue = preferences.getInt(KEY_INTENTION_PREFIX + intentionId, 0);
        preferences.edit().putInt(KEY_INTENTION_PREFIX + intentionId, oldValue + 1).apply();
    }

    public Integer getIntentionChooseCount(String intentionId) {
        return preferences.getInt(KEY_INTENTION_PREFIX + intentionId, 0);
    }

    public boolean isDailyMother() {
        return preferences.getBoolean(KEY_DAILY_MOTHER, false);
    }

    public void setDailyMother(boolean isEnabled) {
        preferences.edit().putBoolean(KEY_DAILY_MOTHER, isEnabled).apply();
    }

    public boolean isDailySweetheart() {
        return preferences.getBoolean(KEY_DAILY_SWEETHEART, false);
    }

    public void setDailySweetheart(boolean isEnabled) {
        preferences.edit().putBoolean(KEY_DAILY_SWEETHEART, isEnabled).apply();
    }

    public boolean isDailyFriend() {
        return preferences.getBoolean(KEY_DAILY_FRIEND, false);
    }

    public void setDailyFriend(boolean isEnabled) {
        preferences.edit().putBoolean(KEY_DAILY_FRIEND, isEnabled).apply();
    }

    public boolean isDailyFacebookFriend() {
        return preferences.getBoolean(KEY_DAILY_FACEBOOK_FRIEND, false);
    }

    public void setKeyDailyFacebookFriend(boolean isEnabled) {
        preferences.edit().putBoolean(KEY_DAILY_FACEBOOK_FRIEND, isEnabled).apply();
    }

    public boolean isHumourMode() {
        return preferences.getBoolean(KEY_HUMOR_MODE, false);
    }

    public void setIsHumourMode(boolean isEnabled) {
        preferences.edit().putBoolean(KEY_HUMOR_MODE, isEnabled).commit();
    }

}
