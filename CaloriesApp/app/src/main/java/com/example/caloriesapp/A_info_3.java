package com.example.caloriesapp;

import androidx.annotation.NonNull;
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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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
    private User user;

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

        syncUserWithFirebase();
    }
    public void OpenA_info_4(){
        Intent intent = new Intent(this,A_info_4.class);
        intent.putExtra(EXTRA_TEXTMUCDICH,mucdich_3);
        intent.putExtra(EXTRA_TEXTGIOITINH,gioitinh_3);
        intent.putExtra(EXTRA_TEXTTUOI,tuoi_3);
        intent.putExtra(EXTRA_TEXTCHIEUCAO,chieucao);
        startActivity(intent);
        finish();
    }
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this,A_info_2.class);
        intent.putExtra(A_info_1.EXTRA_TEXT1,gioitinh_3);
        intent.putExtra(A_info_1.EXTRA_TEXT,gioitinh_3);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
        finish();
    }
    private void syncUserWithFirebase() {
        btnnext.setEnabled(false);
        if(FirebaseAuth.getInstance().getCurrentUser() == null)
        {
            return;
        }

        FirebaseDatabase.getInstance().getReference().child("users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("userinfo").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                user = snapshot.getValue(User.class);

                try {
                    if(user.getHeight() != 0f)
                    heighttxt.setText(String.valueOf(user.getHeight()));
                }catch (Exception e){}

                // update ui here with user
                btnnext.setEnabled(true);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                if(getApplicationContext() != null)
                    Toasty.info(getApplicationContext(), error.getMessage(),Toasty.LENGTH_SHORT).show();

                btnnext.setEnabled(true);
            }
        });
    }
}