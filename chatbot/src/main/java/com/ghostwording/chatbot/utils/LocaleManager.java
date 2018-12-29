package com.ghostwording.chatbot.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;

import java.util.Locale;

public class LocaleManager {

    private static final String PREF_NAME = "LanguagePreferences";
    private static final String KEY_LANGUAGE = "language";

    public static Context setLocale(Context c) {
        return updateResources(c, getLanguage(c));
    }

    public static Context setNewLocale(Context c, String language, int languageIndex) {
        setSelectedLanguage(c, languageIndex);
        return updateResources(c, language);
    }

    public static String getLanguage(Context c) {
        return Utils.getLanguageString(getSelectedLanguage(c));
    }

    public static int getSelectedLanguage(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        int defaultLanguage = 0;
        int phoneLocaleLanguageIndex = Utils.getCurrentLocaleLanguageIndex();
        if (phoneLocaleLanguageIndex >= 0) {
            defaultLanguage = phoneLocaleLanguageIndex;
        }
        return preferences.getInt(KEY_LANGUAGE, defaultLanguage);
    }

    public static void setSelectedLanguage(Context context, int language) {
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).edit().putInt(KEY_LANGUAGE, language).apply();
    }

    private static Context updateResources(Context context, String language) {
        Locale locale = new Locale(language);
        Locale.setDefault(locale);

        Resources res = context.getResources();
        Configuration config = new Configuration(res.getConfiguration());
        if (Build.VERSION.SDK_INT >= 17) {
            config.setLocale(locale);
            context = context.createConfigurationContext(config);
        } else {
            config.locale = locale;
            res.updateConfiguration(config, res.getDisplayMetrics());
        }
        return context;
    }

    public static Locale getLocale(Resources res) {
        Configuration config = res.getConfiguration();
        return Build.VERSION.SDK_INT >= 24 ? config.getLocales().get(0) : config.locale;
    }

    public static Locale getTtsLocale(Context context) {
        String language = getLanguage(context);
        if (language.contains("en")) {
            return Locale.US;
        } else if (language.contains("fr")) {
            return Locale.CANADA_FRENCH;
        } else if (language.contains("es")) {
            return new Locale("es", "ES");
        } else {
            return new Locale(language, language.toUpperCase());
        }
    }
}
