package com.example.oleg.sunrisesunset_v2.SunriseSunsetApi;
import com.example.oleg.sunrisesunset_v2.SunriseSunsetApi.PostList;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public class SandSApi {
    public static final String url = "https://api.sunrise-sunset.org";


    public static PostService postService = null;
    public static PostService getPostService(String s) {
        if(postService == null){

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(s)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            postService = retrofit.create(PostService.class);

        }
        return postService;
    }

    public interface PostService {

        @GET("/json")
        Call<PostList>  getPostCity(@Query("lat") String lat, @Query("lng") String lng  );
    }

}
