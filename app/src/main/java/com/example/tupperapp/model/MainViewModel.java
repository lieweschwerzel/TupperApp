package com.example.tupperapp.model;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.example.tupperapp.RecipeRepository;
import com.example.tupperapp.database.TupperMealRepository;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.support.constraint.Constraints.TAG;

public class MainViewModel extends AndroidViewModel {

    private TupperMealRepository mRepository;
    private LiveData<List<TupperMeal>> mTupperMeals;
    private RecipeRepository recipeRepository = new RecipeRepository();
    private MutableLiveData<List<Recipe>> mRecipes = new MutableLiveData<>();
    private MutableLiveData<String> error = new MutableLiveData<>();

    public MainViewModel(@NonNull Application application) {
        super(application);
        mRepository = new TupperMealRepository(application.getApplicationContext());
        mTupperMeals = mRepository.getAllGameBacklogs();
    }

    public LiveData<List<TupperMeal>> getmTupperMeals() {
        return mTupperMeals;
    }

//    public MainActivityViewModel(@NonNull Application application) {
//        super(application);
//    }

    public MutableLiveData<String> getError() {
        return error;
    }

    public MutableLiveData<List<Recipe>> getAllRecipes() {
        return mRecipes;
    }


    public String searchRecipes(String search) {
        recipeRepository
                .searchRecipes(search)
                .enqueue(new Callback<RecipeResponse>() {
                    @Override
                    public void onResponse(Call<RecipeResponse> call, Response<RecipeResponse> response) {
                        List<Recipe> recipes = response.body().getRecipes();
                        if (response != null){
                            mRecipes.setValue(recipes);
//                        trivia.setValue(response.body().getText());
//                        Toast.makeText(getApplication(), recipes.toString(), Toast.LENGTH_LONG).show();
//                        Toast.makeText(this, mRecipes.toString(), Toast.LENGTH_SHORT).show();
//                            Log.d(TAG, recipes.get(1).toString());
                        }
                    }

                    /**
                     * Invoked when a network exception occurred talking to the server or when an unexpected
                     * exception occurred creating the request or processing the response.
                     *
                     * @param call
                     * @param t
                     */
                    @Override
                    public void onFailure(Call<RecipeResponse> call, Throwable t) {
                        Log.d("Errror", t.getMessage());
                        Toast.makeText(getApplication(), "Errror Fetching Data!", Toast.LENGTH_SHORT).show();
                    }

                });
        return search;
    }

    public void insert(TupperMeal tupperMeal) {
        mRepository.insert(tupperMeal);
    }

    public void update(TupperMeal tupperMeal) {
        mRepository.update(tupperMeal);
    }

    public void delete(TupperMeal tupperMeal) {
        mRepository.delete(tupperMeal);
    }
}
