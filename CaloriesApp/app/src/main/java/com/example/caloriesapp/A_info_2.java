package com.example.caloriesapp;

import androidx.annotation.NonNull;
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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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
    private User user;


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



        syncUserWithFirebase();
    }
    public void OpenA_info_3() {
        Intent intent = new Intent(this,A_info_3.class);
        intent.putExtra(EXTRA_TEXTMUCDICH,mucdich_2);
        intent.putExtra(EXTRA_TEXTGIOITINH,gioitinh_2);
        intent.putExtra(EXTRA_TEXTTUOI,tuoi);
        startActivity(intent);
    }

    private void syncUserWithFirebase() {
        buttonnext.setEnabled(false);
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
                    Age.setText(String.valueOf(user.getAge()));
                }catch (Exception e){}

                // update ui here with user
                buttonnext.setEnabled(true);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                if(getApplicationContext() != null)
                    Toasty.info(getApplicationContext(), error.getMessage(),Toasty.LENGTH_SHORT).show();

                buttonnext.setEnabled(true);
            }
        });
    }
}