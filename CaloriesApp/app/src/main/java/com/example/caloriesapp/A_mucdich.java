package com.example.caloriesapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.Toast;

import com.example.caloriesapp.activities.MainActivity;

public class A_mucdich extends AppCompatActivity {
    private Button tangcan, giamcan, giucan;
    private AnimationDrawable anim,anim2,anim3;
    public static final String SHARED_PREFS = "SharedPrefs";
    public static final String TEXT = "text";
    private String mucdich;

    public static final String EXTRA_TEXT = "com.example.application.example.EXTRA_TEXT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_a_mucdich);
        giucan = (Button)findViewById(R.id.giucan);
        tangcan = (Button)findViewById(R.id.tangcan);
        giamcan = (Button)findViewById(R.id.giamcan);

        //
       // anim = (AnimationDrawable)tangcan.getBackground();
       // anim.setEnterFadeDuration(2500);
       // anim.setExitFadeDuration(2500);
       // anim2 = (AnimationDrawable)giamcan.getBackground();
       // anim2.setEnterFadeDuration(2500);
       // anim2.setExitFadeDuration(2500);
       // anim3 = (AnimationDrawable)giucan.getBackground();
       // anim3.setEnterFadeDuration(2500);
        //anim3.setExitFadeDuration(2500);

        //

        tangcan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                saveData(tangcan);
                loadData();

                Animation animation=AnimationUtils.loadAnimation(A_mucdich.this,R.anim.fadein);
                tangcan.startAnimation(animation);

                openA_info_1();
            }
        });

        giamcan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveData(giamcan);
                loadData();

                Animation animation=AnimationUtils.loadAnimation(A_mucdich.this,R.anim.fadein);
                giamcan.startAnimation(animation);

                openA_info_1();
            }
        });

        giucan.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            saveData(giucan);

            Animation animation=AnimationUtils.loadAnimation(A_mucdich.this,R.anim.fadein);
            giucan.startAnimation(animation);
            loadData();


            openA_info_1();
        }
    });
    }
    public void openA_info_1(){
        Intent intent = new Intent(this,A_info_1.class);
        intent.putExtra(EXTRA_TEXT,mucdich);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
    }

    // save data
    public void saveData(Button button){
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS,MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(TEXT,button.getText().toString());
        editor.apply();;

    }

    public void loadData(){
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS,MODE_PRIVATE);
        mucdich = sharedPreferences.getString(TEXT,"");
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(anim!=null && !anim.isRunning()){
            anim.start();
        }
        if(anim2!=null && !anim2.isRunning()){
            anim2.start();
        }
        if(anim3!=null && !anim3.isRunning()){
            anim3.start();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(anim!=null && anim.isRunning()){
            anim.stop();
        }
        if(anim2!=null && anim2.isRunning()){
            anim2.stop();
        }
        if(anim3!=null && anim3.isRunning()){
            anim3.stop();
        }
    }
}