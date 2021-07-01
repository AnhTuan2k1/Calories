    package com.example.caloriesapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.caloriesapp.activities.MainActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

    public class A_info_5 extends AppCompatActivity {


    Button R1,R2,R3,R4;

    public String mucdich_5;
    public String gioitinh_5;
    public Float tuoi_5,chieucao_5,cannang_5,cannanggoal_5;
    public Float AM;

    private Float BMR;
    private Float TDEE;
    private Float Calo;
    private Float gap;
    private Float time;

//    public static final String EXTRA_TEXTMUCDICH = "com.example.application.example.EXTRA_TEXTMUCDICH";
//    public static final String EXTRA_TEXTGIOITINH = "com.example.application.example.EXTRA_TEXTGIOITINH";
//    public static final String EXTRA_TEXTTUOI = "com.example.application.example.EXTRA_TEXTTUOI";
//    public static final String EXTRA_TEXTCHIEUCAO = "com.example.application.example.EXTRA_TEXTCHIEUCAO";
//    public static final String EXTRA_TEXTCANNANG = "com.example.application.example.EXTRA_TEXTCANNANG";
//    public static final String EXTRA_TEXTAM= "com.example.application.example.EXTRA_TEXTAM";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_a_info_5);
        R1 = findViewById(R.id.R1);
        R2 = findViewById(R.id.R2);
        R3 = findViewById(R.id.R3);
        R4 = findViewById(R.id.R4);

        Intent intent = getIntent();
        mucdich_5 = intent.getStringExtra(A_info_4.EXTRA_TEXTMUCDICH);
        gioitinh_5 = intent.getStringExtra(A_info_4.EXTRA_TEXTGIOITINH);
        tuoi_5 = intent.getFloatExtra(A_info_4.EXTRA_TEXTTUOI, 0);
        chieucao_5 = intent.getFloatExtra(A_info_4.EXTRA_TEXTCHIEUCAO,0);
        cannang_5 = intent.getFloatExtra(A_info_4.EXTRA_TEXTCANNANG,0);
        cannanggoal_5 = intent.getFloatExtra(A_info_4.EXTRA_TEXTCANNANGGOAL,0);



        TextView txt = findViewById(R.id.txt111);
        txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });



        R1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation animation = AnimationUtils.loadAnimation(A_info_5.this,R.anim.fadein);
                R1.startAnimation(animation);
                AM = 1.2f;

                TinhCalo_BMR(gioitinh_5);
                TinhCalo_TDEE(BMR,AM);
                TinhCalo_Mucdich(mucdich_5);
                Toast.makeText(A_info_5.this,"Thanh cong ne thang lon", Toast.LENGTH_LONG).show();
//                OpenA_Main();
            }
        });
        R2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation animation = AnimationUtils.loadAnimation(A_info_5.this,R.anim.fadein);
                R2.startAnimation(animation);
                AM = 1.375f;
                TinhCalo_BMR(gioitinh_5);
                TinhCalo_TDEE(BMR,AM);
                TinhCalo_Mucdich(mucdich_5);

                OpenA_Main();
            }
        });
        R3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation animation = AnimationUtils.loadAnimation(A_info_5.this,R.anim.fadein);
                R3.startAnimation(animation);
                AM = 1.55f;
                TinhCalo_BMR(gioitinh_5);
                TinhCalo_TDEE(BMR,AM);
                TinhCalo_Mucdich(mucdich_5);

                OpenA_Main();
            }
        });
        R4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation animation = AnimationUtils.loadAnimation(A_info_5.this,R.anim.fadein);
                R4.startAnimation(animation);
                AM = 1.725f;
                TinhCalo_BMR(gioitinh_5);
                TinhCalo_TDEE(BMR,AM);
                TinhCalo_Mucdich(mucdich_5);

                OpenA_Main();
            }
        });
    }
    public void OpenA_Main(){
        Intent intent = new Intent(A_info_5.this, MainActivity.class);
//        intent.putExtra(EXTRA_TEXTMUCDICH,mucdich_5);
//        intent.putExtra(EXTRA_TEXTGIOITINH,gioitinh_5);
//        intent.putExtra(EXTRA_TEXTTUOI,tuoi_5);
//        intent.putExtra(EXTRA_TEXTCHIEUCAO,chieucao_5);
//        intent.putExtra(EXTRA_TEXTCANNANG,cannang_5);
//        intent.putExtra(EXTRA_TEXTAM,AM);

        String s = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
        if(s == null) s = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        User user = new User(mucdich_5,gioitinh_5,time.toString(),cannanggoal_5,cannang_5,Math.round(tuoi_5),chieucao_5,AM,Calo, s);
        FirebaseDatabase.getInstance().getReference()
                .child("users").child(FirebaseAuth.getInstance().getUid())
                .child("userinfo").setValue(user);

        startActivity(intent);

    }


    public void TinhCalo_BMR(String gioitinh){
        if(gioitinh=="Male"){
            BMR = (chieucao_5*6.25f) + (cannang_5*10) - (tuoi_5*5) + 5;
        }
        else{
            BMR = (chieucao_5*6.25f) + (cannang_5*10) - (tuoi_5*5) - 161;
        }
    }

    public void TinhCalo_TDEE(Float chisoBMR, Float chisoAM){
        TDEE = chisoBMR*chisoAM;
    }

    public void TinhCalo_Mucdich(String mucdich){
        switch (mucdich){
            case "Lose Weight":
                Calo = TDEE - 500;
                gap = cannang_5 - cannanggoal_5;
                time = gap / 0.5f;
                break;
            case "Gain Weight":
                Calo = TDEE + 500;
                gap = cannanggoal_5 - cannang_5;
                time = gap / 0.5f;
                break;
            case "Maintain Weight":
                Calo = TDEE;
        }
    }

}