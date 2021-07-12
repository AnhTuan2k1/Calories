package com.example.caloriesapp.fragment;


import android.app.Dialog;
import android.app.Notification;
import android.app.PendingIntent;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;

import android.text.InputType;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.caloriesapp.A_info_2;
import com.example.caloriesapp.A_info_3;
import com.example.caloriesapp.A_info_5;
import com.example.caloriesapp.A_mucdich;
import com.example.caloriesapp.CaloDaily;
import com.example.caloriesapp.R;
import com.example.caloriesapp.User;
import com.example.caloriesapp.activities.LoginActivity;
import com.example.caloriesapp.activities.MainActivity;

import android.widget.Toast;
import com.example.caloriesapp.Foodate;

import com.example.caloriesapp.activity_editprofile;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


import de.hdodenhof.circleimageview.CircleImageView;
import dev.shreyaspatil.MaterialDialog.AbstractDialog;
import dev.shreyaspatil.MaterialDialog.MaterialDialog;
import dev.shreyaspatil.MaterialDialog.interfaces.DialogInterface;
import es.dmoral.toasty.Toasty;

import static com.example.caloriesapp.activities.Notification.CHANNEL_ID;


public class FragmentAccount extends Fragment {

    private User user;
    private CircleImageView profile;
    private FloatingActionButton changeProfile;
    private View view;

    private TextView userName, male, age, height, currentWeight,goalWeight, purpose;
    boolean Click;
    Button logout;

