package com.example.tuppermeals.api;

import com.example.tuppermeals.model.RecipeResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;


public interface Service {

//    @GET("search")
//    Call<RecipeResponse> searchRecipes(@Query("key") String apiKey, @Query("q") String search);

    @GET("?")
    Call<RecipeResponse> searchRecipes(@Query("q") String search);

}
