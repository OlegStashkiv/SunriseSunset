package com.example.oleg.sunrisesunset_v2.GoogleMapApi;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public class GoogleMapApi {
    public static final String g_m_url = "https://maps.googleapis.com";
    public static final String KEY = "AIzaSyCVHqoRymahvbyoFCyTzwzxxzOaf3sXOCk";

    public static GoogleService googleService = null;
    public static GoogleService getgoogleService(String s) {
        if(googleService == null){

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(s)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            googleService = retrofit.create(GoogleService.class);

        }
        return googleService;
    }

    public interface GoogleService {
        @GET("/maps/api/geocode/json")
        Call<CordinatesList> getAdress(@Query("address") String city_name, @Query("key") String key );
    }
}
