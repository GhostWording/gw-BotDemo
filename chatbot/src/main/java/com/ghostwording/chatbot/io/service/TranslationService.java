package com.ghostwording.chatbot.io.service;

import com.ghostwording.chatbot.model.texts.TranslationLanguage;
import com.ghostwording.chatbot.model.texts.TranslationResult;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface TranslationService {

    String BASE_URL = "http://gw-autotranslate.azurewebsites.net/";

    @GET("translate")
    Call<TranslationResult> translate(@Query("TextId") String textId, @Query("text") String text, @Query("from") String from, @Query("to") String to);

    @GET("getlanguages")
    Call<List<TranslationLanguage>> getSupportedLanguages(@Query("culture") String culture);

}
