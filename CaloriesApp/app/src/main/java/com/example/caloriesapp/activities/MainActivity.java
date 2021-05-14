package com.example.caloriesapp.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.caloriesapp.R;
import com.example.caloriesapp.fragment.FragmentAccount;
import com.example.caloriesapp.fragment.FragmentHome;
import com.example.caloriesapp.fragment.FragmentStatistic;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private static final int FRAGMENT_HOME = 1;
    private static final int FRAGMENT_STATISTIC = 2;
    private static final int FRAGMENT_ACCOUNT = 3;

    //private User user;
    private int current_Fragment = FRAGMENT_HOME;
    private BottomNavigationView navigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addFragment(new FragmentHome());

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




}