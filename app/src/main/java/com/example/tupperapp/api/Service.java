package com.example.tupperapp.api;

import com.example.tupperapp.model.RecipeResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;


public interface Service {

    @GET("?")
    Call<RecipeResponse> searchRecipes(@Query("q") String search);
}
