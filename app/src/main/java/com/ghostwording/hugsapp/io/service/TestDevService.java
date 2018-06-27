package com.ghostwording.hugsapp.io.service;

import com.ghostwording.hugsapp.chatbot.model.BotSequence;
import com.ghostwording.hugsapp.chatbot.model.SearchRequest;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface TestDevService {

    String BASE_URL = "http://gw-bot-apis.azurewebsites.net/";

    @GET("admin/command/history/clear?adminkey=61fd4333-61b1-4236-83c6-33bda5a9d6e6&facebookid=")
    Call<ResponseBody> clearBotHistory(@Query("botName") String botName, @Query("deviceId") String deviceId);

    @POST("api/sequences/keywordsearch")
    Call<BotSequence> searchBotSequence(@Body SearchRequest searchRequest);

}
