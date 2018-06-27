package com.ghostwording.hugsapp.utils;

import android.content.Context;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;

import com.ghostwording.hugsapp.analytics.AnalyticsHelper;
import com.ghostwording.hugsapp.chatbot.model.BotSequence;
import com.ghostwording.hugsapp.model.AdvertPlacements;
import com.ghostwording.hugsapp.model.ImagePreview;
import com.ghostwording.hugsapp.model.MoodMenuItem;
import com.ghostwording.hugsapp.model.Promotions;
import com.ghostwording.hugsapp.model.SurveyBotStrings;
import com.ghostwording.hugsapp.model.intentions.Intention;
import com.ghostwording.hugsapp.model.recipients.Recipient;
import com.ghostwording.hugsapp.model.texts.Quote;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AppConfiguration {

    public static final String ERROR_NO_SEQUENCES = "NoMoreSequencesAvailable";
    public static final long HUG_APPEARANCE_INTERVAL = 1000 * 60 * 60 * 24;

    private static File httpCacheDirectory;
    private static String appAreaId;
    private static String deviceId;
    private static String appVersionNumber;
    private static String appAreaAnalytics;

    private static boolean sRestartMainActivity;
    private static boolean displayAdMob;
    private static boolean sUserShared = false;
    private static boolean sTestMode = false;
    private static long lastAdTime;
    private static List<Promotions.App> sAppPromotions = new ArrayList<>();
    private static AdvertPlacements sAdvertPlacements;
    private static String sBotName = "savethekitten";
    private static String pictureArea = "sweetheart";

    private static List<String> selectedImages = new ArrayList<>();
    private static List<ImagePreview> sImagePreviews = new ArrayList<>();
    private static List<MoodMenuItem> sCategories = new ArrayList<>();
    private static List<Intention> sIntentions = new ArrayList<>();
    private static List<Quote> sBotQuestions = new ArrayList<>();
    private static List<Recipient> sRecipients = new ArrayList<>();
    private static SurveyBotStrings sSurveyBotStrings;
    private static BotSequence sBotMenu;
    private static BotSequence sDefaultCommands;
    private static BotSequence sTestSequence;
    private static HashMap<String, Boolean> sDisabledBotRequests = new HashMap<>();
    private static Uri loadedHugGifUri = null;
    private static Promotions sPromotions;

    public static void create(Context context) {
        deviceId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        try {
            appVersionNumber = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        httpCacheDirectory = new File(context.getCacheDir(), "responses");

        sIntentions = new Gson().fromJson(AssetsManager.getStringFromAssetFile(context, "intentions.json"), new TypeToken<List<Intention>>() {
        }.getType());
        sRecipients = new Gson().fromJson(AssetsManager.getStringFromAssetFile(context, "recipients.json"), new TypeToken<List<Recipient>>() {
        }.getType());
        sSurveyBotStrings = new Gson().fromJson(AssetsManager.getStringFromAssetFile(context, "surveybotresults.json"), SurveyBotStrings.class);
        sBotMenu = new Gson().fromJson(AssetsManager.getStringFromAssetFile(context, "chatbot_menu.json"), BotSequence.class);
        sDefaultCommands = new Gson().fromJson(AssetsManager.getStringFromAssetFile(context, "default_commands.json"), BotSequence.class);
        sTestSequence = new Gson().fromJson(AssetsManager.getStringFromAssetFile(context, "test_question.json"), BotSequence.class);

        appAreaId = "humourapp";
        appAreaAnalytics = "SaveTheKitten";
    }

    public static void setPromotions(Promotions promotions) {
        sPromotions = promotions;
    }

    public static String getPictureArea() {
        return pictureArea;
    }

    public static BotSequence getTestSequence() {
        return sTestSequence;
    }

    public static Uri getLoadedHugGifUri() {
        return loadedHugGifUri;
    }

    public static void setLoadedHugGifUri(Uri uri) {
        loadedHugGifUri = uri;
    }

    public static boolean isDisabledBotRequests(String botName) {
        return sDisabledBotRequests.containsKey(botName);
    }

    public static void disableBotRequests(String botName) {
        sDisabledBotRequests.put(botName, true);
    }

    public static List<Recipient> getRecipientsList() {
        return sRecipients;
    }

    public static boolean isUserShared() {
        if (sUserShared) {
            sUserShared = false;
            return true;
        }
        return false;
    }

    public static List<Promotions.App> getAppPromos() {
        if (sPromotions != null) {
            return sPromotions.getApps();
        }
        return new ArrayList<>();
    }

    public static String getBotName() {
        return sBotName;
    }

    public static void setBotName(String botName) {
        sBotName = botName;
    }

    public static void setUserShared() {
        sUserShared = true;
    }

    public static BotSequence getDefaultCommands() {
        return sDefaultCommands;
    }

    public static List<Recipient> getRecipients() {
        return sRecipients;
    }

    public static BotSequence getChatbotMenu() {
        return sBotMenu;
    }

    public static SurveyBotStrings getSurveyBotStrings() {
        return sSurveyBotStrings;
    }

    public static List<Quote> getBotQuestions() {
        return sBotQuestions;
    }

    public static List<Intention> getIntentions() {
        return sIntentions;
    }

    public static AdvertPlacements getAdPlacements() {
        return sAdvertPlacements;
    }

    public static void setAdPlacements(AdvertPlacements adPlacements) {
        sAdvertPlacements = adPlacements;
    }

    public static List<MoodMenuItem> getCategories() {
        return sCategories;
    }

    public static boolean isDisplayAdMob() {
        if ((System.currentTimeMillis() - lastAdTime) > 1000 * 60 * 4) {
            displayAdMob = true;
        }
        return displayAdMob;
    }

    public static void setAdEnabled(boolean isDisplayAd) {
        lastAdTime = System.currentTimeMillis();
        displayAdMob = isDisplayAd;
    }

    public static String getApplicationName() {
        return "ineedhugs";
    }

    public static List<Promotions.App> getAppPromotions() {
        return sAppPromotions;
    }

    public static void setAppPromotions(List<Promotions.App> promotions) {
        sAppPromotions = promotions;
    }

    public static List<ImagePreview> getImagePreviews() {
        return sImagePreviews;
    }

    public static void setSelectedImages(List<String> items) {
        selectedImages = new ArrayList<>(items);
    }

    public static List<String> getSelectedImages() {
        return selectedImages;
    }

    public static File getHttpCacheDirectory() {
        return httpCacheDirectory;
    }

    public static String getAppAreaAnalytics() {
        return appAreaAnalytics;
    }

    public static String getDeviceId() {
        return deviceId;
    }

    public static String getAppVersionNumber() {
        return appVersionNumber;
    }

    public static String getAppAreaId() {
        return appAreaId;
    }

    public static void enableTestMode() {
        AnalyticsHelper.sendEvent(AnalyticsHelper.Events.DEVELOPER_MODE);
        sRestartMainActivity = true;
        sTestMode = true;
    }

    public static boolean isTestMode() {
        return sTestMode;
    }

    public static boolean isRestartMainActivity() {
        boolean result = sRestartMainActivity;
        sRestartMainActivity = false;
        return result;
    }

    public static void restartMainActivity() {
        sRestartMainActivity = true;
    }

}


