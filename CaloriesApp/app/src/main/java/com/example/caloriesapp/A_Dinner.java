package com.example.caloriesapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.caloriesapp.activities.SearchFoodActivity;
import com.example.caloriesapp.database.FoodStatic;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class A_Dinner extends AppCompatActivity {
    private RecyclerView recyclerView;
    private FoodAdapter foodAdapter;
    private List<FoodStatic> mListFood;
    private FloatingActionButton floatingActionButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_a__dinner);


        floatingActionButton = findViewById(R.id.dinner_fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Open_SearchFoodActivity();
            }
        });

        recyclerView = findViewById(R.id.dinnerlist);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        foodAdapter = new FoodAdapter();
        recyclerView.setAdapter(foodAdapter);
        mListFood = new ArrayList<>();

        String a = "AkaKiwi";
        int b = 2;
        float c = 123/22;

        FoodStatic food = new FoodStatic(a,c,b);
        food.setCalories(c);
        food.setGram(b);
        food.setNameFood(a);
        food.setId(2);

        mListFood.add(food);
        mListFood.add(food);
        mListFood.add(food);



        foodAdapter.setData(mListFood);
    }


    public void Open_SearchFoodActivity(){
        Intent intent = new Intent(this, SearchFoodActivity.class);
        startActivity(intent);
    }
}