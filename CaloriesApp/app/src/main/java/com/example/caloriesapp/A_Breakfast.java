package com.example.caloriesapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.Dialog;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.caloriesapp.activities.MainActivity;
import com.example.caloriesapp.activities.SearchExerciseActivity;
import com.example.caloriesapp.activities.SearchFoodActivity;
import com.example.caloriesapp.database.FoodStatic;
import com.example.caloriesapp.fragment.FragmentHome;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import es.dmoral.toasty.Toasty;
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class A_Breakfast extends AppCompatActivity {

    private RecyclerView recyclerView;
    private FoodateAdapter foodateAdapter;
    private List<Foodate> foodateList;
    private FloatingActionButton floatingActionButton;
    private Foodate deletedFood = null;
    private SwipeRefreshLayout swipeRefreshLayout;
    private TextView tieude,text_explain_2;
    private String sessionofday;
    private String date;
    private int total;

    public static final String SESSIONOFDAY_BREAKFAST = "com.example.application.example.EXTRA_SESSIONOFDAY";
    public static final String DATE_BREAKFAST = "com.example.application.example.EXTRA_DATE";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_a__breakfast);
        AnhXa();


        String a = "Kiwi";
        int b = 2;
        float c = 123/22;

        Foodate food = new Foodate(a,c,b,sessionofday,date);

        foodateAdapter.setData(foodateList);
//        foodateList.add(food);
        syncDataWithFirebase(date);


        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                foodateList.clear();
