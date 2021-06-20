package com.example.caloriesapp;

public class CaloDaily {

    private String date;
    private float Calories;

    public CaloDaily(){}

    public CaloDaily(String date, float calories) {
        this.date = date;
        Calories = calories;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public float getCalories() {
        return Calories;
    }

    public void setCalories(float calories) {
        Calories = calories;
    }
}
