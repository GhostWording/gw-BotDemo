package com.ghostwording.chatbot.io.service;

import com.ghostwording.chatbot.model.GifResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GiffyService {

    String BASE_URL = "http://api.giphy.com/v1/";

    @GET("gifs/trending?api_key=dc6zaTOxFJmzC")
    Call<GifResponse> getTrendingGifs();

    @GET("gifs/trending?api_key=dc6zaTOxFJmzC")
    Call<GifResponse> getTrendingGifs(@Query("offset") int offset);

    @GET("gifs/search?api_key=dc6zaTOxFJmzC")
    Call<GifResponse> searchGifs(@Query("q") String searchKeyword);

    @GET("gifs?api_key=dc6zaTOxFJmzC")
    Call<GifResponse> getGifByIds(@Query("ids") String ids);

}
