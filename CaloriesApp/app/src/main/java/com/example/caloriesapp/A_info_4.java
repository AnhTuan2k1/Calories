package com.example.caloriesapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.caloriesapp.activities.MainActivity;

public class A_info_4 extends AppCompatActivity {
    private Button button;
    private EditText weighttxt,weighttxt2;

    public String mucdich_4;
    public String gioitinh_4;
    public Float tuoi_4;
    public Float chieucao_4;
    public Float cannang;
    public Float cannanggoal;


    public static final String EXTRA_TEXTMUCDICH = "com.example.application.example.EXTRA_TEXTMUCDICH";
    public static final String EXTRA_TEXTGIOITINH = "com.example.application.example.EXTRA_TEXTGIOITINH";
    public static final String EXTRA_TEXTTUOI = "com.example.application.example.EXTRA_TEXTTUOI";
    public static final String EXTRA_TEXTCHIEUCAO = "com.example.application.example.EXTRA_TEXTCHIEUCAO";
    public static final String EXTRA_TEXTCANNANG = "com.example.application.example.EXTRA_TEXTCANNANG";
    public static final String EXTRA_TEXTCANNANGGOAL = "com.example.application.example.EXTRA_TEXTCANNANGGOAL";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_a_info_4);

        Intent intent = getIntent();
        mucdich_4 = intent.getStringExtra(A_info_3.EXTRA_TEXTMUCDICH);
        gioitinh_4 = intent.getStringExtra(A_info_3.EXTRA_TEXTGIOITINH);
        tuoi_4 = intent.getFloatExtra(A_info_3.EXTRA_TEXTTUOI, 0);
        chieucao_4 = intent.getFloatExtra(A_info_3.EXTRA_TEXTCHIEUCAO,0);

        weighttxt =(EditText) findViewById(R.id.txtweight);
        weighttxt2 = (EditText)findViewById(R.id.txtweight2);

        button = (Button)findViewById(R.id.next4);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(weighttxt.getText().toString().matches("")||weighttxt2.getText().toString().matches(""))
                {

                }
                else{
                    Animation animation = AnimationUtils.loadAnimation(A_info_4.this,R.anim.fadein);
                    button.startAnimation(animation);
                    cannang = Float.parseFloat(weighttxt.getText().toString());
                    cannanggoal = Float.parseFloat(weighttxt2.getText().toString());

                    OpenA_info_5();
                }

            }
        });
    }
    public void OpenA_info_5(){
        Intent intent = new Intent(A_info_4.this,A_info_5.class);
        intent.putExtra(EXTRA_TEXTMUCDICH,mucdich_4);
        intent.putExtra(EXTRA_TEXTGIOITINH,gioitinh_4);
        intent.putExtra(EXTRA_TEXTTUOI,tuoi_4);
        intent.putExtra(EXTRA_TEXTCHIEUCAO,chieucao_4);
        intent.putExtra(EXTRA_TEXTCANNANG,cannang);
        intent.putExtra(EXTRA_TEXTCANNANGGOAL,cannanggoal);
        startActivity(intent);
    }
}