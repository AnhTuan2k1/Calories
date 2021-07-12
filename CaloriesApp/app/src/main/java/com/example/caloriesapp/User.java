package com.example.caloriesapp;

public class User {

    String userName;
    String purposeWeight;    //lose maintain gain
    String gender;
    String timetogetGoalweight; // "dd/MM/yyyy"
    String image;
    float goalWeight;
    float currentWeight;
    int age;
    float height;
    float ExerciseIntensity;
    float dailyCaloriesTarget;


    public User(){}

    public User(String purposeWeight, String gender, String timetogetGoalweight, float goalWeight, float currentWeight,
                int age, float height, float exerciseIntensity, float dailyCaloriesTarget, String userName) {
        this.purposeWeight = purposeWeight;
        this.gender = gender;
        this.timetogetGoalweight = timetogetGoalweight;
        this.goalWeight = goalWeight;
        this.currentWeight = currentWeight;
        this.age = age;
        this.height = height;
        ExerciseIntensity = exerciseIntensity;
        this.dailyCaloriesTarget = dailyCaloriesTarget;
        this.userName = userName;
        image = null;
    }

    public User(String purposeWeight, String gender, String timetogetGoalweight, float goalWeight,
                float currentWeight, int age, float height, float exerciseIntensity, float dailyCaloriesTarget) {
        this.purposeWeight = purposeWeight;
        this.gender = gender;
        this.timetogetGoalweight = timetogetGoalweight;
        this.goalWeight = goalWeight;
        this.currentWeight = currentWeight;
        this.age = age;
        this.height = height;
        ExerciseIntensity = exerciseIntensity;
        this.dailyCaloriesTarget = dailyCaloriesTarget;
        this.userName = "";
        image = null;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPurposeWeight() {
        return purposeWeight;
    }

    public void setPurposeWeight(String purposeWeight) {
        this.purposeWeight = purposeWeight;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getTimetogetGoalweight() {
        return timetogetGoalweight;
    }

    public void setTimetogetGoalweight(String timetogetGoalweight) {
        this.timetogetGoalweight = timetogetGoalweight;
    }

    public float getGoalWeight() {
        return goalWeight;
    }

    public void setGoalWeight(float goalWeight) {
        this.goalWeight = goalWeight;
    }

    public float getCurrentWeight() {
        return currentWeight;
    }

    public void setCurrentWeight(float currentWeight) {
        this.currentWeight = currentWeight;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public float getExerciseIntensity() {
        return ExerciseIntensity;
    }

    public void setExerciseIntensity(float exerciseIntensity) {
        ExerciseIntensity = exerciseIntensity;
    }

    public float getDailyCaloriesTarget() {
        return dailyCaloriesTarget;
    }

    public void setDailyCaloriesTarget(float dailyCaloriesTarget) {
        this.dailyCaloriesTarget = dailyCaloriesTarget;
    }
}
