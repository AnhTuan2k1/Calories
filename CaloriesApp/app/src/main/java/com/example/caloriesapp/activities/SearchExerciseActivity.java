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

import com.example.caloriesapp.A_Excercise;
import com.example.caloriesapp.Exercise;
import com.example.caloriesapp.ExerciseAdapter;
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

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;

public class SearchExerciseActivity extends AppCompatActivity  {

    private EditText editTextSearch;
    private RecyclerView listExercise;
    private Button btnOkSearchExercise;

    private ExerciseAdapter exerciseAdapter;
    private List<Exercise> mListExercise;
    private List<Exercise> xListExercise;
    private String date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_exercise);

        anhxa();

        btnOkSearchExercise.setOnClickListener(new View.OnClickListener() {
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
        exerciseAdapter.OnRecycleViewClickListener(new ExerciseAdapter.OnRecycleViewClickListener() {
            @Override
            public void OnItemClick(int position) {
                openDialog(Gravity.CENTER, mListExercise.get(position), date);// "dd/MM/yyyy"
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void handleSearchFood() {
        String keyword = editTextSearch.getText().toString().trim();
        List<Exercise> list = new ArrayList<>();

        for(int i = 0; i<xListExercise.size(); i++)
        {
            if(xListExercise.get(i).getNameExercise().toLowerCase().startsWith(keyword.toLowerCase()))
                list.add(xListExercise.get(i));
        }

        loadmListFood(list);
        hideSoftKeyboard();
    }

    private void loadmListFood(List<Exercise> list) {
        // Toasty.success(this, "add food successfully", Toast.LENGTH_SHORT, true).show();
        mListExercise = list;
        exerciseAdapter.setData(mListExercise);
        listExercise.setAdapter(exerciseAdapter);
    }
    private void addmListFood(List<Exercise> list) {
        mListExercise.addAll(list);
        exerciseAdapter.setData(mListExercise);
        listExercise.setAdapter(exerciseAdapter);
    }

    private void anhxa() {
        editTextSearch = findViewById(R.id.edittext_searchExercise);
        listExercise = findViewById(R.id.recycleviewExercise);
        btnOkSearchExercise = findViewById(R.id.btnOk_searchExercise);

        mListExercise = new ArrayList<>();
       // addListFood(createFoodList());
        xListExercise = mListExercise = createFoodList();  //FoodDatabase.getInstance(this).foodDAO().getListFood();
        exerciseAdapter = new ExerciseAdapter();
        exerciseAdapter.setData(mListExercise);
        listExercise.setAdapter(exerciseAdapter);
        listExercise.setLayoutManager(new LinearLayoutManager(this));

        Intent intent = getIntent();
        date = intent.getStringExtra(A_Excercise.DATE_EXERCISE);

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

    private void openDialog(int gravity, final Exercise exercise, final String date ) {

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_addexercise);
        Window window = dialog.getWindow();
        if(window == null) return;

        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        windowAttributes.gravity = gravity;
        window.setAttributes(windowAttributes);

        EditText duration = dialog.findViewById(R.id.editText_duration_dialogAddexercise);
        TextView textView_calories = dialog.findViewById(R.id.textView_calories_dialogAddexercise);
        TextView textView_nameExercise = dialog.findViewById(R.id.textView_nameexercise_dialogAddexercise);
        Button button_add = dialog.findViewById(R.id.button_Add_dialogAddexercise);
        Button button_cancel = dialog.findViewById(R.id.button_cancel_dialogAddexercise);
        /////////////////////////////////////////////

        int cal = (int)(exercise.getDuration()*exercise.getCalories());

        textView_calories.setText(String.valueOf(cal));
        textView_nameExercise.setText(exercise.getNameExercise());
        duration.setText(String.valueOf(exercise.getDuration()));

        duration.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(duration.getText().toString().isEmpty())
                {
                    textView_calories.setText("0");
                }
                else
                {
                    textView_calories.setText(String.valueOf((int)(Float.parseFloat(duration.getText().toString())*exercise.getCalories())));
                }
                return false;
            }
        });

        button_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String time = duration.getText().toString();
                if(!time.equals("")  && Integer.parseInt(time)!= 0)
                {
                    Exercise exercise2 = new Exercise(exercise.getNameExercise(), exercise.getCalories(),
                                                        Integer.parseInt(time), date);

                    FirebaseDatabase.getInstance().getReference().child("users")
                            .child(FirebaseAuth.getInstance().getUid())
                            .child("exercise")
                            .child(date)
                            .child(exercise2.getId())
                            .setValue(exercise2)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toasty.success(SearchExerciseActivity.this, "Add exercise Successfully", Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toasty.error(SearchExerciseActivity.this, "something was fail", Toast.LENGTH_SHORT).show();
                        }
                    });
                    // add food locally here
                    dialog.dismiss();
                }
                else
                {
                    Toasty.warning(SearchExerciseActivity.this, "duration has errors", Toast.LENGTH_SHORT).show();
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


    public static List<Exercise> createFoodList()
    {
        List<Exercise> list = new ArrayList<>();
        list.add(new Exercise("Badminton, general", (float)5.355));
        list.add(new Exercise("Basketball, general", (float)5.842));
        list.add(new Exercise("Cycling, general", (float)6.231));
        list.add(new Exercise("Dancing (Aerobic)", (float)7.108));
        list.add(new Exercise("Dancing (Ballet, Modern, Jazz)", (float)4.868));
        list.add(new Exercise("Hiking, general", (float)4.187));
        list.add(new Exercise("Running 6mph (9.7km/h)", (float)9.542));
        list.add(new Exercise("Running 9mph (14.5km/h)", (float)12.463));
        list.add(new Exercise("Running 13mph (20.9km/h)", (float)19.279));
        list.add(new Exercise("Soccer, general", (float)6.816));
        list.add(new Exercise("Swimming, fast", (float)9.055));
        list.add(new Exercise("Swimming, general, leisurely", (float)5.647));
        list.add(new Exercise("Tennis, general", (float)7.108));
        list.add(new Exercise("Volleyball, general", (float)2.921));
        list.add(new Exercise("Walking, 5km/h", (float)4.817));
        list.add(new Exercise("Yoga, general", (float)2.629));


        return list;
    }



}