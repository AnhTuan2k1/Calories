package com.example.caloriesapp.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.caloriesapp.A_Breakfast;
import com.example.caloriesapp.A_Lunch;
import com.example.caloriesapp.FoodAdapter;
import com.example.caloriesapp.Foodate;
import com.example.caloriesapp.R;
import com.example.caloriesapp.database.FoodDAO;
import com.example.caloriesapp.database.FoodDatabase;
import com.example.caloriesapp.database.FoodStatic;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;

public class SearchFoodActivity extends AppCompatActivity  {

    private EditText editTextSearch;
    private RecyclerView listFood;
    private Button btnOkSearchFood;
    private String sessionofday_breakfast,sessionofday_lunch;
    private String date_breakfast,date_lunch;
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
        foodAdapter.OnRecycleViewClickListener(new FoodAdapter.OnRecycleViewClickListener() {
            @Override
            public void OnItemClick(int position, String nameFood, String gram, String Calories) {
                openDialog(Gravity.CENTER, nameFood, Float.parseFloat(Calories),
                        Float.parseFloat(gram), "sessionofday", "date"); // "dd/MM/yyyy"
  //              Toasty.success(SearchFoodActivity.this, nameFood, Toast.LENGTH_LONG).show();
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
        sessionofday_breakfast = null;
        date_lunch = null;
        mListFood = new ArrayList<>();  //FoodDatabase.getInstance(this).foodDAO().getListFood();
        foodAdapter = new FoodAdapter();
        foodAdapter.setData(mListFood);
        listFood.setAdapter(foodAdapter);
        listFood.setLayoutManager(new LinearLayoutManager(this));

        Intent intent = getIntent();
        sessionofday_breakfast = intent.getStringExtra(A_Breakfast.SESSIONOFDAY_BREAKFAST);
        date_breakfast = intent.getStringExtra(A_Breakfast.DATE_BREAKFAST);
        sessionofday_lunch = intent.getStringExtra(A_Lunch.SESSIONOFDAY_LUNCH);
        date_lunch = intent.getStringExtra(A_Lunch.DATE_LUNCH);
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

    private void openDialog(int gravity, final String nameFood, final float calories, final float gram, String sessionofday, String date) {

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_addfood);

        Window window = dialog.getWindow();
        if(window == null) return;

        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        windowAttributes.gravity = gravity;
        window.setAttributes(windowAttributes);

        EditText editText_gram = dialog.findViewById(R.id.editText_gram_dialogAddfood);
        TextView textView_calories = dialog.findViewById(R.id.textView_calories_dialogAddfood);
        TextView textView_namefood = dialog.findViewById(R.id.textView_namefood_dialogAddfood);
        Button button_add = dialog.findViewById(R.id.button_Add_dialogAddfood);
        Button button_cancel = dialog.findViewById(R.id.button_cancel_dialogAddfood);
        /////////////////////////////////////////////
        float cal = calories/gram;
        textView_calories.setText(String.valueOf(calories));
        textView_namefood.setText(nameFood);
        editText_gram.setText(String.valueOf((int)gram));

        editText_gram.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(editText_gram.getText().toString().isEmpty())
                {
                    textView_calories.setText("0");
                }
                else
                {
                    textView_calories.setText(String.valueOf((int)(Float.parseFloat(editText_gram.getText().toString())*cal)));
                }
                return false;
            }
        });

        button_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String grams = editText_gram.getText().toString();
                if(!grams.equals("")  && Integer.parseInt(grams)!= 0)
                {
                    Foodate foodate = new Foodate(nameFood, cal, Integer.parseInt(grams), sessionofday, date);

                    FirebaseDatabase.getInstance().getReference().child("users")
                            .child(FirebaseAuth.getInstance().getUid())
                            .child("foodate")
                            .child(date)
                            .child(foodate.getId())
                            .setValue(foodate)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toasty.success(SearchFoodActivity.this, "Add " + nameFood + " Successfully", Toast.LENGTH_SHORT).show();


                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toasty.error(SearchFoodActivity.this, "something was fail", Toast.LENGTH_SHORT).show();
                        }
                    });
                    // add food locally here
                    dialog.dismiss();
                }
                else
                {
                    Toasty.warning(SearchFoodActivity.this, "gram has errors", Toast.LENGTH_SHORT).show();
                }


            }
        });

        button_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        ///////////////////////////////////

        //dialog.setCancelable(false);
        dialog.show();
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