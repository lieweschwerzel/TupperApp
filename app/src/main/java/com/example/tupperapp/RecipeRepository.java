package com.example.tupperapp;

import com.example.tupperapp.api.RecipeApi;
import com.example.tupperapp.api.Service;
import com.example.tupperapp.model.RecipeResponse;

import retrofit2.Call;

public class RecipeRepository {

    private Service movieService = RecipeApi.create();

//    public Call<RecipeResponse> searchRecipes(String search) {
//        return movieService.searchRecipes(search);
//    }

    public Call<RecipeResponse> searchRecipes(String search) {
        return movieService.searchRecipes( search); //BuildConfig.THE_MOVIE_DB_API_TOKEN,
    }

}


