package com.example.caloriesapp.fragment;


import android.app.Notification;
import android.app.PendingIntent;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.caloriesapp.R;
import com.example.caloriesapp.User;
import com.example.caloriesapp.activities.LoginActivity;
import com.example.caloriesapp.activities.MainActivity;

import android.widget.Toast;
import com.example.caloriesapp.Foodate;

import com.example.caloriesapp.activity_editprofile;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.Date;


import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;

import static com.example.caloriesapp.activities.Notification.CHANNEL_ID;


public class FragmentAccount extends Fragment {

    private User user;
    private CircleImageView profile;
    private FloatingActionButton changeProfile;
    private View view;

    private TextView userName, male, age, height, currentWeight,goalWeight, purpose;
    boolean Click;
    ImageButton notification, editprofile;
    Button logout;

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

        notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendNotification();

                if(Click==false)
                {
                    notification.setImageResource(R.drawable.ic_baseline_notifications_off_24);
                }
                else
                    notification.setImageResource(R.drawable.ic_baseline_notifications);
            }
        });
        editprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), activity_editprofile.class);
                startActivity(intent);
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Uri uri = data.getData();
        profile.setImageURI(uri);
    }


    private void sendNotification() {

        Intent intent = new Intent(getActivity(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(getActivity(),0, intent,0);

        NotificationCompat.Builder notification = new NotificationCompat.Builder(getActivity(),CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_water_notification)
                .setContentTitle("Calories App")
                .setContentText("Đã đến lúc uống nước!")
                .setStyle(new NotificationCompat.BigTextStyle().bigText("Đã đến lúc uống nước!"))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);


        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getActivity());
// notificationId is a unique int for each notification that you must define
        notificationManager.notify(getNotificationId(), notification.build());

    }
    private int getNotificationId(){
        return (int) new Date().getTime();
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
        notification = view.findViewById(R.id.imb_notification);
        logout = view.findViewById(R.id.btnLogin);
        purpose = view.findViewById(R.id.txtpurpose);
        editprofile =view.findViewById(R.id.imb_edit);

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
                }



                // update ui here with user
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                if(getContext() != null)
                    Toasty.info(getContext(), "Please Restart",Toasty.LENGTH_SHORT).show();
            }
        });
    }
}