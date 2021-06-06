package com.example.caloriesapp.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        syncDataWithFirebase("date"); //  "dd/MM/yyyy"
        foodateList = new ArrayList<>();

        return view;
    }

    private void syncDataWithFirebase(String date) {
        if(FirebaseAuth.getInstance().getCurrentUser() == null)
        {
            return;
        }

        FirebaseDatabase.getInstance().getReference().child("users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("userdiary").child(date).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren())
                {
                    Foodate foodate = dataSnapshot.getValue(Foodate.class);
                    foodateList.add(foodate);
                    if(getContext() != null && foodate.getNameFood() != null)
                        Toasty.success(getContext(), foodate.getNameFood(),Toasty.LENGTH_SHORT).show();
                }

                // update ui here with foodateList
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toasty.success(getContext(), "Please Reload",Toasty.LENGTH_SHORT).show();
            }
        });
    }


}