package com.example.tupperapp.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.example.tupperapp.model.TupperMeal;

@Database(entities = {TupperMeal.class}, version = 1, exportSchema = false)
public abstract class TupperMealRoomDatabase extends RoomDatabase {
    private final static String NAME_DATABASE = "tm9_database";

    public abstract TupperMealDao tupperMealDao();

    private static volatile TupperMealRoomDatabase INSTANCE;

    public static TupperMealRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (TupperMealRoomDatabase.class) {
                if (INSTANCE == null) {
                    // Create database here
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            TupperMealRoomDatabase.class, NAME_DATABASE)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

}
