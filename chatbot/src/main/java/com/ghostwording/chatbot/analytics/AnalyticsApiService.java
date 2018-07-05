package com.ghostwording.chatbot.analytics;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AnalyticsApiService {

    String BASE_URL = "http://gw-usertracking.azurewebsites.net/";

    @POST("userevent/batch")
    Call<ResponseBody> sendEvents(@Body List<EventModel> events);

}
