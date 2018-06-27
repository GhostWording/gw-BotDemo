package com.ghostwording.hugsapp;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;

import com.crashlytics.android.Crashlytics;
import com.ghostwording.hugsapp.analytics.AnalyticsHelper;
import com.ghostwording.hugsapp.analytics.GhostAnalytics;
import com.ghostwording.hugsapp.chatbot.BotQuestionsManager;
import com.ghostwording.hugsapp.io.ApiClient;
import com.ghostwording.hugsapp.io.QuotesLoader;
import com.ghostwording.hugsapp.model.Promotions;
import com.ghostwording.hugsapp.model.texts.Quote;
import com.ghostwording.hugsapp.utils.AppConfiguration;
import com.ghostwording.hugsapp.utils.LocaleManager;
import com.ghostwording.hugsapp.utils.Logger;
import com.ghostwording.hugsapp.utils.PrefManager;
import com.ghostwording.hugsapp.utils.Utils;
import com.ghostwording.hugsapp.utils.UtilsLocalNotifications;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import io.fabric.sdk.android.Fabric;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BotDemoApplication extends Application {

    private static BotDemoApplication instance;

    public static BotDemoApplication instance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        Fabric.with(this, new Crashlytics());

        AppConfiguration.create(getApplicationContext());
        PrefManager.createInstance(getApplicationContext());
        QuotesLoader.init(getApplicationContext());
        UtilsLocalNotifications.loadNotifications();
        GhostAnalytics.create(getApplicationContext());

        Utils.setLanguage(this, LocaleManager.getSelectedLanguage(this));

        if (PrefManager.instance().isNewInstall() && PrefManager.instance().getVariationId() == -1) {
            AnalyticsHelper.sendCurrentLocation();
            int variationId = new Random().nextInt(6);
            Logger.i("Current app variation : " + variationId);
            PrefManager.instance().setVariationId(variationId);
            AnalyticsHelper.sendEvent(AnalyticsHelper.Events.LANGUAGE, Resources.getSystem().getConfiguration().locale.getLanguage());
        }

        AnalyticsHelper.sendOneTimeEvent(AnalyticsHelper.Events.FIRST_LAUNCH);
        AnalyticsHelper.sendEvent(AnalyticsHelper.Events.APP_LAUNCH);
        updateBotQuestions();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleManager.setLocale(base));
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        LocaleManager.setLocale(this);
    }

    public void updateBotQuestions() {
        final String BOT_QUOTES_ID = "6A52055181";
        final String POLITE_VERBAL_FORM = "V";
        ApiClient.getInstance().coreApiService.getQuotes(AppConfiguration.getAppAreaId(), BOT_QUOTES_ID).enqueue(new Callback<List<Quote>>() {
            @Override
            public void onResponse(Call<List<Quote>> call, Response<List<Quote>> response) {
                if (response.isSuccessful()) {
                    List<Quote> filteredQuestions = new ArrayList<>();
                    for (Quote quote : response.body()) {
                        if (quote.getPoliteForm().equals(POLITE_VERBAL_FORM)) {
                            continue;
                        }
                        filteredQuestions.add(quote);
                    }
                    BotQuestionsManager.instance().setBotQuestions(filteredQuestions);
                }
            }

            @Override
            public void onFailure(Call<List<Quote>> call, Throwable t) {
            }
        });
    }


}
