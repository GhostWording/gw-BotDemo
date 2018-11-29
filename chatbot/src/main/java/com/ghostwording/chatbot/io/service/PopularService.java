package com.ghostwording.chatbot.io.service;

import com.ghostwording.chatbot.model.PopularImages;
import com.ghostwording.chatbot.model.texts.PopularTexts;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface PopularService {

    String BASE_URL = "http://api.cvd.io/popular/";

    @GET("{areaId}/popularimages/intention/{intentionId}?maxrank=12")
    Call<List<PopularImages>> getPopularImages(@Path("areaId") String areaId, @Path("intentionId") String intentionId);

    @GET("General/popularimages/intention/{intentionId}?maxrank=12")
    Call<List<PopularImages>> getPopularImages(@Path("intentionId") String intentionId);

    @GET("{areaId}/populartexts/intention/{intentionId}?maxrank=12")
    Call<List<PopularTexts>> getPopularTexts(@Path("areaId") String areaId, @Path("intentionId") String intentionId);

    @GET("{areaId}/matchingtexts/image/{imageName}?maxrank=16")
    Call<List<PopularTexts>> getPopularTextsForImage(@Path("areaId") String areaId, @Path("imageName") String imageName);

    @GET("{areaId}/matchingimages/prototypeids/{prototypeId}?maxrank=8")
    Call<List<PopularImages>> getPopularImagesForText(@Path("areaId") String areaId, @Path("prototypeId") String prototypeId);

    @GET("General/matchingimages/prototypeids/{prototypeId}?maxrank=8")
    Call<List<PopularImages>> getPopularImagesForText(@Path("prototypeId") String prototypeId);

    @GET("{areaId}/popularimages/UserProperty/MBTISelected/{mbtiType}?maxrank=12")
    Call<List<PopularImages>> getImagesByMbtiType(@Path("areaId") String areaId, @Path("mbtiType") String mbtiType);

    @GET("{areaId}/populartexts/UserProperty/MBTISelected/{mbtiType}?maxrank=12")
    Call<PopularTexts> getMessagesByMbtiType(@Path("areaId") String areaId, @Path("mbtiType") String mbtiType);

    @GET("{areaId}/popularimages/{themePath}")
    Call<List<PopularImages>> getPopularImagesForTheme(@Path("areaId") String areaId, @Path("themePath") String themePath, @Query("maxrank") int maxRank);

    @GET("{areaId}/populartexts/{areaId}?maxrank=200")
    Call<List<PopularTexts.Text>> getMostPopularTexts(@Path("areaId") String areaId);
}
