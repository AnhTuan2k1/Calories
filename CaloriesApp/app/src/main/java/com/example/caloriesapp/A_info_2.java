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
import android.widget.TextView;

import java.util.Calendar;

public class A_info_2 extends AppCompatActivity {
    private TextView textwhatisyourdateofbirth;
    private Button pick,next;
    DatePickerDialog.OnDateSetListener setListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_a_info_2);
        pick =(Button)findViewById(R.id.pick);
        textwhatisyourdateofbirth = findViewById(R.id.textwhatisyourdateofbirth);
        next=(Button)findViewById(R.id.next2);
        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(calendar.YEAR);
        final int month = calendar.get(calendar.MONTH);
        final int day = calendar.get(calendar.DAY_OF_MONTH);


        pick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        A_info_2.this,android.R.style.Theme_Holo_Dialog_MinWidth,
                        setListener,year,month,day);
                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                datePickerDialog.show();
            }
        });
        setListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month +=1;
                String date = day + "/" + month + "/" + year;
                textwhatisyourdateofbirth.setText(date);
            }
        };
        textwhatisyourdateofbirth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        A_info_2.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {
                        month += 1;
                        String date = day + "/" + month + "/"+ year;
                        textwhatisyourdateofbirth.setText(date);
                    }
                },year,month,day);
                datePickerDialog.show();

            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation animation= AnimationUtils.loadAnimation(A_info_2.this,R.anim.fadein);
                next.startAnimation(animation);
                OpenA_info_3();
            }
        });


    }
    private void OpenA_info_3() {
        Intent intent = new Intent(this,A_info_2.class);
        startActivity(intent);
    }
}