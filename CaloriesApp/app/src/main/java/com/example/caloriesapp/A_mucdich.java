package com.example.caloriesapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class A_mucdich extends AppCompatActivity {
    private Button tangcan, giamcan, giucan;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_a_mucdich);


    giucan = (Button)findViewById(R.id.giucan);
    giucan.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
             openA_info_1();
        }
    });
    }
    public void openA_info_1(){
        Intent intent = new Intent(this,A_info_1.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
    }
}