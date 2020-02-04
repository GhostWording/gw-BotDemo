package com.ghostwording.chatbot.io.service;

import com.ghostwording.chatbot.chatbot.model.BotSequence;
import com.ghostwording.chatbot.chatbot.model.SearchRequest;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface TestDevService {

    String BASE_URL = "http://gw-bot-apis.azurewebsites.net/";

    @POST("api/sequences/keywordsearch")
    Call<BotSequence> searchBotSequence(@Body SearchRequest searchRequest);

}
