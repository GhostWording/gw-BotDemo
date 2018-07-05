package com.ghostwording.chatbot.io.service;

import com.ghostwording.chatbot.chatbot.model.BotSequence;
import com.ghostwording.chatbot.chatbot.model.CarouselMessage;
import com.ghostwording.chatbot.model.AdvertPlacements;
import com.ghostwording.chatbot.model.GifIdsResponse;
import com.ghostwording.chatbot.model.GifImages;
import com.ghostwording.chatbot.model.NotificationsModel;
import com.ghostwording.chatbot.model.Promotions;
import com.ghostwording.chatbot.model.SequencesResponse;
import com.ghostwording.chatbot.model.ThemeResponse;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ConfigService {

    String BASE_URL = "https://raw.githubusercontent.com/GhostWording/gw-config-apis/master/";

    @GET("data/bot/sequences/masterfiles/fragmentfiles.json")
    Call<HashMap<String, String>> getFragments();

    @GET("data/bot/apps/emoji-hero/master-Hero.json")
    Call<SequencesResponse> getBotQuestions();

    @GET("data/bot/apps/emoji-hero/master-Test.json")
    Call<SequencesResponse> getTestQuestions();

    @GET("{path}")
    Call<BotSequence> getBotSequence(@Path("path") String path);

    @GET("data/common/giphycontent/trendinggifs.json")
    Call<GifIdsResponse> getTrendingGifIds();

    @GET("data/common/gifsnewformat/pnghugintro.json")
    Call<GifImages> getPreviewGifIds();

    @GET("data/common/gifsnewformat/hugsview1.json")
    Call<GifImages> getHugGifs();

    @GET("data/appspecific/ineedhugs/tryme-multilanguage.json")
    Call<Promotions> getAppPromotions();

    @GET("data/appspecific/savethekitten/notificationtexts.json")
    Call<NotificationsModel> getNotifications();

    @GET("data/appspecific/emojicartoon/giphypremiumthemes2.json")
    Call<ThemeResponse> getPremiumThemes();

    @GET("data/emojihero/sticker-themes.json")
    Call<ThemeResponse> getStickerThemes();

    @GET("data/appspecific/emojicartoon/advertIDsAndroid.json")
    Call<AdvertPlacements> getAdPlacements();

    @GET("data/emojihero/emoji-themes.json")
    Call<ThemeResponse> getEmojiThemes();

    @GET("{path}")
    Call<GifImages> getGiffsByPath(@Path("path") String path);

    @GET("{path}")
    Call<GifImages> getGiffsByPathNewFormat(@Path("path") String path);

    @GET("{path}")
    Call<CarouselMessage> getCarouselItems(@Path("path") String path);

}