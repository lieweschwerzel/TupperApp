package com.example.tupperapp.database;

import android.arch.lifecycle.LiveData;
import android.content.Context;

import com.example.tupperapp.model.TupperMeal;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class TupperMealRepository {

    private TupperMealRoomDatabase mAppDatabase;
    private TupperMealDao mTupperMealDao;
    private LiveData<List<TupperMeal>> mTupperMeals;
    private Executor mExecutor = Executors.newSingleThreadExecutor();

    public TupperMealRepository(Context context) {
        mAppDatabase = TupperMealRoomDatabase.getDatabase(context);
        mTupperMealDao = mAppDatabase.tupperMealDao();
        mTupperMeals = mTupperMealDao.getAllGameBacklogs();
    }

    public LiveData<List<TupperMeal>> getAllGameBacklogs() {
        return mTupperMeals;
    }

    public void insert(final TupperMeal tupperMeal) {
        mExecutor.execute(new Runnable() {
            @Override
            public void run() {
                mTupperMealDao.insertGameBacklog(tupperMeal);
            }
        });
    }

    public void update(final TupperMeal tupperMeal) {
        mExecutor.execute(new Runnable() {
            @Override
            public void run() {
                mTupperMealDao.updateGameBacklog(tupperMeal);
            }
        });
    }

    public void delete(final TupperMeal tupperMeal) {
        mExecutor.execute(new Runnable() {
            @Override
            public void run() {
                mTupperMealDao.deleteGameBacklog(tupperMeal);
            }
        });
    }
}
