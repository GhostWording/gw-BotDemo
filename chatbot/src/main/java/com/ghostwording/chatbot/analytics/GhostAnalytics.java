package com.ghostwording.chatbot.analytics;

import android.content.Context;

import com.ghostwording.chatbot.utils.Logger;
import com.ghostwording.chatbot.utils.PrefManager;
import com.ghostwording.chatbot.utils.Utils;
import com.google.gson.Gson;

import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GhostAnalytics {

    private static final int UPLOAD_EVENTS_PERIOD_SECONDS = 20;

    private static GhostAnalytics instance;

    private EventsDatabase eventsDatabase;
    private AnalyticsApiService analyticsApiService;

    public static void create(Context context) {
        instance = new GhostAnalytics(context);
    }

    public static GhostAnalytics instance() {
        return instance;
    }

    public void logEvent(EventModel eventModel) {
        eventModel.setClientTime(new Date().getTime());
        eventModel.setRecipientType(PrefManager.instance().getRecipientType());
        eventsDatabase.addEvent(eventModel);
    }

    private GhostAnalytics(Context context) {
        eventsDatabase = new EventsDatabase(context);

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BASIC);

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(chain -> {
                    Request original = chain.request();
                    Request.Builder requestBuilder = original.newBuilder()
                            .addHeader("Accept", "application/json")
                            .addHeader("Content-Type", "application/json")
                            .addHeader("date", Utils.UTC_DATE_FORMAT.format(new Date()));
                    return chain.proceed(requestBuilder.build());
                })
                .addInterceptor(logging)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(AnalyticsApiService.BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        analyticsApiService = retrofit.create(AnalyticsApiService.class);

        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(this::uploadEventsToServer, 0, UPLOAD_EVENTS_PERIOD_SECONDS, TimeUnit.SECONDS);
    }

    private void uploadEventsToServer() {
        List<EventModel> eventModelList = eventsDatabase.getEvents();
        Logger.e(new Gson().toJson(eventModelList));
        if (eventModelList.size() > 0) {
            try {
                retrofit2.Response<ResponseBody> response = analyticsApiService.sendEvents(eventModelList).execute();
                if (response.isSuccessful()) {
                    eventsDatabase.removeEvents(eventModelList);
                }
            } catch (Exception ex) {
                Logger.e(ex.toString());
            }
        }
    }

}
