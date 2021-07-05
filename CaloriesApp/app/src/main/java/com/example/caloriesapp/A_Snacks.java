package com.example.caloriesapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;
import android.view.View;

import com.example.caloriesapp.activities.SearchFoodActivity;
import com.example.caloriesapp.database.FoodStatic;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class A_Snacks extends AppCompatActivity {
    private RecyclerView recyclerView;
    private FoodAdapter foodAdapter;
    private List<FoodStatic> mListFood;
    private FloatingActionButton floatingActionButton;
    private FoodStatic deletedFood = null;
    private SwipeRefreshLayout swipeRefreshLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_a__snacks);
        AnhXa();

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Open_SearchFoodActivity();
            }
        });
        FoodStatic food2 = new FoodStatic("Coconut",40,100);
        FoodStatic food1= new FoodStatic("Coconut",40,100);
        FoodStatic food3 = new FoodStatic("Coconut",40,100);
        FoodStatic food4 = new FoodStatic("Coconut",40,100);

        mListFood.add(food1);
        mListFood.add(food2);
        mListFood.add(food3);
        mListFood.add(food4);



        foodAdapter.setData(mListFood);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mListFood.clear();
                mListFood.add(food1);
                mListFood.add(food2);
                mListFood.add(food3);
                mListFood.add(food2);
                mListFood.add(food1);
                foodAdapter.notifyDataSetChanged();
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
                    deletedFood = mListFood.get(positon);
                    mListFood.remove(positon);
                    foodAdapter.notifyItemRemoved(positon);
                    Snackbar.make(recyclerView, deletedFood.getNameFood(), BaseTransientBottomBar.LENGTH_LONG)
                            .setAction("Undo", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    mListFood.add(positon, deletedFood);
                                    foodAdapter.notifyItemInserted(positon);
                                }
                            }).show();


                    break;
                case ItemTouchHelper.RIGHT:

                    break;
                default:
                    break;
            }
        }
        public void onChildDraw (Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive){

            new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                    .addSwipeLeftBackgroundColor(ContextCompat.getColor(A_Snacks.this, R.color.gray_item_nav))
                    .addSwipeLeftActionIcon(R.drawable.ic_baseline_delete_sweep_24)
                    .addSwipeRightBackgroundColor(ContextCompat.getColor(A_Snacks.this, R.color.gray_item_nav))
                    .addSwipeRightActionIcon(R.drawable.ic_baseline_edit_24)
                    .create()
                    .decorate();

            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
    };


    private void Open_SearchFoodActivity() {
        Intent intent = new Intent(this, SearchFoodActivity.class);
        startActivity(intent);
    }

    public void AnhXa(){
        swipeRefreshLayout = findViewById(R.id.swiperefresh_dinner);
        floatingActionButton = findViewById(R.id.dinner_fab);
        recyclerView = findViewById(R.id.dinnerlist);
        mListFood = new ArrayList<>();
        foodAdapter = new FoodAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(foodAdapter);
    }
}