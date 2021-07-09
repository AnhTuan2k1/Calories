package com.example.caloriesapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import es.dmoral.toasty.Toasty;

public class A_info_3 extends AppCompatActivity {
    private Button btnnext;
    private EditText heighttxt;
    public String mucdich_3;
    public String gioitinh_3;
    public Float tuoi_3;
    public Float chieucao;

    public static final String EXTRA_TEXTMUCDICH = "com.example.application.example.EXTRA_TEXTMUCDICH";
    public static final String EXTRA_TEXTGIOITINH = "com.example.application.example.EXTRA_TEXTGIOITINH";
    public static final String EXTRA_TEXTTUOI = "com.example.application.example.EXTRA_TEXTTUOI";
    public static final String EXTRA_TEXTCHIEUCAO = "com.example.application.example.EXTRA_TEXTCHIEUCAO";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_a_info_3);

        Intent intent = getIntent();
        mucdich_3 = intent.getStringExtra(A_info_2.EXTRA_TEXTMUCDICH);
        gioitinh_3 = intent.getStringExtra(A_info_2.EXTRA_TEXTGIOITINH);
        tuoi_3 = intent.getFloatExtra(A_info_2.EXTRA_TEXTTUOI, 0);


        heighttxt = findViewById(R.id.txtheight);


        btnnext = (Button)findViewById(R.id.next3);
        btnnext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chieucao = Float.parseFloat(heighttxt.getText().toString());
                if(heighttxt.getText().toString().equals("")||chieucao<30||chieucao>250){
                    Toasty.error(A_info_3.this,"Invalid Height",Toasty.LENGTH_SHORT).show();
                }
                else{
                    Animation animation= AnimationUtils.loadAnimation(A_info_3.this,R.anim.fadein);
                    btnnext.startAnimation(animation);
                    OpenA_info_4();
                }
            }
        });

    }
    public void OpenA_info_4(){
        Intent intent = new Intent(this,A_info_4.class);
        intent.putExtra(EXTRA_TEXTMUCDICH,mucdich_3);
        intent.putExtra(EXTRA_TEXTGIOITINH,gioitinh_3);
        intent.putExtra(EXTRA_TEXTTUOI,tuoi_3);
        intent.putExtra(EXTRA_TEXTCHIEUCAO,chieucao);
        startActivity(intent);
    }
}