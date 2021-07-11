package com.example.caloriesapp.database;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "FoodStatic")
public class FoodStatic {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String nameFood;
    private float calories;  //   Cal/g
    private int gram;      //   common gram

    public FoodStatic(String nameFood, float calories, int gram) {
        this.nameFood = nameFood;
        this.calories = calories;
        this.gram = gram;
    }

    public FoodStatic() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNameFood() {
        return nameFood;
    }

    public void setNameFood(String nameFood) {
        this.nameFood = nameFood;
    }

    public float getCalories() {
        return calories;
    }

    public void setCalories(float calories) {
        this.calories = calories;
    }

    public int getGram() {
        return gram;
    }

    public void setGram(int gram) {
        this.gram = gram;
    }
}
