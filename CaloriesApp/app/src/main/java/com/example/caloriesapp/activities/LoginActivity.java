package com.example.caloriesapp.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.example.caloriesapp.A_mucdich;
import com.example.caloriesapp.R;
import com.example.caloriesapp.User;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import es.dmoral.toasty.Toasty;

public class LoginActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 1301;

    LottieAnimationView lottieAnimationView;
    EditText editText_email;
    EditText editText_password;
    Button btnLogin;
    TextView textview_register;
    TextView textView_fogotpassword;
    ImageView imageView_google;
    ImageView imageView_facabook;

    FirebaseAuth firebaseAuth;
    DatabaseReference myDatabase;
    FirebaseUser firebaseUser;
    float calories = 0f;
    private GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        anhxa();
        createRequestGoogleSignin();

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
        imageView_google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signInGoogle();
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
        if(firebaseUser == null) return;

        startActivity(new Intent(LoginActivity.this, MainActivity.class));
        finish();
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

        lottieAnimationView.setVisibility(View.VISIBLE);
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                // get info account on firebase
                //if not have info: start initialize info account activity
                // Intent intent = new Intent(LoginActivity.this, A_mucdich.class);
                //else start mainActivity
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                lottieAnimationView.setVisibility(View.GONE);
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toasty.error(LoginActivity.this, e.getMessage(), Toasty.LENGTH_SHORT).show();
                lottieAnimationView.setVisibility(View.GONE);
            }
        });


    }

    ///////////////////// google sign in ////////////////////////////////
    private void createRequestGoogleSignin() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    private void signInGoogle(){
        lottieAnimationView.setVisibility(View.VISIBLE);
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Toasty.error(LoginActivity.this, "Google sign in failed (API)", Toast.LENGTH_SHORT).show();
                lottieAnimationView.setVisibility(View.GONE);
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            firebaseUser = firebaseAuth.getCurrentUser();
                            checkInfoUser(firebaseUser);
                        } else {
                            Toasty.error(LoginActivity.this, "Google sign in failed", Toast.LENGTH_SHORT).show();
                            lottieAnimationView.setVisibility(View.GONE);

                        }
                    }
                });
    }

    private void checkInfoUser(FirebaseUser firebaseUser)
    {
        if(firebaseUser == null)
        {
            lottieAnimationView.setVisibility(View.GONE);
            return;
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
                            lottieAnimationView.setVisibility(View.GONE);
                            return;
                        }

                        calories = snapshot.child("dailyCaloriesTarget").getValue(float.class);
                        if(calories == 0f)
                        {
                            startActivity(new Intent(getApplicationContext(), A_mucdich.class));
                            finish();
                            lottieAnimationView.setVisibility(View.GONE);
                        }
                        else
                        {
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                            finish();
                            lottieAnimationView.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        lottieAnimationView.setVisibility(View.GONE);
                    }
                });
    }
    ///////////////////// google sign in ////////////////////////////////


    private void anhxa() {
        lottieAnimationView = findViewById(R.id.LottieAnimationView_Login);
        editText_email = findViewById(R.id.editext_email_LogActivity);
        editText_password = findViewById(R.id.edittext_password_LogActivity);
        btnLogin = findViewById(R.id.btn_login_LogActivity);
        textview_register = findViewById(R.id.textView_register_logActivity);
        textView_fogotpassword = findViewById(R.id.forgotpassword);
        imageView_google = findViewById(R.id.imageView_google);
        imageView_facabook = findViewById(R.id.imageView_facebook);

        firebaseAuth = FirebaseAuth.getInstance();
        myDatabase = FirebaseDatabase.getInstance().getReference();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    }
}