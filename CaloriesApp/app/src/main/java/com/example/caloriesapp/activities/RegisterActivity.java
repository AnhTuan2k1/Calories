package com.example.caloriesapp.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.example.caloriesapp.A_mucdich;
import com.example.caloriesapp.R;
import com.example.caloriesapp.User;
import com.example.caloriesapp.viewmodel.MainActivityViewModel;
import com.example.caloriesapp.viewmodel.RegisterActivityViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.ActionCodeSettings;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;
import java.util.regex.Pattern;

import es.dmoral.toasty.Toasty;

public class RegisterActivity extends AppCompatActivity {

    LottieAnimationView lottieAnimationView;
    TextInputLayout textInputLayout2_Reg;
    TextView textView_signin;
    EditText editText_email;
    EditText editText_password;
    EditText editText_confirmpassword;
    Button button_register;
    ProgressBar progressBar;
    FirebaseAuth firebaseAuth;
    DatabaseReference myDatabase;
    RegisterActivityViewModel viewModel;


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
//        editText_email.setOnKeyListener(new View.OnKeyListener() {
//            @Override
//            public boolean onKey(View v, int keyCode, KeyEvent event) {
//                viewModel.editText_email = editText_email.getText().toString();
//                return false;
//            }
//        });
//        editText_password.setOnKeyListener(new View.OnKeyListener() {
//            @Override
//            public boolean onKey(View v, int keyCode, KeyEvent event) {
//                viewModel.editText_password = editText_password.getText().toString();
//                return false;
//            }
//        });
//        editText_confirmpassword.setOnKeyListener(new View.OnKeyListener() {
//            @Override
//            public boolean onKey(View v, int keyCode, KeyEvent event) {
//                viewModel.editText_confirmpassword = editText_confirmpassword.getText().toString();
//                return false;
//            }
//        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void register() {
        String email = editText_email.getText().toString().trim();
        String password = editText_password.getText().toString().trim();
        String confirmpassword = editText_confirmpassword.getText().toString().trim();
        textInputLayout2_Reg.setError(null);
        if(TextUtils.isEmpty(email))
        {
            editText_email.setError("Email is Required");
            return;
        }
        if(TextUtils.isEmpty(password))
        {
            editText_password.setError("Password is Required");
            return;
        }
        if(TextUtils.isEmpty(confirmpassword))
        {
            editText_password.setError("Confirm Password is Required");

            return;
        }
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

        lottieAnimationView.setVisibility(View.VISIBLE);
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                myDatabase.child("users").child(FirebaseAuth.getInstance().getUid())
                        .child("userinfo").setValue(new User("0","0","0",0,
                        0,0,0,0,0, email));

                Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).sendEmailVerification()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Intent intent = new Intent(getApplicationContext(), VerificationActivity.class);
                        startActivity(intent);
                        lottieAnimationView.setVisibility(View.GONE);
                        finish();

                        FirebaseAuth.getInstance().signOut();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        lottieAnimationView.setVisibility(View.GONE);
                        Toasty.error(RegisterActivity.this, Objects.requireNonNull(e.getMessage()), Toasty.LENGTH_LONG).show();

                        FirebaseAuth.getInstance().signOut();
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toasty.error(RegisterActivity.this, Objects.requireNonNull(e.getMessage()), Toasty.LENGTH_LONG).show();
                lottieAnimationView.setVisibility(View.GONE);
            }
        });


    }

    private boolean isValidemail(CharSequence email) {
        return(!TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches());
    }

    private void anhxa() {
        viewModel = new ViewModelProvider(this,
                new ViewModelProvider.AndroidViewModelFactory(getApplication())).get(RegisterActivityViewModel.class);

        textView_signin = findViewById(R.id.textView_signin_regActivity);
        editText_email = findViewById(R.id.editext_email_RegActivity);
        editText_password = findViewById(R.id.edittext_password_regActivity);
        editText_confirmpassword = findViewById(R.id.edittext_confirmpassword_RegActivity);
        button_register = findViewById(R.id.btn_register_RegActivity);
        lottieAnimationView = findViewById(R.id.LottieAnimationView_Reg);


        firebaseAuth = FirebaseAuth.getInstance();
        myDatabase = FirebaseDatabase.getInstance().getReference();
        textInputLayout2_Reg = findViewById(R.id.textInputLayout2_Reg);

        editText_email.setText(viewModel.editText_email);
        editText_confirmpassword.setText(viewModel.editText_confirmpassword);
        editText_password.setText(viewModel.editText_password);
    }


}