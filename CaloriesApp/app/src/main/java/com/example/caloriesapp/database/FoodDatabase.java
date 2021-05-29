package com.example.caloriesapp.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;


@Database(entities = {FoodStatic.class}, version = 1)
public abstract class FoodDatabase extends RoomDatabase {

    private static final String DATABASE_NAME = "foods.db";
    private static FoodDatabase instance;

    public static synchronized FoodDatabase getInstance(Context context)
    {
        if(instance == null)
        {
            instance = Room.databaseBuilder(context.getApplicationContext(), FoodDatabase.class , DATABASE_NAME)
                    .allowMainThreadQueries()
                    .build();
        }
        return instance;
    }

    public  abstract FoodDAO foodDAO();





















}
