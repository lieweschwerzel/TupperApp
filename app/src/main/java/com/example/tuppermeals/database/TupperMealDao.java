package com.example.tuppermeals.database;


import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.tuppermeals.model.TupperMeal;


import java.util.List;

@Dao
public interface TupperMealDao {

    @Query("SELECT * FROM TupperMeal")
    public LiveData<List<TupperMeal>> getAllGameBacklogs();

    @Insert
    void insertGameBacklog(TupperMeal tupperMeal);

    @Delete
    void deleteGameBacklog(TupperMeal tupperMeal);

    @Update
    void updateGameBacklog(TupperMeal tupperMeal);
}

