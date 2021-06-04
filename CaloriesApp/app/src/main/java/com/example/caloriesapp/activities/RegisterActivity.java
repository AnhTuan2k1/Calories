package com.example.caloriesapp.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.caloriesapp.A_mucdich;
import com.example.caloriesapp.R;
import com.example.caloriesapp.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.regex.Pattern;

import es.dmoral.toasty.Toasty;

public class RegisterActivity extends AppCompatActivity {

    TextInputLayout textInputLayout2_Reg;
    TextView textView_signin;
    EditText editText_email;
    EditText editText_password;
    EditText editText_confirmpassword;
    Button button_register;
    ProgressBar progressBar;
    FirebaseAuth firebaseAuth;
    DatabaseReference myDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        anhxa();
        button_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });
        textView_signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

    private void register() {
        String email = editText_email.getText().toString().trim();
        String password = editText_password.getText().toString().trim();
        String confirmpassword = editText_confirmpassword.getText().toString().trim();
//        if(TextUtils.isEmpty(email))
//        {
//            editText_email.setError("Email is Required");
//            return;
//        }
//        if(TextUtils.isEmpty(password))
//        {
//            editText_password.setError("Password is Required");
//            return;
//        }
//        if(TextUtils.isEmpty(confirmpassword))
//        {
//            editText_password.setError("Confirm Password is Required");
//            return;
//        }
        if(!password.equals(confirmpassword))
        {
            editText_confirmpassword.setError("Password Do not Match");
            return;
        }
//        if(password.length()<4)
//        {
//            editText_password.setError("Length should be > 4");
//            return;
//        }
//        if(!isValidemail(email))
//        {
//            editText_email.setError("Invalid email");
//            return;
//        }

        //progessbar start
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                Toasty.success(RegisterActivity.this, "Register Successfully", Toasty.LENGTH_SHORT).show();
                myDatabase.child("users").child(FirebaseAuth.getInstance().getUid())
                        .child("userinfo").setValue(new User("9","8","7",6,5,4,3,2,0));


                Intent intent = new Intent(RegisterActivity.this, A_mucdich.class);
                startActivity(intent);
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toasty.error(RegisterActivity.this, e.getMessage(), Toasty.LENGTH_LONG).show();
            }
        });

    }

    private boolean isValidemail(CharSequence email) {
        return(!TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches());
    }

    private void anhxa() {
        textView_signin = findViewById(R.id.textView_signin_regActivity);
        editText_email = findViewById(R.id.editext_email_RegActivity);
        editText_password = findViewById(R.id.edittext_password_regActivity);
        editText_confirmpassword = findViewById(R.id.edittext_confirmpassword_RegActivity);
        button_register = findViewById(R.id.btn_register_RegActivity);
        progressBar = findViewById(R.id.progressBar_Reg);


        firebaseAuth = FirebaseAuth.getInstance();
        myDatabase = FirebaseDatabase.getInstance().getReference();
        textInputLayout2_Reg = findViewById(R.id.textInputLayout2_Reg);
    }


}