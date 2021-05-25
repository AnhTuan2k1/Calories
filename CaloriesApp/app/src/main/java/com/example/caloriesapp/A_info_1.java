package com.example.caloriesapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class A_info_1 extends AppCompatActivity {
    private Button next;
    private ImageView Ifemale, Imale;
    private Button female, male;
    private boolean isFemaleClicked = false;
    private boolean isMaleClicked = false;
    public String gioitinh;
    public String mucdich_1;

    public static final String EXTRA_TEXT1 = "com.example.application.example.EXTRA_TEXT1";
    public static final String EXTRA_TEXT = "com.example.application.example.EXTRA_TEXT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_a_info_1);


         Intent intent = getIntent();
         mucdich_1 = intent.getStringExtra(A_mucdich.EXTRA_TEXT);



        Ifemale = findViewById(R.id.imagefemale);
        Imale = findViewById(R.id.imagemale);
        male = findViewById(R.id.buttonmale);
        female = findViewById(R.id.buttonfemale);

        male.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isMaleClicked = true;
                isFemaleClicked = false;
                CheckMale(isMaleClicked);
                CheckFeMale(isFemaleClicked);
            }
        });


        female.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isMaleClicked = false;
                isFemaleClicked = true;
                CheckMale(isMaleClicked);
                CheckFeMale(isFemaleClicked);
            }
        });


//        Imale.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                isMaleClicked = true;
//                isFemaleClicked = false;
//                Check(isFemaleClicked,isMaleClicked);
//            }
//        });
//        Ifemale.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                isMaleClicked = false;
//                isFemaleClicked = true;
//                Check(isFemaleClicked,isMaleClicked);
//            }
//        });







        next = (Button)findViewById(R.id.next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               if((isFemaleClicked==false)&&(isMaleClicked==false)){

               }
               else {

                   // Pass to Main
                   if(isFemaleClicked==true){
                       gioitinh = female.getText().toString();
                   }
                   if(isMaleClicked==true){
                       gioitinh = male.getText().toString();
                   }

                    //
                   Animation animation = AnimationUtils.loadAnimation(A_info_1.this, R.anim.fadein);
                   next.startAnimation(animation);

                   OpenA_info_2();
               }
            }
        });
    }



    public void OpenA_info_2(){
        Intent intent = new Intent(this,A_info_2.class);
        intent.putExtra(EXTRA_TEXT1,gioitinh);
        intent.putExtra(EXTRA_TEXT,mucdich_1);
        startActivity(intent);
    }
    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
    }

    public void CheckMale(boolean a){

        if(a){
            male.setTextColor(Color.parseColor("#000000"));
        }
        else{
            male.setTextColor(Color.parseColor("#FFFFFF"));
        }
    }
    public void CheckFeMale(boolean a){

        if(a){
            female.setTextColor(Color.parseColor("#000000"));
        }
        else{
            female.setTextColor(Color.parseColor("#FFFFFF"));
        }
    }

}