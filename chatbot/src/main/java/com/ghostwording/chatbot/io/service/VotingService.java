package com.ghostwording.chatbot.io.service;

import com.ghostwording.chatbot.model.VotingCounters;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface VotingService {

    String BASE_URL = "http://api.cvd.io/";

    @POST("vote/GlobalCounters/Set")
    Call<ResponseBody> vote(@Body VotingCounters votingCounters);

    @GET("vote/GlobalCounters/{counterId}")
    Call<VotingCounters.VotingCounter> getVotes(@Path("counterId") String counterId);

}