    private TextView goal, txtgoal;
    private ImageButton imbGoal;
    private ImageButton imbHeight;
    private ImageButton imbWeight;
    private ImageButton imbSex;
    private ImageButton imbAge;
    private ImageButton imageView_name;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.fragment_account, container, false);


        Click =false;

        AnhXa();
        syncUserWithFirebase();
        changeProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImagePicker.Companion.with(FragmentAccount.this)
                        .crop()	    			//Crop image(Optional), Check Customization for more option
                        .compress(1024)			//Final image size will be less than 1 MB(Optional)
                        .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                        .start();
            }
        });


        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToAttract(view);
            }
        });


        goal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openDialog();
            }
        });
        imbGoal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog();
            }
        });

        imbSex.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showGenderDialog();
            }
        });

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openeditageDialog(Gravity.CENTER, (ImageButton)v);
            }
        };


        imbHeight.setOnClickListener(onClickListener);
        imbWeight.setOnClickListener(onClickListener);
        imbAge.setOnClickListener(onClickListener);
        imageView_name.setOnClickListener(onClickListener);

        return view;
    }


    private void openeditageDialog(int gravity, ImageButton imageButton) {

        final Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_editage);

        Window window = dialog.getWindow();
        if(window == null) return;

        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        windowAttributes.gravity = gravity;
        window.setAttributes(windowAttributes);


        TextView textView = dialog.findViewById(R.id.textView5);
        EditText editText = dialog.findViewById(R.id.edittext_centimet_dialogdedit);
        Button btnOK = dialog.findViewById(R.id.ok_dialogedit);
        /////////////////////////////////////////////

        switch (imageButton.getId())
        {
            case R.id.imbAge_edit :
                textView.setText("Age");
                editText.setText(String.valueOf(user.getAge()));
                editText.setHint("");
                break;
            case R.id.imbHeight_edit :
                textView.setText("Height");
                editText.setText(String.valueOf((int) user.getHeight()));
                editText.setHint("Centimet");
                break;
            case R.id.imbWeight_edit :
                textView.setText("Weight");
                editText.setText(String.valueOf((int) user.getCurrentWeight()));
                editText.setHint("Kg");
                break;
            case R.id.imageView_name:
                textView.setText("Name");
                editText.setText(user.getUserName());
                editText.setHint("");
                editText.setInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
                break;
        }

        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatabaseReference reference =  FirebaseDatabase.getInstance().getReference()
                        .child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .child("userinfo");

                try{
                    switch (imageButton.getId())
                    {
                        case R.id.imbAge_edit :
                            if(editText.getText().toString().equals("")
                                    || Integer.parseInt(editText.getText().toString())<=5
                                    || Integer.parseInt(editText.getText().toString())>100){
                                Toasty.error(getContext(),"Invalid Age",Toasty.LENGTH_SHORT).show();
                            }
                            else{
                                user.setAge(Integer.parseInt(editText.getText().toString()));
                                age.setText(String.valueOf(user.getAge()));
                                reference.setValue(user);
                            }

                            break;
                        case R.id.imbHeight_edit :
                            int chieucao = Integer.parseInt(editText.getText().toString());
                            if(editText.getText().toString().equals("")||chieucao<30||chieucao>250){
                                Toasty.error(getContext(),"Invalid Height",Toasty.LENGTH_SHORT).show();
                            }
                            else{
                                user.setHeight(chieucao);
                                height.setText(String.valueOf(user.getHeight()) + " CM" );
                                reference.setValue(user);
                            }

                            break;
                        case R.id.imbWeight_edit :
                            if(editText.getText().toString().matches("")
                                    ||Integer.parseInt(editText.getText().toString()) <0
                                    ||Integer.parseInt(editText.getText().toString()) >300
                                    ||Integer.parseInt(editText.getText().toString())
                                    != Integer.parseInt(editText.getText().toString())
                            ){
                                Toasty.error(getContext(),"Invalid Weight",Toasty.LENGTH_SHORT).show();
                            }
                            else{
                                user.setCurrentWeight(Integer.parseInt(editText.getText().toString()));
                                currentWeight.setText(String.valueOf(user.getCurrentWeight()) +" KG" );
                                reference.setValue(user);
                            }

                            break;
                        case R.id.imageView_name:
                            if(editText.getText().toString().isEmpty())
                            {
                                dialog.dismiss();
                                return;
                            }
                            user.setUserName(editText.getText().toString());
                            userName.setText(user.getUserName());
                            reference.setValue(user);
                            break;
                    }
                }catch (Exception ignored){}


                dialog.dismiss();

            }
        });

        //dialog.setCancelable(false);
        dialog.show();
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Uri uri = null;
        try {
            uri = data.getData();
            profile.setImageURI(uri);


        }catch (Exception ignored){}
        StorageReference reference = FirebaseStorage.getInstance()
                .getReference().child(FirebaseAuth.getInstance().getUid());

        reference.putFile(uri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                FirebaseDatabase.getInstance().getReference().child("users")
                                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                        .child("userinfo")
                                        .child("image").setValue(uri.toString());
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                if(getContext() != null)
                                    Toasty.error(getContext(), e.getMessage(), Toasty.LENGTH_SHORT).show();
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                if(getContext() != null)
                    Toasty.error(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String getFileExtension(Uri uri) {
        String s = null;
        try{
            ContentResolver cr = getActivity().getContentResolver();
            MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
            s = mimeTypeMap.getExtensionFromMimeType(cr.getType(uri));
        }catch (Exception ignored){}

        return  s;
    }

    private void showGenderDialog() {
        String[] a = {"Male", "Female"};
        new MaterialAlertDialogBuilder(getContext())
                .setSingleChoiceItems(a, getCheckedItem(), new android.content.DialogInterface.OnClickListener() {
            @Override
            public void onClick(android.content.DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        user.setGender("Male");
                        male.setText("Male");
                        break;
                    case 1:
                        user.setGender("Female");
                        male.setText("Female");
                        break;
                }
                FirebaseDatabase.getInstance().getReference().child("users")
                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .child("userinfo").setValue(user);
                dialog.dismiss();
            }
        }).show();
    }

    private int getCheckedItem() {
        switch (male.getText().toString()) {
            case "Male":
                return 0;
            case "Female":
                return 1;
            default: return 0;
        }
    }




    private void AnhXa()
    {
        profile = view.findViewById(R.id.profile_image);
        changeProfile = view.findViewById(R.id.changeProfile);
        userName = view.findViewById(R.id.txtUsername);
        male = view.findViewById(R.id.textsex);
        age = view.findViewById(R.id.textage);
        height = view.findViewById(R.id.textheight);
        currentWeight = view.findViewById(R.id.textcurrentweight);
        goalWeight = view.findViewById(R.id.textgoalweight);
       // notification = view.findViewById(R.id.imb_notification);
        logout = view.findViewById(R.id.btnLogin);
        purpose = view.findViewById(R.id.txtpurpose);
        //editprofile =view.findViewById(R.id.imb_edit);
        goal = view.findViewById(R.id.txt_updategoal_Account);
        txtgoal = view.findViewById(R.id.txtgoal);
        imbSex = view.findViewById(R.id.imbSex_edit);
        imbAge = view.findViewById(R.id.imbAge_edit);
        imbHeight = view.findViewById(R.id.imbHeight_edit);
        imbWeight = view.findViewById(R.id.imbWeight_edit);
        imbGoal = view.findViewById(R.id.imbGoal_edit);
        imageView_name = view.findViewById(R.id.imageView_name);
    }

    public void goToAttract(View v)
    {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        startActivity(intent);
        getActivity().finish();

    }

    private void syncUserWithFirebase() {
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

                if(user != null){
                userName.setText(user.getUserName());
                age.setText(String.valueOf(user.getAge()));
                male.setText(user.getGender());
                height.setText(String.valueOf(user.getHeight()) + " CM" );
                currentWeight.setText(String.valueOf(user.getCurrentWeight()) +" KG" );
                goalWeight.setText(String.valueOf(user.getGoalWeight()) + " KG");
                purpose.setText(user.getPurposeWeight());
                txtgoal.setText(user.getPurposeWeight());
                if(user.getImage() != null)
                    loadImage(user.getImage());
                }



                // update ui here with user
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                if(getContext() != null)
                    Toasty.info(getContext(), error.getMessage(),Toasty.LENGTH_SHORT).show();
            }
        });
