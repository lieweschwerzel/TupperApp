package com.example.tupperapp.model;

import java.util.List;

import com.example.tupperapp.model.Recipe;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RecipeResponse {

    @SerializedName("count")
    @Expose
    private Integer count;
    @SerializedName("recipes")
    @Expose
    private List<Recipe> recipes = null;

    /**
     * No args constructor for use in serialization
     *
     */
    public RecipeResponse() {
    }

    /**
     *
     * @param count
     * @param recipes
     */
    public RecipeResponse(Integer count, List<Recipe> recipes) {
        super();
        this.count = count;
        this.recipes = recipes;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public List<Recipe> getRecipes() {
        return recipes;
    }

    public void setRecipes(List<Recipe> recipes) {
        this.recipes = recipes;
    }

}