package com.example.tupperapp;

import com.example.tupperapp.api.MovieApi;
import com.example.tupperapp.api.Service;
import com.example.tupperapp.model.RecipeResponse;

import retrofit2.Call;

public class RecipeRepository {

    private Service movieService = MovieApi.create();

    public Call<RecipeResponse> searchRecipes(String search) {
        return movieService.searchRecipes(search);
    }

}


