package com.example.caloriesapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;

public class A_info_3 extends AppCompatActivity {
    private Button btnnext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_a_info_3);
        btnnext = (Button)findViewById(R.id.next3);
        btnnext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation animation= AnimationUtils.loadAnimation(A_info_3.this,R.anim.fadein);
                btnnext.startAnimation(animation);
                OpenA_info_4();
            }
        });

    }
    public void OpenA_info_4(){
        Intent intent = new Intent(this,A_info_4.class);
        startActivity(intent);
    }
}