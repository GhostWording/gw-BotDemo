package com.ghostwording.chatbot.utils;

import android.content.Context;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;

import com.ghostwording.chatbot.analytics.AnalyticsHelper;
import com.ghostwording.chatbot.chatbot.model.BotSequence;
import com.ghostwording.chatbot.model.AdvertPlacements;
import com.ghostwording.chatbot.model.ImagePreview;
import com.ghostwording.chatbot.model.MoodMenuItem;
import com.ghostwording.chatbot.model.Promotions;
import com.ghostwording.chatbot.model.SurveyBotStrings;
import com.ghostwording.chatbot.model.intentions.Intention;
import com.ghostwording.chatbot.model.recipients.Recipient;
import com.ghostwording.chatbot.model.texts.Quote;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AppConfiguration {

    public static final String ERROR_NO_SEQUENCES = "NoMoreSequencesAvailable";

    private static File httpCacheDirectory;
    private static String deviceId;
    private static String appVersionNumber;

    private static String appAreaId = "humourapp";
    private static String appAreaAnalytics = "SaveTheKitten";
    private static String pictureArea = "sweetheart";
    private static String appName = "ineedhugs";
    private static String sBotName = "savethekitten";

    private static boolean sRestartMainActivity;
    private static boolean displayAdMob;
    private static boolean sTestMode = false;
    private static long lastAdTime;
    private static List<Promotions.App> sAppPromotions = new ArrayList<>();
    private static AdvertPlacements sAdvertPlacements;

    private static List<Intention> sIntentions = new ArrayList<>();
    private static List<Quote> sBotQuestions = new ArrayList<>();
    private static List<Recipient> sRecipients = new ArrayList<>();
    private static SurveyBotStrings sSurveyBotStrings;
    private static BotSequence sTestSequence;
    private static HashMap<String, Boolean> sDisabledBotRequests = new HashMap<>();
    private static Promotions sPromotions;
    private static boolean sIsBotWaitingForInput = false;
    private static boolean sIsAnimateButtons = true;

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
        sTestSequence = new Gson().fromJson(AssetsManager.getStringFromAssetFile(context, "test_question.json"), BotSequence.class);
    }

    public static boolean isAnimateButtons() {
        if (sIsAnimateButtons) {
            sIsAnimateButtons = false;
            return true;
        }
        return false;
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

    public static boolean isWaitingForInput() {
        return sIsBotWaitingForInput;
    }

    public static void setWaitForInput(boolean isWaitForInput) {
        sIsBotWaitingForInput = isWaitForInput;
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

    public static List<Recipient> getRecipients() {
        return sRecipients;
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
        return appName;
    }

    public static List<Promotions.App> getAppPromotions() {
        return sAppPromotions;
    }

    public static void setAppPromotions(List<Promotions.App> promotions) {
        sAppPromotions = promotions;
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


