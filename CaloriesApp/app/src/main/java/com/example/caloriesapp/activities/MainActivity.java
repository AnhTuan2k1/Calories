package com.example.caloriesapp.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.airbnb.lottie.LottieAnimationView;
import com.example.caloriesapp.A_mucdich;
import com.example.caloriesapp.CaloDaily;
import com.example.caloriesapp.R;
import com.example.caloriesapp.User;
import com.example.caloriesapp.fragment.FragmentAccount;
import com.example.caloriesapp.fragment.FragmentHome;
import com.example.caloriesapp.fragment.FragmentStatistic;
import com.example.caloriesapp.viewmodel.MainActivityViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import dev.shreyaspatil.MaterialDialog.MaterialDialog;

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
    private int current_Fragment;
    private MainActivityViewModel viewmodel;
    private BottomNavigationView navigationView;
    LottieAnimationView lottieAnimationView;
    FirebaseAuth firebaseAuth;
    DatabaseReference myDatabase;
    FirebaseUser firebaseUser;
    float calories = 0;

    Fragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //SearchFoodActivity.createFoodDatabase(this);
        anhxa();
        checkInfoUser();

        if(current_Fragment == 0)
            openHomeFragment();



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
        lottieAnimationView = findViewById(R.id.LottieAnimationView_Main);
        myDatabase = FirebaseDatabase.getInstance().getReference();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        viewmodel = new ViewModelProvider(this,
                new ViewModelProvider.AndroidViewModelFactory(getApplication())).get(MainActivityViewModel.class);
        current_Fragment = viewmodel.currentFragment;
    }

    private void openHomeFragment()
    {
        if(current_Fragment != FRAGMENT_HOME)
        {
            fragment = new FragmentHome();
            replaceFragment(fragment);
            viewmodel.currentFragment = FRAGMENT_HOME;
            current_Fragment = viewmodel.currentFragment;

        }
    }
    private void openStatisticFragment()
    {
        if(current_Fragment != FRAGMENT_STATISTIC)
        {
            fragment = new FragmentStatistic();
            replaceFragment(fragment);
            viewmodel.currentFragment = FRAGMENT_STATISTIC;
            current_Fragment = viewmodel.currentFragment;
        }
    }
    private void openAccountFragment()
    {
        if(current_Fragment != FRAGMENT_ACCOUNT)
        {
            fragment = new FragmentAccount();
            replaceFragment(fragment);
            viewmodel.currentFragment = FRAGMENT_ACCOUNT;
            current_Fragment = viewmodel.currentFragment;
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
        lottieAnimationView.setVisibility(View.VISIBLE);
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
                            MaterialDialog mDialog = new MaterialDialog.Builder(MainActivity.this)
                                    .setTitle("Notify")
                                    .setMessage("to use this app, please update your profile")
                                    .setCancelable(false)
                                    .setPositiveButton("         OK     ", R.drawable.ic_green_check_24, new MaterialDialog.OnClickListener() {
                                        @Override
                                        public void onClick(dev.shreyaspatil.MaterialDialog.interfaces.DialogInterface dialogInterface, int which) {
                                            startActivity(new Intent(getApplicationContext(), A_mucdich.class));
                                            finish();
                                        }
                                    }).build();
                            //mDialog.show();
                        }

                        saveCaloriesTarget();
                        lottieAnimationView.setVisibility(View.GONE);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        lottieAnimationView.setVisibility(View.GONE);

                    }
                });
    }

    private void saveCaloriesTarget() {
//        Calendar calendar = Calendar.getInstance();

//        String day = "";
//        int intday = calendar.get(Calendar.DAY_OF_MONTH);
//        if(intday < 10){
//            day = "0" + String.valueOf(intday);
//        }
//        else
//            day = String.valueOf(intday);
//
//        String month = "";
//        int intmonth = calendar.get(Calendar.MONTH);
//        if(intmonth < 10){
//            month = "0" + String.valueOf(intmonth);
//        }
//        else
//            month = String.valueOf(intmonth);
//
//        String year = String.valueOf(calendar.get(Calendar.YEAR));
//        String date = year + month + day;

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String date = sdf.format(calendar.getTime());

        FirebaseDatabase.getInstance().getReference().child("users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("calodaily").child(date).setValue(new CaloDaily(date, calories));


//        Calendar calendar = Calendar.getInstance();
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
//        String date = simpleDateFormat.format(calendar.getTime());
//
//        Calendar calendar1 = Calendar.getInstance();
//        try {
//            calendar1.setTime(simpleDateFormat.parse("2021-05-05"));
//
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//
//        if(calendar.after(calendar1))
//            Toasty.info(getApplicationContext(), simpleDateFormat.format(calendar1.getTime()), Toasty.LENGTH_LONG).show();
//        else
//            Toasty.info(getApplicationContext(), date, Toasty.LENGTH_LONG).show();
    }

}