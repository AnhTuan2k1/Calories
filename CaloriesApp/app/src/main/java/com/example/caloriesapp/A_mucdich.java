package com.example.caloriesapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.Toast;

import com.example.caloriesapp.activities.MainActivity;

public class A_mucdich extends AppCompatActivity {
    private Button tangcan, giamcan, giucan;

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


}