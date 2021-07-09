package com.example.caloriesapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.time.LocalDate;
import java.time.Period;
import java.util.Calendar;

import es.dmoral.toasty.Toasty;

public class A_info_2 extends AppCompatActivity {
    private EditText Age;
    private Button buttonnext;
    public String mucdich_2;
    public String gioitinh_2;
    public Float tuoi;

    public static final String EXTRA_TEXTMUCDICH = "com.example.application.example.EXTRA_TEXTMUCDICH";
    public static final String EXTRA_TEXTGIOITINH = "com.example.application.example.EXTRA_TEXTGIOITINH";
    public static final String EXTRA_TEXTTUOI = "com.example.application.example.EXTRA_TEXTTUOI";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_a_info_2);
        Age = (EditText)findViewById(R.id.pick);
        buttonnext = (Button)findViewById(R.id.next2);
        buttonnext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Age.getText().toString().equals("")|| Integer.parseInt(Age.getText().toString())<=0|| Integer.parseInt(Age.getText().toString())>100){
                    Toasty.error(A_info_2.this,"Invalid Age",Toasty.LENGTH_SHORT).show();
                }
                else{
                    Animation animation= AnimationUtils.loadAnimation(A_info_2.this,R.anim.fadein);
                    buttonnext.startAnimation(animation);
                    tuoi = Float.parseFloat(Age.getText().toString());

                    OpenA_info_3();
                }
            }
        });

        Intent intent = getIntent();
        gioitinh_2 = intent.getStringExtra(A_info_1.EXTRA_TEXT1);
        mucdich_2 = intent.getStringExtra(A_info_1.EXTRA_TEXT);


//        Toast.makeText(A_info_2.this,gioitinh_2,Toast.LENGTH_LONG).show();




    }
    public void OpenA_info_3() {
        Intent intent = new Intent(this,A_info_3.class);
        intent.putExtra(EXTRA_TEXTMUCDICH,mucdich_2);
        intent.putExtra(EXTRA_TEXTGIOITINH,gioitinh_2);
        intent.putExtra(EXTRA_TEXTTUOI,tuoi);
        startActivity(intent);
    }
}