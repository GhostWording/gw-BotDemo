package com.ghostwording.hugsapp.io;

import com.ghostwording.hugsapp.io.service.BotService;
import com.ghostwording.hugsapp.io.service.BotSuggestionsService;
import com.ghostwording.hugsapp.io.service.ConfigService;
import com.ghostwording.hugsapp.io.service.CoreApiService;
import com.ghostwording.hugsapp.io.service.CountryService;
import com.ghostwording.hugsapp.io.service.GiffyService;
import com.ghostwording.hugsapp.io.service.IdeasService;
import com.ghostwording.hugsapp.io.service.PictureService;
import com.ghostwording.hugsapp.io.service.PopularService;
import com.ghostwording.hugsapp.io.service.SuggestionsService;
import com.ghostwording.hugsapp.io.service.TestDevService;
import com.ghostwording.hugsapp.io.service.TranslationService;
import com.ghostwording.hugsapp.io.service.VotingService;
import com.ghostwording.hugsapp.utils.AppConfiguration;
import com.ghostwording.hugsapp.utils.Logger;

import junit.framework.Test;

import java.io.IOException;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

    private static int CONNECT_TIMEOUT_MILLIS = 15000;
    private static int READ_TIMEOUT_MILLIS = 15000;
    private static int RETRY_COUNT = 3;

    private static ApiClient instance;
    private final OkHttpClient client;

    public final PictureService pictureService;
    public final CoreApiService coreApiService;
    public final GiffyService giffyService;
    public final ConfigService configService;
    public final PopularService popularService;
    public final SuggestionsService suggestionsService;
    public final BotService botService;
    public final VotingService votingService;
    public final IdeasService ideasService;
    public final BotSuggestionsService botSuggestionsService;
    public final TestDevService testDevService;
    public final TranslationService translationService;

    public static ApiClient getInstance() {
        if (instance == null) {
            instance = new ApiClient();
        }
        return instance;
    }

    public static void clearData() {
        if (instance != null) {
            try {
                instance.client.cache().evictAll();
            } catch (IOException e) {
                Logger.e(e.toString());
            }
        }
        instance = null;
    }

    private ApiClient() {
        client = createHttpClient(true);
        OkHttpClient clientWithoutCache = createHttpClient(false);

        pictureService = createRestAdapterForEndPoint(client, PictureService.BASE_URL).create(PictureService.class);
        coreApiService = createRestAdapterForEndPoint(client, CoreApiService.BASE_URL).create(CoreApiService.class);
        popularService = createRestAdapterForEndPoint(client, PopularService.BASE_URL).create(PopularService.class);
        giffyService = createRestAdapterForEndPoint(clientWithoutCache, GiffyService.BASE_URL).create(GiffyService.class);
        configService = createRestAdapterForEndPoint(clientWithoutCache, ConfigService.BASE_URL).create(ConfigService.class);
        suggestionsService = createRestAdapterForEndPoint(clientWithoutCache, SuggestionsService.BASE_URL).create(SuggestionsService.class);
        botService = createRestAdapterForEndPoint(clientWithoutCache, BotService.BASE_URL).create(BotService.class);
        votingService = createRestAdapterForEndPoint(clientWithoutCache, VotingService.BASE_URL).create(VotingService.class);
        ideasService = createRestAdapterForEndPoint(clientWithoutCache, IdeasService.BASE_URL).create(IdeasService.class);
        botSuggestionsService = createRestAdapterForEndPoint(clientWithoutCache, BotSuggestionsService.BASE_URL).create(BotSuggestionsService.class);
        testDevService = createRestAdapterForEndPoint(clientWithoutCache, TestDevService.BASE_URL).create(TestDevService.class);
        translationService = createRestAdapterForEndPoint(clientWithoutCache, TranslationService.BASE_URL).create(TranslationService.class);
    }

    private Retrofit createRestAdapterForEndPoint(OkHttpClient client, String endPoint) {
        return new Retrofit.Builder()
                .baseUrl(endPoint)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    private OkHttpClient createHttpClient(boolean cacheEnabled) {
        Locale locale = Locale.getDefault();
        final String acceptLanguage = locale.getLanguage() + "-" + locale.getLanguage().toUpperCase();

        //HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        //logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        return new OkHttpClient.Builder()
                .connectTimeout(CONNECT_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS)
                .readTimeout(READ_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS)
                .cache(cacheEnabled ? new Cache(AppConfiguration.getHttpCacheDirectory(), 10 * 1024 * 1024) : null)
                .addInterceptor(chain -> {
                    Request original = chain.request();
                    Request.Builder requestBuilder = original.newBuilder()
                            .addHeader("Content-Type", "application/json")
                            .addHeader("Accept", "application/json")
                            .addHeader("Accept-Language", acceptLanguage);
                    return chain.proceed(requestBuilder.build());
                })
                .addInterceptor(chain -> {
                    Request request = chain.request();
                    Response response = chain.proceed(request);
                    int tryCount = 0;
                    while (!response.isSuccessful() && tryCount < RETRY_COUNT) {
                        tryCount++;
                        response = chain.proceed(request);
                    }
                    return response;
                })
                //.addInterceptor(logging)
                .addNetworkInterceptor(chain -> {
                    Response originalResponse = chain.proceed(chain.request());
                    return originalResponse.newBuilder()
                            .header("Cache-Control", "max-age=86400, only-if-cached, max-stale=0")
                            .build();
                })
                .build();
    }

    public static CountryService getCountryService() {
        return new Retrofit.Builder().baseUrl(CountryService.BASE_URL).addConverterFactory(GsonConverterFactory.create()).build().create(CountryService.class);
    }

}

