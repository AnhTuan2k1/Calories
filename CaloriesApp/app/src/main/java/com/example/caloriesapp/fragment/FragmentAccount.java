package com.example.caloriesapp.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.caloriesapp.Foodate;
import com.example.caloriesapp.R;
import com.example.caloriesapp.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import es.dmoral.toasty.Toasty;


public class FragmentAccount extends Fragment {

    private User user;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_account, container, false);
        syncUserWithFirebase();
        return view;
    }


    private void syncUserWithFirebase() {
        if(FirebaseAuth.getInstance().getCurrentUser() == null)
        {
            return;
        }

        FirebaseDatabase.getInstance().getReference().child("users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("userinfo").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                user = snapshot.getValue(User.class);

                // update ui here with user
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toasty.info(getContext(), "Please Restart",Toasty.LENGTH_SHORT).show();
            }
        });
    }
}