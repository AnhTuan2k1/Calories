package com.example.caloriesapp.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.caloriesapp.CaloDaily;
import com.example.caloriesapp.Exercise;
import com.example.caloriesapp.Foodate;
import com.example.caloriesapp.R;
import com.example.caloriesapp.activities.LoginActivity;
import com.example.caloriesapp.activities.MainActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import es.dmoral.toasty.Toasty;


public class FragmentHome extends Fragment implements View.OnClickListener {
    boolean isClicked1,isClicked2,isClicked4,isClicked3;
    private ImageView imbottle1,imbottle2,imbottle3,imbottle4;
    private List<Foodate> foodateList;
    private List<Exercise> exerciseList;
    private float caloDaily;
    float water;
    private TextView watercount;

    private TextView date_home;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        foodateList = new ArrayList<>();
        exerciseList = new ArrayList<>();
        caloDaily = 0;
        syncDataWithFirebase("date"); // truyen vao ngay can update ui  "dd/MM/yyyy"
        imbottle1 = view.findViewById(R.id.bottle1);
        imbottle2 = view.findViewById(R.id.bottle2);
        imbottle3 = view.findViewById(R.id.bottle3);
        imbottle4 = view.findViewById(R.id.bottle4);
        watercount = view.findViewById(R.id.watercount);
        date_home = view.findViewById(R.id.home_date);




//        Calendar c = Calendar.getInstance();
//        int day = c.get(Calendar.DAY_OF_MONTH);

        Date date = new Date();
        LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        int year  = localDate.getYear();
        int month = localDate.getMonthValue();
        int day   = localDate.getDayOfMonth();

        date_home.setText(day + "thg" + month);

        isClicked1 = false;
        isClicked2 = false;
        isClicked3 = false;
        isClicked4 = false;
        water = 0;

        imbottle1.setOnClickListener(this);
        imbottle2.setOnClickListener(this);
        imbottle3.setOnClickListener(this);
        imbottle4.setOnClickListener(this);

        return view;
    }

    private void syncDataWithFirebase(String date) {
        updateFoodateList(date);
        updateExercise(date);
        updateCaloDaily(date);
    }

    private void updateFoodateList(String date) {
        FirebaseDatabase.getInstance().getReference().child("users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("foodate").child(date).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren())
                {
                    Foodate foodate = dataSnapshot.getValue(Foodate.class);
                    foodateList.add(foodate);
                }

                // update ui here with foodateList
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toasty.info(getContext(), "Please Restart",Toasty.LENGTH_SHORT).show();
            }
        });
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
                    exerciseList.add(exercise);
                }

                // update ui here with exerciseList
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toasty.info(getContext(), "Please Restart",Toasty.LENGTH_SHORT).show();
            }
        });
    }

    private void updateCaloDaily(String date) {
        FirebaseDatabase.getInstance().getReference().child("users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("calodaily").child(date).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.getValue(CaloDaily.class) != null)
                {
                    caloDaily = snapshot.getValue(CaloDaily.class).getCalories();

                    // update ui here with caloDaily
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toasty.info(getContext(), "Please Restart",Toasty.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bottle1:
                if(isClicked1==false){
                    imbottle1.setImageResource(R.drawable.icon_bottle_filled);
                    isClicked1 = true;
                    water +=0.5;
                    watercount.setText(String.valueOf(water)+ "/2L");
                }
                else {
                    imbottle1.setImageResource(R.drawable.icon_bottle_unfilled);
                    isClicked1 = false;
                    water-=0.5;
                    watercount.setText(String.valueOf(water)+ "/2L");
                }
                break;
            case R.id.bottle2:
                if(isClicked2==false){
                    imbottle2.setImageResource(R.drawable.icon_bottle_filled);
                    isClicked2 = true;
                    water +=0.5;
                    watercount.setText(String.valueOf(water)+ "/2L");
                }
                else {
                    imbottle2.setImageResource(R.drawable.icon_bottle_unfilled);
                    isClicked2 = false;
                    water-=0.5;
                    watercount.setText(String.valueOf(water)+ "/2L");
                }
                break;
            case R.id.bottle3:
                if(isClicked3==false){
                    imbottle3.setImageResource(R.drawable.icon_bottle_filled);
                    isClicked3 = true;
                    water +=0.5;
                    watercount.setText(String.valueOf(water)+ "/2L");
                }
                else {
                    imbottle3.setImageResource(R.drawable.icon_bottle_unfilled);
                    isClicked3 = false;
                    water-=0.5;
                    watercount.setText(String.valueOf(water)+ "/2L");
                }
                break;
            case R.id.bottle4:
                if(isClicked4==false){
                    imbottle4.setImageResource(R.drawable.icon_bottle_filled);
                    isClicked4 = true;
                    water +=0.5;
                    watercount.setText(String.valueOf(water)+ "/2L");
                }
                else {
                    imbottle4.setImageResource(R.drawable.icon_bottle_unfilled);
                    isClicked4 = false;
                    water-=0.5;
                    watercount.setText(String.valueOf(water)+ "/2L");
                }
                break;
        }
    }

}