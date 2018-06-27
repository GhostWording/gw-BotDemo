package com.ghostwording.hugsapp.io.service;

import com.ghostwording.hugsapp.model.Location;

import retrofit2.Call;
import retrofit2.http.GET;

public interface CountryService {

    String BASE_URL = "http://ip-api.com/";

    @GET("json")
    Call<Location> getLocationInformation();

}