//                foodateList.add(food);

                foodateAdapter.notifyDataSetChanged();
                syncDataWithFirebase(date);
                swipeRefreshLayout.setRefreshing(false);

            }
        });

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Open_SearchFoodActivity();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

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
                    deletedFood = foodateList.get(positon);
                    foodateList.remove(positon);
                    FirebaseDatabase.getInstance().getReference().child("users")
                            .child(FirebaseAuth.getInstance().getUid())
                            .child("foodate")
                            .child(date)
                            .child(deletedFood.getId()).removeValue();
                    foodateAdapter.notifyItemRemoved(positon);
                    updateUI(date);
                    Snackbar.make(recyclerView, deletedFood.getNameFood(), BaseTransientBottomBar.LENGTH_LONG)
                            .setAction("Undo", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    foodateList.add(positon, deletedFood);
                                    foodateAdapter.notifyItemInserted(positon);

                                    FirebaseDatabase.getInstance().getReference().child("users")
                                            .child(FirebaseAuth.getInstance().getUid())
                                            .child("foodate")
                                            .child(date)
                                            .child(deletedFood.getId())
                                            .setValue(deletedFood)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    updateUI(date);
                                                }
                                            });
                                }
                            }).show();


                    break;
                case ItemTouchHelper.RIGHT:
                    deletedFood = foodateList.get(positon);
                    openDialog(Gravity.CENTER,
                            deletedFood.getId(),
                            deletedFood.getNameFood(),
                            deletedFood.getCalories(),
                            deletedFood.getGram(),
                            sessionofday,
                            date);
                    foodateList.clear();
                    updateUI(date);
                    foodateAdapter.notifyDataSetChanged();
                    break;
                default:
                    break;
            }
        }
        public void onChildDraw (Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,float dX, float dY,int actionState, boolean isCurrentlyActive){

            new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                    .addSwipeLeftBackgroundColor(ContextCompat.getColor(A_Breakfast.this, R.color.gray_item_nav))
                    .addSwipeLeftActionIcon(R.drawable.ic_baseline_delete_sweep_24)
                    .addSwipeRightBackgroundColor(ContextCompat.getColor(A_Breakfast.this, R.color.gray_item_nav))
                    .addSwipeRightActionIcon(R.drawable.ic_baseline_edit_24)
                    .create()
                    .decorate();

            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
    };

    public void Open_SearchFoodActivity() {
        Intent intent = new Intent(this, SearchFoodActivity.class);
        intent.putExtra(SESSIONOFDAY_BREAKFAST,sessionofday);
        intent.putExtra(DATE_BREAKFAST,date);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(A_Breakfast.this, MainActivity.class);
        intent.putExtra("date",date);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);

    }


    public void AnhXa(){

        tieude = findViewById(R.id.tieudebreakfast);
        floatingActionButton = findViewById(R.id.breakfast_fab);
        swipeRefreshLayout = findViewById(R.id.swiperefresh_breakfast);
        recyclerView = findViewById(R.id.breakfastlist);
        text_explain_2 = findViewById(R.id.text_explain2);
        foodateList = new ArrayList<>();
        foodateAdapter = new FoodateAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(foodateAdapter);
        sessionofday = ((String) tieude.getText()).toLowerCase();
        Intent intent = getIntent();
        date = intent.getStringExtra("key");
        sessionofday = intent.getStringExtra("session");
        tieude.setText(sessionofday.substring(0,1).toUpperCase() + sessionofday.substring(1));
        total = 0;

        }
    private void syncDataWithFirebase(String date) {
        if(FirebaseAuth.getInstance().getCurrentUser() == null)
        {
            return;
        }
        updateUI(date);
        updateFoodateList(date);
//        updateExercise(date);
//        updateCaloDaily(date);
    }
    private void updateUI(String date) {
        FirebaseDatabase.getInstance().getReference().child("users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("foodate")
                .child(date).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Foodate foodate = dataSnapshot.getValue(Foodate.class);
                    if(foodate.getSessionofday().equals(sessionofday)){
                        float a = (foodate.getGram()*foodate.getCalories());
//                            Toasty.info(A_Breakfast.this, String.valueOf(a), Toasty.LENGTH_SHORT).show();
                        total = total + Math.round(a);
                    }
                }


                text_explain_2.clearComposingText();
                text_explain_2.setText("Total Calories: ");
                text_explain_2.append(String.valueOf(total));
                total=0;
                // update ui here with foodateList
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toasty.info(A_Breakfast.this, "Please Restart", Toasty.LENGTH_SHORT).show();
            }
        });
    }

    private void updateFoodateList(String date) {
        FirebaseDatabase.getInstance().getReference().child("users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("foodate")
                .child(date).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Foodate foodate = dataSnapshot.getValue(Foodate.class);
                        if(foodate.getSessionofday().equals(sessionofday)){

                            foodateList.add(foodate);
                        }
                    }
                    foodateAdapter.notifyDataSetChanged();
                // update ui here with foodateList
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toasty.info(A_Breakfast.this, "Please Restart", Toasty.LENGTH_SHORT).show();
            }
        });
    }

    private String getcurrentday() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String date = sdf.format(calendar.getTime());
        return date;
    }
    private void openDialog(int gravity,final String id ,final String nameFood, final float calories, final float gram, String sessionofday, String date) {

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_editfoodate);

        Window window = dialog.getWindow();
        if(window == null) return;

        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        windowAttributes.gravity = gravity;
        window.setAttributes(windowAttributes);

        EditText editText_gram = dialog.findViewById(R.id.editText_gram_dialogEditfood);
        TextView textView_calories = dialog.findViewById(R.id.textView_calories_dialogEditfood);
        TextView textView_namefood = dialog.findViewById(R.id.textView_namefood_dialogEditfood);
        Button button_add = dialog.findViewById(R.id.button_Edit_dialogEditfood);
        Button button_cancel = dialog.findViewById(R.id.button_cancel_dialogEditfood);
        /////////////////////////////////////////////
        float cal = calories*gram;
        textView_calories.setText(String.valueOf(cal));
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
                    textView_calories.setText(String.valueOf((int)(Float.parseFloat(editText_gram.getText().toString())*calories)));
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

                    HashMap Food = new HashMap();
                    Food.put("nameFood",nameFood);
                    Food.put("calories",calories);
                    Food.put("gram",Integer.parseInt(grams));
//                    Foodate foodate = new Foodate(nameFood, cal, Integer.parseInt(grams), sessionofday, date);
                    FirebaseDatabase.getInstance().getReference().child("users")
                            .child(FirebaseAuth.getInstance().getUid())
                            .child("foodate")
                            .child(date)
                            .child(id)
                            .updateChildren(Food).addOnCompleteListener(new OnCompleteListener() {
                        @Override
                        public void onComplete(@NonNull Task task) {
                            if(task.isSuccessful()){
                                Toasty.success(A_Breakfast.this, "Edit " + nameFood + " Successfully", Toast.LENGTH_SHORT).show();
                                foodateList.clear();
                                syncDataWithFirebase(date);
                                foodateAdapter.notifyDataSetChanged();
                            }
                            else{
                                Toasty.error(A_Breakfast.this, "something was fail", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    // add food locally here


                    dialog.dismiss();
                }
                else
                {
                    Toasty.warning(A_Breakfast.this, "gram has errors", Toast.LENGTH_SHORT).show();
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


}