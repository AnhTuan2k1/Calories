package com.example.caloriesapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.caloriesapp.R;
import com.example.caloriesapp.fragment.FragmentHome;
import com.example.caloriesapp.fragment.FragmentOpenGmail;
import com.example.caloriesapp.fragment.FragmentSendEmail;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import es.dmoral.toasty.Toasty;

public class ForgetPasswordActivity extends AppCompatActivity {

    private static final int FRAGMENT_OPEN_GMAIL = 2;
    private static final int FRAGMENT_SENDEMAIL = 1;

    private Fragment fragment;
    private int current_Fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        current_Fragment = 0;
        openSendemailFragment();

    }

    @Override
    public void onBackPressed() {
        if(current_Fragment == 1)
        {
            Intent intent = new Intent(ForgetPasswordActivity.this, LoginActivity.class);
            startActivity(intent);
            super.onBackPressed();
        }
        else if(current_Fragment == 2)
        {
            openSendemailFragment();
        }
    }


    public void openSendemailFragment()
    {
        if(current_Fragment == FRAGMENT_OPEN_GMAIL)
        {
            fragment = new FragmentSendEmail();
            replaceFragment(fragment);
            current_Fragment = FRAGMENT_SENDEMAIL;

        }
        else if(current_Fragment == 0)
        {
            addFragment(new FragmentSendEmail());
            current_Fragment = FRAGMENT_SENDEMAIL;
        }
    }

    public void openOpengmailFragment()
    {
        if(current_Fragment == FRAGMENT_SENDEMAIL)
        {
            fragment = new FragmentOpenGmail();
            replaceFragment(fragment);
            current_Fragment = FRAGMENT_OPEN_GMAIL;

        }
        else if(current_Fragment == 0)
        {
            addFragment(new FragmentOpenGmail());
            current_Fragment = FRAGMENT_OPEN_GMAIL;
        }
    }

    private void replaceFragment(Fragment fragment)
    {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.framelayout_forgetpassword,fragment);
        fragmentTransaction.commit();
    }

    private void addFragment(Fragment fragment)
    {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.framelayout_forgetpassword,fragment);
        fragmentTransaction.commit();
    }
}