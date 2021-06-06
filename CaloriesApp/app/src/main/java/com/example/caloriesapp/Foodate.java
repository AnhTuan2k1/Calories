package com.example.caloriesapp;

import java.util.UUID;

public class Foodate {
    private String id;
    private String nameFood;
    private float calories;  //   Cal/g
    private int gram;    // total gram ate
    private String sessionofday;  // breakfast, lunch, diner, snacks
    private String date;  // vd 11/11/2020

    public Foodate(){}

    public Foodate(String nameFood, float calories, int gram, String sessionofday, String date) {
        this.nameFood = nameFood;
        this.calories = calories;
        this.gram = gram;
        this.sessionofday = sessionofday;
        this.date = date;
        this.id = UUID.randomUUID().toString();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
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

    public String getSessionofday() {
        return sessionofday;
    }

    public void setSessionofday(String sessionofday) {
        this.sessionofday = sessionofday;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
