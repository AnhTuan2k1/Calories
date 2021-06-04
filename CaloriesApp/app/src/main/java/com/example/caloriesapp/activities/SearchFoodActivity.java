package com.example.caloriesapp.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.caloriesapp.FoodAdapter;
import com.example.caloriesapp.R;
import com.example.caloriesapp.database.FoodDAO;
import com.example.caloriesapp.database.FoodDatabase;
import com.example.caloriesapp.database.FoodStatic;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;

public class SearchFoodActivity extends AppCompatActivity {

    private EditText editTextSearch;
    private RecyclerView listFood;
    private Button btnOkSearchFood;

    private FoodAdapter foodAdapter;
    private List<FoodStatic> mListFood;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_food);
        SearchFoodActivity.createFoodDatabase(SearchFoodActivity.this);
        anhxa();

        btnOkSearchFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleSearchFood();
            }
        });
        editTextSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_SEARCH)
                {
                    handleSearchFood();
                }
                return false;
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void handleSearchFood() {
        String keyword = editTextSearch.getText().toString().trim();
        mListFood = FoodDatabase.getInstance(this).foodDAO().searchFood(keyword);
        loadListFood();
        hideSoftKeyboard();
    }

    private void loadListFood(List<FoodStatic> list) {
       // Toasty.success(this, "add food successfully", Toast.LENGTH_SHORT, true).show();
        mListFood = list;
        foodAdapter.setData(mListFood);
        listFood.setAdapter(foodAdapter);
    }
    private void loadListFood() {
        foodAdapter.setData(mListFood);
        listFood.setAdapter(foodAdapter);
    }

    private void anhxa() {
        editTextSearch = findViewById(R.id.edittext_searchFood);
        listFood = findViewById(R.id.recycleviewFood);
        btnOkSearchFood = findViewById(R.id.btnOk_searchFood);

        mListFood = new ArrayList<>();  //FoodDatabase.getInstance(this).foodDAO().getListFood();
        foodAdapter = new FoodAdapter();
        foodAdapter.setData(mListFood);
        listFood.setAdapter(foodAdapter);
        listFood.setLayoutManager(new LinearLayoutManager(this));
    }

    public void hideSoftKeyboard()
    {
        try{
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        } catch (NullPointerException ex)
        {
            ex.printStackTrace();
        }
    }


    public static void createFoodDatabase(@NonNull Context context)
    {
        if(!FoodDatabase.getInstance(context).foodDAO().isExistDatabaseFoods(150).isEmpty()) return;
        FoodDatabase.getInstance(context).foodDAO().insertFood(new FoodStatic("Banana", (float)134/150, 150));
        FoodDatabase.getInstance(context).foodDAO().insertFood(new FoodStatic("Apple", (float)95/182, 182));
        FoodDatabase.getInstance(context).foodDAO().insertFood(new FoodStatic("Orange", (float)86/184, 184));
        FoodDatabase.getInstance(context).foodDAO().insertFood(new FoodStatic("Guava", (float)37/55, 55));



    }















}