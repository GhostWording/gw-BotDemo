package com.ghostwording.hugsapp.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class AppRater {

    private static final String PREF_NAME = "AppRater";

    private static final String KEY_SHOW_APP_RATE = "show_app_rate";
    private static final String KEY_SHOW_LATER_COUNTER = "show_later_counter";

    private static final long SHOW_APP_RATE_INTERVAL = 1000 * 60 * 60 * 24 * 2;

    public static boolean isShowAppRateDialog(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        if (preferences.getBoolean(KEY_SHOW_APP_RATE, true) && Utils.getCurrentLocaleLanguageIndex() >= 0) {
            int showLaterCounter = preferences.getInt(KEY_SHOW_LATER_COUNTER, 1);
            if ((System.currentTimeMillis() - PrefManager.instance().getAppFirstLaunchTime()) > SHOW_APP_RATE_INTERVAL * showLaterCounter) {
                return true;
            }
        }
        return false;
    }

    public static void disableAppRateDialog(Context context) {
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).edit().putBoolean(KEY_SHOW_APP_RATE, false).apply();
    }
}