/*
        FirebaseDatabase.getInstance().getReference().child("users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("image").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                String s = snapshot.getValue(String.class);
                Bitmap bitmap = null;
                HttpURLConnection httpURLConnection = null;
                if(s != null)
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            HttpURLConnection httpURLConnection = null;
                            try {

                                httpURLConnection = (HttpURLConnection) new URL(s).openConnection();
                                Bitmap bitmap = BitmapFactory.decodeStream(httpURLConnection.getInputStream());

                                if(getActivity() != null)
                                {
                                    getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            profile.setImageBitmap(bitmap);
                                        }
                                    });
                                }

                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                        }
                    }).start();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

 */
    }

    private void loadImage(String url)
    {
        if(url != null)
            new Thread(new Runnable() {
                @Override
                public void run() {
                    HttpURLConnection httpURLConnection = null;
                    try {

                        httpURLConnection = (HttpURLConnection) new URL(url).openConnection();
                        Bitmap bitmap = BitmapFactory.decodeStream(httpURLConnection.getInputStream());

                        if(getActivity() != null)
                        {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    profile.setImageBitmap(bitmap);
                                }
                            });
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }).start();
    }

    private void openDialog() {
        MaterialDialog mDialog = new MaterialDialog.Builder(getActivity())
                .setTitle("Notification")
                .setMessage("Do you want to set your goal again ?")
                .setPositiveButton("         Yes     ", R.drawable.ic_green_check_24, new MaterialDialog.OnClickListener() {
                    @Override
                    public void onClick(dev.shreyaspatil.MaterialDialog.interfaces.DialogInterface dialogInterface, int which) {
                        startActivity(new Intent(getContext(), A_mucdich.class));
                        getActivity().finish();
                    }
                }).setNegativeButton("No", R.drawable.ic_cancel_24, new MaterialDialog.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        dialogInterface.dismiss();
                    }
                })
                .build();

        mDialog.show();
    }
}