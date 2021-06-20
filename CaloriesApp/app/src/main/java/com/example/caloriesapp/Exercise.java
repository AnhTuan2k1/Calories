package com.example.caloriesapp;

import java.util.UUID;
import java.util.concurrent.Executor;

public class Exercise {
    private String id;
    private String nameExercise;
    private float calories;  //   Cal/minute
    private int duration;    // minute  default: 60
    private String date;  // vd 11/11/2020

    public Exercise(){}

    public Exercise(String nameExercise, float calories, int duration, String date) {
        this.nameExercise = nameExercise;
        this.calories = calories;
        this.duration = duration;
        this.date = date;

        this.id = UUID.randomUUID().toString();
    }

    public Exercise(String nameExercise, float calories) {
        this.nameExercise = nameExercise;
        this.calories = calories;

        this.duration = 60;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNameExercise() {
        return nameExercise;
    }

    public void setNameExercise(String nameExercise) {
        this.nameExercise = nameExercise;
    }

    public float getCalories() {
        return calories;
    }

    public void setCalories(float calories) {
        this.calories = calories;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
