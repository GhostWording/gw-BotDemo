package com.ghostwording.hugsapp.io.service;

import com.ghostwording.hugsapp.model.DailySuggestion;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface IdeasService {

    String BASE_URL = "http://gw-ideas.azurewebsites.net/bot/";

    @GET("IdeasOfTheDay/forRecipient/all/andIntention/{path}?isquote=false&nbcards=50&mode=pickrandom&nbrand=20")
    Call<List<DailySuggestion>> getIdeasForIntention(@Path("path") String text);

}
