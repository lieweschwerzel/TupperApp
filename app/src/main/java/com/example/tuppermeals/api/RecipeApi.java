package com.example.tuppermeals.api;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RecipeApi {

//    public static final String BASE_URL = "http://www.recipepuppy.com/api/";
    public static final String BASE_URL = "https://www.food2fork.com/api/";

//    public static Retrofit retrofit = null;

    public static Service create() {

        // Create an OkHttpClient to be able to make a log of the network traffic
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .build();

        Retrofit MovieApi = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return MovieApi.create(Service.class);
    }
    // Return the Retrofit NumbersApiService

//        return retrofit;
}

