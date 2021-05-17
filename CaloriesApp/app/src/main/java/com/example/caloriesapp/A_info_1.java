package com.example.caloriesapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class A_info_1 extends AppCompatActivity {
    private Button next;
    private ImageView Ifemale, Imale;
    private TextView female, male;
    private boolean isFemaleClicked = false;
    private boolean isMaleClicked = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_a_info_1);



        Ifemale = findViewById(R.id.imagefemale);
        Imale = findViewById(R.id.imagemale);
        male = findViewById(R.id.textviewmale);
        female = findViewById(R.id.textviewfemale);

        Imale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                isMaleClicked = true;
                isFemaleClicked = false;
                Check(isFemaleClicked,isMaleClicked);
            }
        });
        Ifemale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isMaleClicked = false;
                isFemaleClicked = true;
                Check(isFemaleClicked,isMaleClicked);
            }
        });







        next = (Button)findViewById(R.id.next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               if((isFemaleClicked==false)&&(isMaleClicked==false)){

               }
               else {
                   Animation animation = AnimationUtils.loadAnimation(A_info_1.this, R.anim.fadein);
                   next.startAnimation(animation);
                   OpenA_info_2();
               }
            }
        });
    }
    public void OpenA_info_2(){
        Intent intent = new Intent(this,A_info_2.class);
        startActivity(intent);
    }
    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
    }

    public void Check(boolean a,boolean b){

        if(a){
            female.setShadowLayer(1.2f,1.5f, 1.3f,Color.parseColor("#77754a"));
            female.setAllCaps(true);
            female.setTextColor(Color.parseColor("#000000"));
            female.setTextSize(TypedValue.COMPLEX_UNIT_SP, 22);

        }
        else{
            female.setShadowLayer(0,0, 0,Color.parseColor("#ffffff"));
            female.setAllCaps(false);
            female.setTextColor(Color.parseColor("#000000"));
            female.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        }
        if (b) {
            male.setShadowLayer(1.2f,1.5f, 1.3f,Color.parseColor("#77754a"));
            male.setAllCaps(true);
            male.setTextColor(Color.parseColor("#000000"));
            male.setTextSize(TypedValue.COMPLEX_UNIT_SP, 22);
        }
        else{
            male.setShadowLayer(0,0, 0,Color.parseColor("#FFFFFF"));
            male.setAllCaps(false);
            male.setTextColor(Color.parseColor("#000000"));
            male.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        }
    }

}