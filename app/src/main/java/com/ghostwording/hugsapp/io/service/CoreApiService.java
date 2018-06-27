package com.ghostwording.hugsapp.io.service;


import com.ghostwording.hugsapp.model.UserProfile;
import com.ghostwording.hugsapp.model.intentions.Intention;
import com.ghostwording.hugsapp.model.texts.Quote;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface CoreApiService {

    String BASE_URL = "http://api.cvd.io/";

    @GET("{areaId}/intentions")
    Call<List<Intention>> getIntentions(@Path("areaId") String areaId);

    @GET("{areaId}/intention/{intention}/texts")
    Call<List<Quote>> getQuotes(@Path("areaId") String areaId, @Path("intention") String intentionId);

    @GET("{areaId}/users/withfacebookid/{userId}")
    Call<UserProfile> getUserById(@Path("areaId") String areaId, @Path("userId") String userId);

    @GET("{areaId}/users/withdeviceid/{userId}")
    Call<UserProfile> getUserByDeviceId(@Path("areaId") String areaId, @Path("userId") String userId);

    @GET("{areaId}/text/realizations/{textId}")
    Call<List<Quote>> getTextByPrototypeId(@Path("areaId") String areaId, @Path("textId") String textId);

    @GET("{areaId}/text/prototypes/{prototypes}/realizations")
    Call<List<Quote>> getQuotesFromPrototypes(@Path("areaId") String areaId, @Path("prototypes") String prototypes);

    @GET("{areaId}/text/{textId}")
    Call<Quote> getTextById(@Path("areaId") String areaId, @Path("textId") String textId);

    @GET("{areaId}/text/realizations/{prototypes}")
    Call<List<Quote>> getQuotesFromRealizations(@Path("areaId") String areaId, @Path("prototypes") String prototypes);

}
