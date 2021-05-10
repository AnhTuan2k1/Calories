package com.example.caloriesapp;
public class User {
    public String Aim;
    public String Name;
    public Float CurrentWeight;
    public Float GoalWeight;
    public Integer Sex;
    public Float CurrentHeight;
    public Integer Age;


    public User(String aim) {
        Aim = aim;
    }

    public User(String name, Float currentWeight, Float goalWeight, Integer sex, Float currentHeight, Integer age) {
        Name = name;
        CurrentWeight = currentWeight;
        GoalWeight = goalWeight;
        Sex = sex;
        CurrentHeight = currentHeight;
        Age = age;
    }
    public String getAim() {
        return Aim;
    }


    public String getName() {
        return Name;
    }

    public Float getCurrentWeight() {
        return CurrentWeight;
    }

    public Float getGoalWeight() {
        return GoalWeight;
    }

    public Integer getSex() {
        return Sex;
    }

    public Float getCurrentHeight() {
        return CurrentHeight;
    }

    public Integer getAge() {
        return Age;
    }

    public void setAim(String aim) {
        Aim = aim;
    }

    public void setName(String name) {
        Name = name;
    }

    public void setCurrentWeight(Float currentWeight) {
        CurrentWeight = currentWeight;
    }

    public void setGoalWeight(Float goalWeight) {
        GoalWeight = goalWeight;
    }

    public void setSex(Integer sex) {
        Sex = sex;
    }

    public void setCurrentHeight(Float currentHeight) {
        CurrentHeight = currentHeight;
    }

    public void setAge(Integer age) {
        Age = age;
    }



}
