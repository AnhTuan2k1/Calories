package com.example.caloriesapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.Dialog;
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

import com.example.caloriesapp.activities.SearchFoodActivity;
import com.example.caloriesapp.database.FoodStatic;
import com.google.android.gms.tasks.OnCompleteListener;
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
import java.util.List;
import java.util.Locale;

import es.dmoral.toasty.Toasty;
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class A_Lunch extends AppCompatActivity {
    private RecyclerView recyclerView;
    private FoodateAdapter foodateAdapter;
    private List<Foodate> foodateList;
    private FloatingActionButton floatingActionButton;
    private Foodate deletedFood = null;
    private SwipeRefreshLayout swipeRefreshLayout;
    private TextView tieude;
    private String sessionofday_lunch;
    private String date_lunch;

    public static final String SESSIONOFDAY_LUNCH = "com.example.application.example.EXTRA_SESSIONOFDAY";
    public static final String DATE_LUNCH = "com.example.application.example.EXTRA_DATE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_a__lunch);
        AnhXa();

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Open_SearchFoodActivity();
            }
        });

        date_lunch = getcurrentday();


        String a = "AkaKiwi";
        int b = 2;
        float c = 123/22;

        FoodStatic food = new FoodStatic(a,c,b);
        food.setCalories(c);
        food.setGram(b);
        food.setNameFood(a);
        food.setId(2);

        foodateAdapter.setData(foodateList);
//        foodateList.add(food);
        syncDataWithFirebase(date_lunch);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                foodateList.clear();
//                foodateList.add(food);
                syncDataWithFirebase(date_lunch);
                foodateAdapter.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);

            }
        });


        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
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
                    foodateAdapter.notifyItemRemoved(positon);
                    Snackbar.make(recyclerView, deletedFood.getNameFood(), BaseTransientBottomBar.LENGTH_LONG)
                            .setAction("Undo", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    foodateList.add(positon, deletedFood);
                                    foodateAdapter.notifyItemInserted(positon);
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
                            sessionofday_lunch,
                            date_lunch);
                    foodateAdapter.notifyDataSetChanged();
                    break;
                default:
                    break;
            }
        }
        public void onChildDraw (Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive){

            new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                    .addSwipeLeftBackgroundColor(ContextCompat.getColor(A_Lunch.this, R.color.gray_item_nav))
                    .addSwipeLeftActionIcon(R.drawable.ic_baseline_delete_sweep_24)
                    .addSwipeRightBackgroundColor(ContextCompat.getColor(A_Lunch.this, R.color.gray_item_nav))
                    .addSwipeRightActionIcon(R.drawable.ic_baseline_edit_24)
                    .create()
                    .decorate();

            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
    };


    public void Open_SearchFoodActivity(){
        Intent intent = new Intent(this, SearchFoodActivity.class);
        intent.putExtra(SESSIONOFDAY_LUNCH,sessionofday_lunch);
        intent.putExtra(DATE_LUNCH,date_lunch);
        startActivity(intent);
    }
    public void AnhXa(){
        tieude = findViewById(R.id.tieudelunch);
        floatingActionButton = findViewById(R.id.lunch_fab);
        swipeRefreshLayout = findViewById(R.id.swiperefresh_lunch);
        recyclerView = findViewById(R.id.lunchlist);
        foodateList = new ArrayList<>();
        foodateAdapter = new FoodateAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(foodateAdapter);
        sessionofday_lunch = ((String) tieude.getText()).toLowerCase();
        date_lunch = null;

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this,DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);

    }
    private void syncDataWithFirebase(String date) {
        if(FirebaseAuth.getInstance().getCurrentUser() == null)
        {
            return;
        }
//        updateUI(date);
        updateFoodateList(date);
//        updateExercise(date);
//        updateCaloDaily(date);
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
                    if(foodate.getSessionofday().equals(sessionofday_lunch)){
                        Toasty.info(A_Lunch.this, foodate.getSessionofday(), Toasty.LENGTH_SHORT).show();
                        foodateList.add(foodate);
                    }
                }
                foodateAdapter.notifyDataSetChanged();

                // update ui here with foodateList
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toasty.info(A_Lunch.this, "Please Restart", Toasty.LENGTH_SHORT).show();
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
                                Toasty.success(A_Lunch.this, "Edit " + nameFood + " Successfully", Toast.LENGTH_SHORT).show();
                                foodateList.clear();
                                syncDataWithFirebase(date);
                                foodateAdapter.notifyDataSetChanged();
                            }
                            else{
                                Toasty.error(A_Lunch.this, "something was fail", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    // add food locally here


                    dialog.dismiss();
                }
                else
                {
                    Toasty.warning(A_Lunch.this, "gram has errors", Toast.LENGTH_SHORT).show();
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