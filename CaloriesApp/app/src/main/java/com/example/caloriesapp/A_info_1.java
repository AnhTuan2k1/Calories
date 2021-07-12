package com.example.caloriesapp;

import androidx.annotation.NonNull;
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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import es.dmoral.toasty.Toasty;

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
    private User user;

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
                   Toasty.warning(A_info_1.this,"Please pick a gender!!!",Toasty.LENGTH_SHORT).show();
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
        syncUserWithFirebase();
    }



    public void OpenA_info_2(){
        Intent intent = new Intent(this,A_info_2.class);
        intent.putExtra(EXTRA_TEXT1,gioitinh);
        intent.putExtra(EXTRA_TEXT,mucdich_1);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this,A_mucdich.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
        finish();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
    }

    public void CheckMale(boolean a){

        if(a){
            male.setBackgroundColor(Color.parseColor("#0277BD"));
            male.setTextColor(Color.parseColor("#000000"));
        }
        else{
            male.setBackgroundColor(Color.parseColor("#03A9F4"));
            male.setTextColor(Color.parseColor("#FFFFFF"));
        }
    }
    public void CheckFeMale(boolean a){

        if(a){
            female.setBackgroundColor(Color.parseColor("#0277BD"));
            female.setTextColor(Color.parseColor("#000000"));
        }
        else{
            female.setBackgroundColor(Color.parseColor("#03A9F4"));
            female.setTextColor(Color.parseColor("#FFFFFF"));
        }
    }

    private void syncUserWithFirebase() {
        next.setEnabled(false);
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
                    switch (user.getGender())
                    {
                        case "Male":
                            male.callOnClick();
                            break;
                        case "Female":
                            female.callOnClick();
                            break;
                    }

                }catch (Exception e){}

                // update ui here with user
                next.setEnabled(true);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                if(getApplicationContext() != null)
                    Toasty.info(getApplicationContext(), error.getMessage(),Toasty.LENGTH_SHORT).show();

                next.setEnabled(true);
            }
        });
    }

}