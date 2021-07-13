package com.example.caloriesapp.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.caloriesapp.A_Breakfast;
import com.example.caloriesapp.A_Excercise;
import com.example.caloriesapp.FoodAdapter;
import com.example.caloriesapp.Foodate;
import com.example.caloriesapp.R;
import com.example.caloriesapp.database.FoodDatabase;
import com.example.caloriesapp.database.FoodStatic;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import es.dmoral.toasty.Toasty;
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class SearchFoodActivity extends AppCompatActivity  {

    private EditText editTextSearch;
    private RecyclerView listFood;
    private RecyclerView recycleviewYourFood;
    private Button btnOkSearchFood;
    private String sessionofday_breakfast;
    private String date_breakfast;
    private FoodAdapter foodAdapter;
    private List<FoodStatic> mListFood;
    private TextView addYourFood;
    private ArrayList<FoodStatic> mListFoodCustom;
    private FoodAdapter foodAdapterCustom;
    private FoodStatic deletedFood = null;

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
        addYourFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCustomFoodDialog(Gravity.CENTER);
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
        editTextSearch.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                handleAutoSearchFood();
                return false;
            }
        });
        foodAdapter.OnRecycleViewClickListener(new FoodAdapter.OnRecycleViewClickListener() {
            @Override
            public void OnItemClick(int position, String nameFood, String gram, String Calories) {
                openDialog(Gravity.CENTER, nameFood, Float.parseFloat(Calories),
                        Float.parseFloat(gram), sessionofday_breakfast, date_breakfast); // "dd/MM/yyyy"
  //              Toasty.success(SearchFoodActivity.this, nameFood, Toast.LENGTH_LONG).show();
            }
        });
        foodAdapterCustom.OnRecycleViewClickListener(new FoodAdapter.OnRecycleViewClickListener() {
            @Override
            public void OnItemClick(int position, String nameFood, String gram, String Calories) {
                openDialog(Gravity.CENTER, nameFood, Float.parseFloat(Calories),
                        Float.parseFloat(gram), sessionofday_breakfast, date_breakfast); // "dd/MM/yyyy"
                //              Toasty.success(SearchFoodActivity.this, nameFood, Toast.LENGTH_LONG).show();
            }
        });

        updatemyFoodateList();
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recycleviewYourFood);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(getApplicationContext(), A_Breakfast.class);
        intent.putExtra("key",date_breakfast);
        intent.putExtra("session",sessionofday_breakfast);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
    }

    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }


        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            int positon = viewHolder.getBindingAdapterPosition();

            switch (direction) {
                case ItemTouchHelper.LEFT:
                    deletedFood = mListFoodCustom.get(positon);
                    mListFoodCustom.remove(positon);
                    FirebaseDatabase.getInstance().getReference().child("users")
                            .child(FirebaseAuth.getInstance().getUid())
                            .child("myfood")
                            .setValue(mListFoodCustom);
                    foodAdapterCustom.notifyItemRemoved(positon);
                    foodAdapterCustom.setData(mListFoodCustom);
                    recycleviewYourFood.setAdapter(foodAdapterCustom);


                    Snackbar.make(recycleviewYourFood, deletedFood.getNameFood(), BaseTransientBottomBar.LENGTH_LONG)
                            .setAction("Undo", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    mListFoodCustom.add(positon, deletedFood);
                                    foodAdapterCustom.notifyItemInserted(positon);
                                    foodAdapterCustom.setData(mListFoodCustom);
                                    recycleviewYourFood.setAdapter(foodAdapterCustom);

                                    FirebaseDatabase.getInstance().getReference().child("users")
                                            .child(FirebaseAuth.getInstance().getUid())
                                            .child("myfood")
                                            .setValue(mListFoodCustom);

                                }
                            }).show();
                    break;
                case ItemTouchHelper.RIGHT:
                    showCustomFoodDialog(Gravity.CENTER, positon);
                    break;

                default:
                    break;
            }
        }
        public void onChildDraw (@NotNull Canvas c, @NotNull RecyclerView recyclerView, @NotNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive){

            new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                    .addSwipeLeftBackgroundColor(ContextCompat.getColor(SearchFoodActivity.this, R.color.gray_item_nav))
                    .addSwipeLeftActionIcon(R.drawable.ic_baseline_delete_sweep_24)
                    .addSwipeRightBackgroundColor(ContextCompat.getColor(SearchFoodActivity.this, R.color.gray_item_nav))
                    .addSwipeRightActionIcon(R.drawable.ic_baseline_edit_24)
                    .create()
                    .decorate();

            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
    };

    private void handleSearchFood() {
        String keyword = editTextSearch.getText().toString().trim();
        mListFood = FoodDatabase.getInstance(this).foodDAO().searchFood(keyword);
        loadListFood();
        hideSoftKeyboard();
    }

    private void handleAutoSearchFood() {
//        String keyword = editTextSearch.getText().toString().trim();
//        mListFood = FoodDatabase.getInstance(this).foodDAO().searchFood(keyword);
//        loadListFood();
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

    private void updatemyFoodateList() {
        mListFoodCustom.clear();
        FirebaseDatabase.getInstance().getReference().child("users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("myfood").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren())
                {
                    FoodStatic foodStatic = dataSnapshot.getValue(FoodStatic.class);
                    mListFoodCustom.add(foodStatic);
                }

                foodAdapterCustom.setData(mListFoodCustom);
                recycleviewYourFood.setAdapter(foodAdapterCustom);
                // update ui here with foodateList
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                if(getApplicationContext() != null)
                    Toasty.error(getApplicationContext(), error.getMessage(),Toasty.LENGTH_SHORT).show();
            }
        });
    }

    private void anhxa() {

        editTextSearch = findViewById(R.id.edittext_searchFood);
        listFood = findViewById(R.id.recycleviewFood);
        btnOkSearchFood = findViewById(R.id.btnOk_searchFood);
        sessionofday_breakfast = null;
        mListFood = new ArrayList<>();  //FoodDatabase.getInstance(this).foodDAO().getListFood();
        foodAdapter = new FoodAdapter();
        foodAdapter.setData(mListFood);
        listFood.setAdapter(foodAdapter);
        listFood.setLayoutManager(new LinearLayoutManager(this));
        addYourFood = findViewById(R.id.addYourFood_searchfood);

        recycleviewYourFood = findViewById(R.id.recycleviewYourFood);
        mListFoodCustom = new ArrayList<>();
        foodAdapterCustom = new FoodAdapter();
        recycleviewYourFood.setLayoutManager(new LinearLayoutManager(getApplicationContext()));



        Intent intent = getIntent();
        sessionofday_breakfast = intent.getStringExtra(A_Breakfast.SESSIONOFDAY_BREAKFAST);
        date_breakfast = intent.getStringExtra(A_Breakfast.DATE_BREAKFAST);
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

    private void showCustomFoodDialog(int gravity) {
        final Dialog dialogg = new Dialog(this);
        dialogg.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogg.setContentView(R.layout.dialog_customfood);

        Window window = dialogg.getWindow();
        if(window == null) return;

        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        windowAttributes.gravity = gravity;
        window.setAttributes(windowAttributes);

        EditText editText_gram = dialogg.findViewById(R.id.editText_gram_dialogCustomfood);
        EditText  editText_calories = dialogg.findViewById(R.id.textView_calories_dialogCustomfood);
        EditText editText_namefood = dialogg.findViewById(R.id.editText_namefood_dialogCustomfood);
        Button button_add = dialogg.findViewById(R.id.button_Add_dialogCustomfood);
        Button button_cancel = dialogg.findViewById(R.id.button_cancel_dialogCustomfood);
        /////////////////////////////////////////////


        button_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String grams = editText_gram.getText().toString();
                if(!grams.equals("")  && Integer.parseInt(grams)!= 0)
                {
                    float calori = Float.parseFloat(editText_calories.getText().toString());
                    String namef = editText_namefood.getText().toString();
                    if(calori <= 0 || namef.isEmpty()) return;

                    FoodStatic foodate = new FoodStatic(namef, calori/Integer.parseInt(grams), Integer.parseInt(grams));
                    foodate.setId(new Random().nextInt());

                    mListFoodCustom.add(foodate);
                    foodAdapterCustom.setData(mListFoodCustom);
                    recycleviewYourFood.setAdapter(foodAdapterCustom);

                    FirebaseDatabase.getInstance().getReference().child("users")
                            .child(FirebaseAuth.getInstance().getUid())
                            .child("myfood")
                            .setValue(mListFoodCustom)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toasty.error(SearchFoodActivity.this, "something was fail", Toast.LENGTH_SHORT).show();
                        }
                    });



                    // add food locally here
                    dialogg.dismiss();
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
                dialogg.dismiss();
            }
        });
        ///////////////////////////////////

        //dialog.setCancelable(false);
        dialogg.show();
    }

    private void showCustomFoodDialog(int gravity, int position) {
        final Dialog dialogg = new Dialog(this);
        dialogg.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogg.setContentView(R.layout.dialog_customfood);

        Window window = dialogg.getWindow();
        if(window == null) return;

        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        windowAttributes.gravity = gravity;
        window.setAttributes(windowAttributes);

        EditText editText_gram = dialogg.findViewById(R.id.editText_gram_dialogCustomfood);
        EditText  editText_calories = dialogg.findViewById(R.id.textView_calories_dialogCustomfood);
        EditText editText_namefood = dialogg.findViewById(R.id.editText_namefood_dialogCustomfood);
        Button button_add = dialogg.findViewById(R.id.button_Add_dialogCustomfood);
        Button button_cancel = dialogg.findViewById(R.id.button_cancel_dialogCustomfood);
        /////////////////////////////////////////////

        int g = mListFoodCustom.get(position).getGram();
        float f = mListFoodCustom.get(position).getCalories();
        editText_gram.setText(String.valueOf(g));
        editText_calories.setText(String.valueOf((int) (f*g)));
        editText_namefood.setText(mListFoodCustom.get(position).getNameFood());

        String s = "SAVE";
        button_add.setText(s);
        button_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String grams = editText_gram.getText().toString();
                if(!grams.equals("")  && Integer.parseInt(grams)!= 0)
                {
                    float calori = Float.parseFloat(editText_calories.getText().toString());
                    String namef = editText_namefood.getText().toString();
                    if(calori <= 0 || namef.isEmpty()) return;

                    mListFoodCustom.get(position).setCalories(calori/Integer.parseInt(grams));
                    mListFoodCustom.get(position).setGram(Integer.parseInt(grams));
                    mListFoodCustom.get(position).setNameFood(namef);


                    FirebaseDatabase.getInstance().getReference().child("users")
                            .child(FirebaseAuth.getInstance().getUid())
                            .child("myfood")
                            .setValue(mListFoodCustom)
                            .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toasty.error(SearchFoodActivity.this, "something was fail", Toast.LENGTH_SHORT).show();
                        }
                    });

                    foodAdapterCustom.setData(mListFoodCustom);
                    recycleviewYourFood.setAdapter(foodAdapterCustom);


                    // add food locally here
                    dialogg.dismiss();
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
                dialogg.dismiss();
                foodAdapterCustom.setData(mListFoodCustom);
                recycleviewYourFood.setAdapter(foodAdapterCustom);
            }
        });
        ///////////////////////////////////

        dialogg.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                foodAdapterCustom.setData(mListFoodCustom);
                recycleviewYourFood.setAdapter(foodAdapterCustom);
            }
        });
        //dialog.setCancelable(false);
        dialogg.show();
    }

    public static void createFoodDatabase(@NonNull Context context)
    {
        if(!FoodDatabase.getInstance(context).foodDAO().isExistDatabaseFoods(150).isEmpty()) return;
        FoodDatabase.getInstance(context).foodDAO().insertFood(new FoodStatic("Banana", (float)134/150, 150));
        FoodDatabase.getInstance(context).foodDAO().insertFood(new FoodStatic("Apple", (float)95/182, 182));
        FoodDatabase.getInstance(context).foodDAO().insertFood(new FoodStatic("Orange", (float)86/184, 184));
        FoodDatabase.getInstance(context).foodDAO().insertFood(new FoodStatic("Guava", (float)37/55, 55));
        FoodDatabase.getInstance(context).foodDAO().insertFood(new FoodStatic("Avocado", (float)190/100, 100));
        FoodDatabase.getInstance(context).foodDAO().insertFood(new FoodStatic("Tomato", (float)18/100, 100));
        FoodDatabase.getInstance(context).foodDAO().insertFood(new FoodStatic("Mango", (float)85/120, 120));


        FoodDatabase.getInstance(context).foodDAO().insertFood(new FoodStatic("Chicken", (float)273/100, 100));
        FoodDatabase.getInstance(context).foodDAO().insertFood(new FoodStatic("Beef", (float)251/100, 100));
        FoodDatabase.getInstance(context).foodDAO().insertFood(new FoodStatic("Rice", (float)282/80, 80));
        FoodDatabase.getInstance(context).foodDAO().insertFood(new FoodStatic("Sandwich", (float)340/150, 150));
        FoodDatabase.getInstance(context).foodDAO().insertFood(new FoodStatic("Noodle Soup", (float)340/250, 250));
        FoodDatabase.getInstance(context).foodDAO().insertFood(new FoodStatic("Fish", (float)135/150, 150));
        FoodDatabase.getInstance(context).foodDAO().insertFood(new FoodStatic("Milk", (float)200/220, 220));
        FoodDatabase.getInstance(context).foodDAO().insertFood(new FoodStatic("Coffee", (float)94/220, 220));



    }



}