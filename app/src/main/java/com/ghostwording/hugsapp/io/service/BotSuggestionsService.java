package com.ghostwording.hugsapp.io.service;

import com.ghostwording.hugsapp.model.PopularImages;
import com.ghostwording.hugsapp.model.texts.PopularTexts;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface BotSuggestionsService {

    String BASE_URL = "http://gw-suggest.azurewebsites.net/Bot/";

    @GET("popularimages/{path}?nbcards=20")
    Call<List<PopularImages>> getSuggestedImages(@Path("path") String text);

    @GET("populartexts/intention/{path}?nbcards=20")
    Call<List<PopularTexts>> getSuggestedTexts(@Path("path") String text);


}
