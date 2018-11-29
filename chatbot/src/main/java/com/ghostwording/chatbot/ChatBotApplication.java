package com.ghostwording.chatbot;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;

import com.ghostwording.chatbot.analytics.AnalyticsHelper;
import com.ghostwording.chatbot.analytics.GhostAnalytics;
import com.ghostwording.chatbot.chatbot.BotQuestionsManager;
import com.ghostwording.chatbot.chatbot.RedirectionManager;
import com.ghostwording.chatbot.io.ApiClient;
import com.ghostwording.chatbot.io.DataLoader;
import com.ghostwording.chatbot.io.QuotesLoader;
import com.ghostwording.chatbot.model.texts.Quote;
import com.ghostwording.chatbot.utils.AppConfiguration;
import com.ghostwording.chatbot.utils.LocaleManager;
import com.ghostwording.chatbot.utils.Logger;
import com.ghostwording.chatbot.utils.PrefManager;
import com.ghostwording.chatbot.utils.Utils;
import com.ghostwording.chatbot.utils.UtilsLocalNotifications;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import io.reactivex.subjects.PublishSubject;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatBotApplication extends Application {

    private static ChatBotApplication instance;
    private static PublishSubject<String> userInput;
    protected RedirectionManager redirectionManager;

    public static ChatBotApplication instance() {
        return instance;
    }

    public static PublishSubject<String> getUserInputSubject() {
        return userInput;
    }

    public RedirectionManager getRedirectionManager() {
        return redirectionManager;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        userInput = PublishSubject.create();
        createRedirectionManager();
        AppConfiguration.create(getApplicationContext());
        PrefManager.createInstance(getApplicationContext());
        DataLoader.init(getApplicationContext());
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

    protected void createRedirectionManager() {
        redirectionManager = (activity, step) -> {
        };
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
