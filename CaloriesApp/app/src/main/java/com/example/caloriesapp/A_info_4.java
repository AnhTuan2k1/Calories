package com.example.caloriesapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import es.dmoral.toasty.Toasty;

public class A_info_4 extends AppCompatActivity {
    private Button button;
    private EditText goalweight,currentweight;
    private TextWatcher textWatcher = null;
    public String mucdich_4;
    public String gioitinh_4;
    public Float tuoi_4;
    public Float chieucao_4;
    public Float cannang,cannanggoal;



    public static final String EXTRA_TEXTMUCDICH = "com.example.application.example.EXTRA_TEXTMUCDICH";
    public static final String EXTRA_TEXTGIOITINH = "com.example.application.example.EXTRA_TEXTGIOITINH";
    public static final String EXTRA_TEXTTUOI = "com.example.application.example.EXTRA_TEXTTUOI";
    public static final String EXTRA_TEXTCHIEUCAO = "com.example.application.example.EXTRA_TEXTCHIEUCAO";
    public static final String EXTRA_TEXTCANNANG = "com.example.application.example.EXTRA_TEXTCANNANG";
    public static final String EXTRA_TEXTCANNANGGOAL = "com.example.application.example.EXTRA_TEXTCANNANGGOAL";
    private User user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_a_info_4);

        Intent intent = getIntent();
        mucdich_4 = intent.getStringExtra(A_info_3.EXTRA_TEXTMUCDICH);
        gioitinh_4 = intent.getStringExtra(A_info_3.EXTRA_TEXTGIOITINH);
        tuoi_4 = intent.getFloatExtra(A_info_3.EXTRA_TEXTTUOI, 0);
        chieucao_4 = intent.getFloatExtra(A_info_3.EXTRA_TEXTCHIEUCAO,0);
        goalweight =(EditText) findViewById(R.id.txtweight);
        currentweight =(EditText) findViewById(R.id.txtweight2);
        Checking(mucdich_4);

        button = (Button)findViewById(R.id.next4);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Checking2(mucdich_4);

            }
        });

        currentweight.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(mucdich_4.equals("Maintain Weight"))
                goalweight.setText(currentweight.getText().toString());
                return false;
            }
        });
        syncUserWithFirebase();
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

    public void Checking(String mucdich){

            if(mucdich == "Maintain Weight"){
                {
                    textWatcher = new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                            goalweight.setText(currentweight.getText().toString());
                        }

                        @Override
                        public void afterTextChanged(Editable s) {

                        }
                    };
                    currentweight.addTextChangedListener(textWatcher);
                }
        }
    }

    public void Checking2(String mucdich){
        switch (mucdich){
            case "Gain Weight":
                if(currentweight.getText().toString().matches("")
                        ||goalweight.getText().toString().matches("")
                        ||Float.parseFloat(currentweight.getText().toString()) <2
                        ||Float.parseFloat(goalweight.getText().toString()) <2
                        ||Float.parseFloat(currentweight.getText().toString()) >300
                        ||Float.parseFloat(goalweight.getText().toString()) >300
                        ||Float.parseFloat(goalweight.getText().toString()) <= Float.parseFloat(currentweight.getText().toString()))
                {
                    Toasty.warning(A_info_4.this, "Invalid Weight!!").show();
                }
                else{
                    Animation animation = AnimationUtils.loadAnimation(A_info_4.this,R.anim.fadein);
                    button.startAnimation(animation);
                    cannang = Float.parseFloat(currentweight.getText().toString());
                    cannanggoal = Float.parseFloat(goalweight.getText().toString());
                    OpenA_info_5();
                }
                break;
            case "Lose Weight":
                if(currentweight.getText().toString().matches("")
                        ||goalweight.getText().toString().matches("")
                        ||Float.parseFloat(currentweight.getText().toString()) <0
                        ||Float.parseFloat(goalweight.getText().toString()) <0
                        ||Float.parseFloat(currentweight.getText().toString()) >300
                        ||Float.parseFloat(goalweight.getText().toString()) >300
                        ||Float.parseFloat(goalweight.getText().toString()) >= Float.parseFloat(currentweight.getText().toString()))
                {
                    Toasty.warning(A_info_4.this, "Invalid Weight!!").show();
                }
                else{
                    Animation animation = AnimationUtils.loadAnimation(A_info_4.this,R.anim.fadein);
                    button.startAnimation(animation);
                    cannang = Float.parseFloat(currentweight.getText().toString());
                    cannanggoal = Float.parseFloat(goalweight.getText().toString());
                    OpenA_info_5();
                }
                break;
            case "Maintain Weight":
                if(currentweight.getText().toString().matches("")
                        /*
                        ||goalweight.getText().toString().matches("")
                         */
                        ||Float.parseFloat(currentweight.getText().toString()) <0
                        /*
                        ||Float.parseFloat(goalweight.getText().toString()) <0
                         */
                        ||Float.parseFloat(currentweight.getText().toString()) >300
                        /*
                        ||Float.parseFloat(goalweight.getText().toString()) >300
                         */
                        ||Float.parseFloat(currentweight.getText().toString())
                        != Float.parseFloat(goalweight.getText().toString())
                )
                {
                    Toasty.warning(A_info_4.this, "Invalid Weight!!").show();
                }
                else{
                    Animation animation = AnimationUtils.loadAnimation(A_info_4.this,R.anim.fadein);
                    button.startAnimation(animation);
                    cannang = Float.parseFloat(currentweight.getText().toString());
                    cannanggoal = Float.parseFloat(currentweight.getText().toString());
                    OpenA_info_5();
                }
                break;
        }
    }

    private void syncUserWithFirebase() {
        button.setEnabled(false);
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
                    if(user.getCurrentWeight() != 0f)
                    {
                        currentweight.setText(String.valueOf((int)user.getCurrentWeight()));
                        goalweight.setText(String.valueOf((int)user.getGoalWeight()));
                    }

                }catch (Exception e){}

                // update ui here with user
                button.setEnabled(true);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                if(getApplicationContext() != null)
                    Toasty.info(getApplicationContext(), error.getMessage(),Toasty.LENGTH_SHORT).show();

                button.setEnabled(true);
            }
        });
    }
}