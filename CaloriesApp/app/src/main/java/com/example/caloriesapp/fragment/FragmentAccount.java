package com.example.caloriesapp.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import com.example.caloriesapp.Foodate;
import com.example.caloriesapp.R;
import com.example.caloriesapp.User;
import com.example.caloriesapp.activities.LoginActivity;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;


public class FragmentAccount extends Fragment {

    private User user;
    private CircleImageView profile;
    private FloatingActionButton changeProfile;
    private View view;
    private TextView userName, male, age, height, currentWeight,goalWeight;
    ImageButton notification;
    Button logout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.fragment_account, container, false);

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
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Uri uri = data.getData();
        profile.setImageURI(uri);
    }

    private void AnhXa()
    {
        profile = view.findViewById(R.id.profile_image);
        changeProfile = view.findViewById(R.id.changeProfile);
        userName = view.findViewById(R.id.txtUsername);
        male = view.findViewById(R.id.textsex);
        age = view.findViewById(R.id.textage);
        height = view.findViewById(R.id.txtheight);
        currentWeight = view.findViewById(R.id.textcurrentweight);
        goalWeight = view.findViewById(R.id.textgoalweight);
        notification = view.findViewById(R.id.imb_notification);
        logout = view.findViewById(R.id.btnLogin);

    }

    public void goToAttract(View v)
    {
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        startActivity(intent);
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
                userName.setText(user.getUserName());
                age.setText(user.getAge());
                male.setText(user.getGender());
                height.setText((int) user.getHeight());
                currentWeight.setText(user.getPurposeWeight());
                goalWeight.setText((int) user.getGoalWeight());


                // update ui here with user
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toasty.info(getContext(), "Please Restart",Toasty.LENGTH_SHORT).show();
            }
        });
    }
}