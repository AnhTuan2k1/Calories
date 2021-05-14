package com.example.caloriesapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;

import com.example.caloriesapp.activities.MainActivity;

public class A_info_4 extends AppCompatActivity {
    private Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_a_info_4);
        button = (Button)findViewById(R.id.next4);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation animation = AnimationUtils.loadAnimation(A_info_4.this,R.anim.fadein);
                button.startAnimation(animation);
                OpenA_main();
            }
        });
    }
    public void OpenA_main(){
        Intent intent = new Intent(A_info_4.this, MainActivity.class);
        startActivity(intent);
    }
}