package com.example.caloriesapp.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.caloriesapp.A_info_5;
import com.example.caloriesapp.A_mucdich;
import com.example.caloriesapp.R;
import com.example.caloriesapp.User;
import com.example.caloriesapp.database.FoodDatabase;
import com.example.caloriesapp.database.FoodStatic;
import com.example.caloriesapp.fragment.FragmentAccount;
import com.example.caloriesapp.fragment.FragmentHome;
import com.example.caloriesapp.fragment.FragmentStatistic;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    private static final int FRAGMENT_HOME = 1;
    private static final int FRAGMENT_STATISTIC = 2;
    private static final int FRAGMENT_ACCOUNT = 3;

   private Button bt1;

    private Double cm,kg,age;
    private Double heso;

    //-------------
//    public String mucdichmain;
//    public String gioitinhmain;
//    public Double tuoimain;
//    public Double chieucaomain;
//    public Double cannangmain;
//    public Double AMmain;
//    Button btn;
    //-------------

    //private User user;
    private int current_Fragment = FRAGMENT_HOME;
    private BottomNavigationView navigationView;
    FirebaseAuth firebaseAuth;
    DatabaseReference myDatabase;
    FirebaseUser firebaseUser;
    float calories = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SearchFoodActivity.createFoodDatabase(this);
        anhxa();
        checkInfoUser();
        addFragment(new FragmentHome());



        //-------------

        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        boolean firstStart = prefs.getBoolean("firstStart",true);


        //============

        //-------------

//        Intent intent = getIntent();
//        chieucaomain = intent.getDoubleExtra(A_info_5.EXTRA_TEXTCHIEUCAO,0);
//        cannangmain = intent.getDoubleExtra(A_info_5.EXTRA_TEXTCANNANG,0);
//        tuoimain = intent.getDoubleExtra(A_info_5.EXTRA_TEXTTUOI,0);
//        cm = chieucaomain;
//        kg = cannangmain;
//        age = tuoimain;

        



        //============



        navigationView = findViewById(R.id.bottom_nav);
        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId())
                {
                    case R.id.action_account:
                        openAccountFragment();
                        setTitle("Account");
                        break;
                    case R.id.action_home:
                        openHomeFragment();
                        setTitle("Home");
                        break;
                    case R.id.action_statistic:
                        openStatisticFragment();
                        setTitle("Statistic");
                        break;
                }

                return true;
            }
        });


    }

    private void anhxa() {

        myDatabase = FirebaseDatabase.getInstance().getReference();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    }

    private void openHomeFragment()
    {
        if(current_Fragment != FRAGMENT_HOME)
        {
            replaceFragment(new FragmentHome());
            current_Fragment = FRAGMENT_HOME;

        }
    }
    private void openStatisticFragment()
    {
        if(current_Fragment != FRAGMENT_STATISTIC)
        {
            replaceFragment(new FragmentStatistic());
            current_Fragment = FRAGMENT_STATISTIC;
        }
    }
    private void openAccountFragment()
    {
        if(current_Fragment != FRAGMENT_ACCOUNT)
        {
            replaceFragment(new FragmentAccount());
            current_Fragment = FRAGMENT_ACCOUNT;
        }
    }

    private void replaceFragment(Fragment fragment)
    {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame_mainActivity,fragment);
        fragmentTransaction.commit();
    }
    private void addFragment(Fragment fragment)
    {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.frame_mainActivity,fragment);
        fragmentTransaction.commit();
    }
    private void FirstRun(){
        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("firstStart",false);
        editor.apply();
        Intent intent = new Intent(MainActivity.this, A_mucdich.class);
        startActivity(intent);
    }

    private void checkInfoUser()
    {
        if(firebaseUser == null)
        {
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
            finish();
        }
        myDatabase.child("users").child(firebaseUser.getUid()).child("userinfo")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        if(snapshot.child("dailyCaloriesTarget").getValue(float.class) == null){
                            myDatabase.child("users").child(FirebaseAuth.getInstance().getUid())
                                    .child("userinfo").setValue(new User("9","8","7",6,
                                    5,4,3,2,0, firebaseUser.getEmail()));

                            startActivity(new Intent(getApplicationContext(), A_mucdich.class));
                            finish();
                            return;
                        }

                        calories = snapshot.child("dailyCaloriesTarget").getValue(float.class);

                        if(calories == 0f)
                        {
                            MaterialAlertDialogBuilder materialAlertDialogBuilder = new MaterialAlertDialogBuilder(MainActivity.this);
                            materialAlertDialogBuilder.setMessage("to use this app, please update your profile").setCancelable(false)
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            startActivity(new Intent(getApplicationContext(), A_mucdich.class));
                                            finish();
                                        }
                                    }).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

}