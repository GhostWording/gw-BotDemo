package com.ghostwording.hugsapp.io.service;

import com.ghostwording.hugsapp.model.DailySuggestion;
import com.ghostwording.hugsapp.utils.AppConfiguration;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface SuggestionsService {

    String BASE_URL = "http://api.cvd.io/popular/" + AppConfiguration.getAppAreaId() + "/";

    @GET("IdeasOfTheDay/ByIntention")
    Call<List<DailySuggestion>> getDailySuggestions(@Query("deviceid") String deviceId);

    @GET("IdeasOfTheDay/ByIntention?nbcards=30")
    Call<List<DailySuggestion>> getDailySuggestionsWithVersion(@Query("deviceid") String deviceId, @Query("version") int version);

    @GET("IdeasOfTheDay/forRecipient/{recipientId}")
    Call<List<DailySuggestion>> getDailySuggestions(@Path("recipientId") String recipient, @Query("deviceid") String deviceId);

    @GET("IdeasOfTheDay/forRecipient/{recipientId}/andIntention/{intentionId}")
    Call<List<DailySuggestion>> getDailySuggestions(@Path("recipientId") String recipient, @Path("intentionId") String intentionId, @Query("senderGender") String senderGender);

    @GET("IdeasOfTheDay/forRecipient/BotFriends/andIntention/{intentionId}?nbcards=10")
    Call<List<DailySuggestion>> getBotSuggestions(@Path("intentionId") String intentionId);

}