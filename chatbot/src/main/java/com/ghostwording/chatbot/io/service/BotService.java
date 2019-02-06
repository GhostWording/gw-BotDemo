package com.ghostwording.chatbot.io.service;

import com.ghostwording.chatbot.chatbot.model.BotSequence;
import com.ghostwording.chatbot.chatbot.model.ChatMessage;
import com.ghostwording.chatbot.chatbot.model.SequenceMasterFileResponse;
import com.ghostwording.chatbot.model.requests.FragmentRequest;
import com.ghostwording.chatbot.model.requests.SequenceRequest;
import com.ghostwording.chatbot.model.requests.UserProperty;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface BotService {

    String BASE_URL = "http://api.cvd.io/";

    @GET("bot/stickers/talktobot/?command=cardset&top=10")
    Call<List<ChatMessage.BotMessage>> getBotSuggestion(@Query("text") String text);

    @POST("botapis/user/setproperty")
    Call<ResponseBody> postUserProperty(@Body UserProperty userProperty);

    @POST("botapis/sequences/next")
    Call<BotSequence> getNextSequence(@Body SequenceRequest sequenceRequest);

    @POST("botapis/sequences/fragment")
    Call<BotSequence> getFragment(@Body FragmentRequest sequenceRequest);

    @GET("botapis/sequences/master/sequencesForMaster")
    Call<List<SequenceMasterFileResponse>> getMasterSequences(@Query("botName") String botName);

}
