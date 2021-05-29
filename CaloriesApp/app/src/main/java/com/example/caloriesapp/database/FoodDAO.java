package com.example.caloriesapp.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;


import java.util.List;

@Dao
public interface FoodDAO {

    @Insert
    void insertFood(FoodStatic foodStatic);

    @Query("SELECT * FROM FoodStatic")
    List<FoodStatic> getListFood();

    @Query("SELECT * FROM FOODSTATIC WHERE gram= :gram")
    List<FoodStatic> isExistDatabaseFoods(int gram);

    @Query("SELECT * FROM FOODSTATIC WHERE nameFood LIKE '%' || :name || '%'")
    List<FoodStatic> searchFood(String name);


















}
