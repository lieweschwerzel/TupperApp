package com.example.tuppermeals;

import com.example.tuppermeals.api.RecipeApi;
import com.example.tuppermeals.api.Service;
import com.example.tuppermeals.model.RecipeResponse;

import retrofit2.Call;

public class RecipeRepository {

    private Service movieService = RecipeApi.create();

    public Call<RecipeResponse> searchRecipes(String search) {
        return movieService.searchRecipes(BuildConfig.RECIPE_API_TOKEN, search);
    }

//    public Call<RecipeResponse> searchRecipes(String search) {
//        return movieService.searchRecipes( search); //
//    }

}


