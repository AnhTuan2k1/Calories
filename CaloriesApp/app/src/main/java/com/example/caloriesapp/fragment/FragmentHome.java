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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import es.dmoral.toasty.Toasty;


public class FragmentHome extends Fragment {


    private List<Foodate> foodateList;
    private List<Exercise> exerciseList;
    private float caloDaily;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        foodateList = new ArrayList<>();
        exerciseList = new ArrayList<>();
        caloDaily = 0;
        syncDataWithFirebase("date"); // truyen vao ngay can update ui  "dd/MM/yyyy"

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


}