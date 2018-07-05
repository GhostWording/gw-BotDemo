package com.ghostwording.chatbot.io.service;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface PictureService {

    String BASE_URL = "http://gw-static.azurewebsites.net/container/files/";
    String HOST_URL = "http://az767698.vo.msecnd.net/";
    String ALTERNATIVE_HOST_URL = "http://gw-static.azurewebsites.net/";
    String POPULAR_HOST_URL = HOST_URL + "canonical/";

    @GET("cvd/{pictureArea}?size=small")
    Call<List<String>> getDefaultPictures(@Path("pictureArea") String pictureArea);

    @GET("{imagePath}?size=small")
    Call<List<String>> getPicturesByPath(@Path("imagePath") String imagePath);

}
