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

import com.example.caloriesapp.activities.MainActivity;
import com.example.caloriesapp.activities.SearchExerciseActivity;
import com.example.caloriesapp.activities.SearchFoodActivity;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import es.dmoral.toasty.Toasty;
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class A_Excercise extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ExerciseAdapter exerciseAdapter;
    private List<Exercise> exerciseList;
    private FloatingActionButton floatingActionButton;
    private Exercise deletedExcercise = null;
    private SwipeRefreshLayout swipeRefreshLayout;
    private TextView tieude,text_explain_3;
    private String date;
    private int total;

    public static final String DATE_EXERCISE = "com.example.application.example.EXTRA_DATE_EXERCISE";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_a__excercise);
        AnhXa();
        exerciseAdapter.setData(exerciseList);
        syncDataWithFirebase(date);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                exerciseList.clear();

                exerciseAdapter.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);
                syncDataWithFirebase(date);
            }
        });
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Open_SearchExerciseActivity();
            }
        });
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
                    deletedExcercise = exerciseList.get(positon);
                    exerciseList.remove(positon);
                    FirebaseDatabase.getInstance().getReference().child("users")
                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .child("exercise").child(date).child(deletedExcercise.getId()).removeValue();
                    exerciseAdapter.notifyItemRemoved(positon);
                    updateUI(date);
                    Snackbar.make(recyclerView, deletedExcercise.getNameExercise(), BaseTransientBottomBar.LENGTH_LONG)
                            .setAction("Undo", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    exerciseList.add(positon, deletedExcercise);
                                    exerciseAdapter.notifyItemInserted(positon);
                                    FirebaseDatabase.getInstance().getReference().child("users")
                                            .child(FirebaseAuth.getInstance().getUid())
                                            .child("exercise")
                                            .child(date)
                                            .child(deletedExcercise.getId())
                                            .setValue(deletedExcercise)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    updateUI(date);
                                                }
                                            });

                                }
                            }).show();


                    break;
                case ItemTouchHelper.RIGHT:
                    deletedExcercise = exerciseList.get(positon);
                    openDialog(Gravity.CENTER,
                            deletedExcercise.getId(),
                            deletedExcercise,
                            date
                            );
                    exerciseAdapter.notifyDataSetChanged();
                    updateUI(date);
                    break;
                default:
                    break;
            }
        }
        public void onChildDraw (Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive){

            new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                    .addSwipeLeftBackgroundColor(ContextCompat.getColor(A_Excercise.this, R.color.gray_item_nav))
                    .addSwipeLeftActionIcon(R.drawable.ic_baseline_delete_sweep_24)
                    .addSwipeRightBackgroundColor(ContextCompat.getColor(A_Excercise.this, R.color.gray_item_nav))
                    .addSwipeRightActionIcon(R.drawable.ic_baseline_edit_24)
                    .create()
                    .decorate();

            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
    };
    private void Open_SearchExerciseActivity() {
        Intent intent = new Intent(this, SearchExerciseActivity.class);
        intent.putExtra(DATE_EXERCISE,date);
        startActivity(intent);
    }
    private void AnhXa() {
        tieude = findViewById(R.id.tieudeexcercise);
        text_explain_3 = findViewById(R.id.text_explain3);
        floatingActionButton = findViewById(R.id.excercise_fab);
        swipeRefreshLayout = findViewById(R.id.swiperefresh_excercise);
        recyclerView = findViewById(R.id.excerciselist);
        exerciseList = new ArrayList<>();
        exerciseAdapter = new ExerciseAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(exerciseAdapter);
        Intent intent = getIntent();
        date = intent.getStringExtra("key");

    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent = new Intent(A_Excercise.this, MainActivity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);

    }

    private void updateExercise(String date) {
        FirebaseDatabase.getInstance().getReference().child("users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("exercise").child(date).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren())
                {
                    Exercise exercise = dataSnapshot.getValue(Exercise.class);
//                        Toasty.info(A_Excercise.this, exercise.getSessionofday(), Toasty.LENGTH_SHORT).show();

                    exerciseList.add(exercise);
                }
                    exerciseAdapter.notifyDataSetChanged();

                // update ui here with exerciseList
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toasty.info(A_Excercise.this, "Please Restart",Toasty.LENGTH_SHORT).show();
            }
        });
    }
    private void updateUI(String date) {
        FirebaseDatabase.getInstance().getReference().child("users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("exercise").child(date).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren())
                {
                    Exercise exercise = dataSnapshot.getValue(Exercise.class);
//                        Toasty.info(A_Excercise.this, exercise.getSessionofday(), Toasty.LENGTH_SHORT).show();
                    int a = Math.round(exercise.getCalories()*exercise.getDuration());
                    total = total + a;

                }

                text_explain_3.clearComposingText();
                text_explain_3.setText("Total Calories: ");
                text_explain_3.append(String.valueOf(total));
                total=0;

                // update ui here with exerciseList
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toasty.info(A_Excercise.this, "Please Restart",Toasty.LENGTH_SHORT).show();
            }
        });
    }
    private void syncDataWithFirebase(String date) {
        if(FirebaseAuth.getInstance().getCurrentUser() == null)
        {
            return;
        }
        updateUI(date);
//        updateFoodateList(date);
        updateExercise(date);
//        updateCaloDaily(date);
    }
    private void openDialog(int gravity,final String id, final Exercise exercise, final String date) {

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_editexercise);
        Window window = dialog.getWindow();
        if(window == null) return;

        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        windowAttributes.gravity = gravity;
        window.setAttributes(windowAttributes);

        EditText duration = dialog.findViewById(R.id.editText_duration_dialogEditexercise);
        TextView textView_calories = dialog.findViewById(R.id.textView_calories_dialogEditexercise);
        TextView textView_nameExercise = dialog.findViewById(R.id.textView_nameexercise_dialogEditexercise);
        Button button_add = dialog.findViewById(R.id.button_Edit_dialogEditexercise);
        Button button_cancel = dialog.findViewById(R.id.button_cancel_dialogEditexercise);
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
                    HashMap EXER = new HashMap();
                    EXER.put("nameExercise",exercise2.getNameExercise());
                    EXER.put("calories",exercise2.getCalories());
                    EXER.put("duration",exercise2.getDuration());


                    FirebaseDatabase.getInstance().getReference().child("users")
                            .child(FirebaseAuth.getInstance().getUid())
                            .child("exercise")
                            .child(date)
                            .child(id)
                            .updateChildren(EXER).addOnCompleteListener(new OnCompleteListener() {
                        @Override
                        public void onComplete(@NonNull Task task) {
                            Toasty.success(A_Excercise.this, "Edit exercise Successfully", Toast.LENGTH_SHORT).show();
                            exerciseList.clear();
                            if(task.isSuccessful()){
                                exerciseList.clear();
                                syncDataWithFirebase(date);
                                exerciseAdapter.notifyDataSetChanged();
                            }
                            else{
                                Toasty.error(A_Excercise.this, "something was fail", Toast.LENGTH_SHORT).show();
                            }
                        }

                    });



                    // add food locally here
                    dialog.dismiss();
                }
                else
                {
                    Toasty.warning(A_Excercise.this, "duration has errors", Toast.LENGTH_SHORT).show();
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