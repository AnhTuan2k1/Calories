package com.example.caloriesapp.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.caloriesapp.A_mucdich;
import com.example.caloriesapp.R;
import com.example.caloriesapp.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import es.dmoral.toasty.Toasty;

public class LoginActivity extends AppCompatActivity {

    EditText editText_email;
    EditText editText_password;
    Button btnLogin;
    TextView textview_register;
    TextView textView_fogotpassword;
    ProgressBar progressBar;
    FirebaseAuth firebaseAuth;
    DatabaseReference myDatabase;
    FirebaseUser firebaseUser;
    float calories = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        anhxa();
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
        textview_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                finish();
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
       // if(firebaseUser == null) return;

    }


    private void login() {
        String email = editText_email.getText().toString().trim();
        String password = editText_password.getText().toString().trim();
        if(TextUtils.isEmpty(email))
        {
            editText_email.setError("Email is Missing");
            return;
        }
        if(TextUtils.isEmpty(password))
        {
            editText_password.setError("Password is Missing");
            return;
        }

        //progessbar start
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                // get info account on firebase
                //if not have info: start initialize info account activity
                // Intent intent = new Intent(LoginActivity.this, A_mucdich.class);
                //else start mainActivity
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toasty.error(LoginActivity.this, e.getMessage(), Toasty.LENGTH_SHORT).show();
            }

        });

    }


    private void anhxa() {
        editText_email = findViewById(R.id.editext_email_LogActivity);
        editText_password = findViewById(R.id.edittext_password_LogActivity);
        btnLogin = findViewById(R.id.btn_login_LogActivity);
        textview_register = findViewById(R.id.textView_register_logActivity);
        textView_fogotpassword = findViewById(R.id.forgotpassword);
        progressBar = findViewById(R.id.progressBar_Login);

        firebaseAuth = FirebaseAuth.getInstance();
        myDatabase = FirebaseDatabase.getInstance().getReference();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    }
}