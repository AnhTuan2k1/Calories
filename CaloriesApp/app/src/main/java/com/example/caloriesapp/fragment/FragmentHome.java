package com.example.caloriesapp.fragment;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.example.caloriesapp.A_Breakfast;
import com.example.caloriesapp.A_Excercise;
import com.example.caloriesapp.A_mucdich;
import com.example.caloriesapp.CaloDaily;
import com.example.caloriesapp.Exercise;
import com.example.caloriesapp.Foodate;
import com.example.caloriesapp.R;
import com.example.caloriesapp.activities.MainActivity;
import com.example.caloriesapp.viewmodel.MainActivityViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import dev.shreyaspatil.MaterialDialog.MaterialDialog;
import es.dmoral.toasty.Toasty;

import static com.facebook.FacebookSdk.getApplicationContext;


public class FragmentHome extends Fragment implements View.OnClickListener{
    boolean isClicked1,isClicked2,isClicked4,isClicked3;
    private ImageView imbottle1,imbottle2,imbottle3,imbottle4;
    private List<Foodate> foodateList;
    private int daynextt,daybackk;
    private String string_datenow,string_breakfast,string_lunch,string_dinner,string_snacks,string_excercise;
    private List<Exercise> exerciseList;
    private float caloDaily = 0f;
    private float caloshow;
    private DatePickerDialog.OnDateSetListener setListener;
    private boolean isClicked;

    float water;
    private TextView watercount,caloshow_breakfast,caloshow_lunch,caloshow_dinner,caloshow_snacks,text_explain_3;
    private ImageView imageBreakfast, imageLunch,imageDinner,imageSnack,backday,nextday,imageExcercise;
    private CardView cardBreakfast,cardLunch,cardDinner,cardSnack;
    private TextView date_home,text_calodaily,burnedcalo,earnedcalo;
    public static final String SESSION = "com.example.application.example.EXTRA_SESSION";
    private MainActivityViewModel viewmodel;
    @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
    @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf2 = new SimpleDateFormat("MMM dd");

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        AnhXa(view);
        Set_date_time();
        syncDataWithFirebase(string_datenow); // truyen vao ngay can update ui  "yyyy-m-dd"



        return view;
    }

    private void syncDataWithFirebase(String date) {
        if(FirebaseAuth.getInstance().getCurrentUser() == null)
        {
            return;
        }
          updateUI(date);
//        updateFoodateList(date);
//        updateExercise(date);

        //update calo fragment home
        updateCaloDaily(date,"breakfast",caloshow_breakfast);
        updateCaloDaily(date,"lunch",caloshow_lunch);
        updateCaloDaily(date,"dinner",caloshow_dinner);
        updateCaloDaily(date,"snacks",caloshow_snacks);
        //update earned calo
        updateEarnedCalo(date);
        //update burned calo
        updateBurnedCalo(date);

    }
    private void  updateUI(String date){
        FirebaseDatabase.getInstance().getReference().child("users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("calodaily").child(date).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try {


                        if(snapshot.getValue(CaloDaily.class) == null)
                        {
                            FirebaseDatabase.getInstance().getReference().child("users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .child("calodaily").child(date).setValue(new CaloDaily(date, caloDaily));
                            return;
                        }
                       

                    else
                    {
                        caloDaily = Math.round(snapshot.getValue(CaloDaily.class).getCalories()) ;
                        text_calodaily.setText("Daily Calories Target\n");
                        text_calodaily.append(String.valueOf(Math.round(caloDaily)));
                    }
                }catch (Exception e){}


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                if(getContext() != null)
                Toasty.info(getContext(), "Please Restart",Toasty.LENGTH_SHORT).show();
            }
        });
    }
    private void updateEarnedCalo(String date){
        FirebaseDatabase.getInstance().getReference().child("users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("foodate").child(date).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Foodate foodate = dataSnapshot.getValue(Foodate.class);
                        float a = (foodate.getGram()*foodate.getCalories());
                        caloshow = caloshow+ a;
                }
                earnedcalo.setText(String.valueOf(Math.round(caloshow)));
                caloshow = 0;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void updateBurnedCalo(String date){
        FirebaseDatabase.getInstance().getReference().child("users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("exercise").child(date).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Exercise exercise = dataSnapshot.getValue(Exercise.class);
                    float a = exercise.getCalories()*exercise.getDuration();
                    caloshow = caloshow+ a;
                }
                burnedcalo.setText(String.valueOf(Math.round(caloshow)));

                caloshow = 0;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void updateFoodateList(String date) {
        FirebaseDatabase.getInstance().getReference().child("users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("foodate").child(date).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren())
                {
                    Foodate foodate = dataSnapshot.getValue(Foodate.class);
                    foodateList.add(foodate);
                }

                // update ui here with foodateList
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                if(getContext() != null)
                Toasty.info(getContext(), "Please Restart",Toasty.LENGTH_SHORT).show();
            }
        });
    }



    private void updateCaloDaily(String date,String sessionofday,TextView textView) {
        FirebaseDatabase.getInstance().getReference().child("users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("foodate").child(date).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Foodate foodate = dataSnapshot.getValue(Foodate.class);
                    if(foodate.getSessionofday().equals(sessionofday)){

                        float a = (foodate.getGram()*foodate.getCalories());

                        caloshow = caloshow+ a;

                    }
                    else{

                    }
                }
                textView.setText(String.valueOf(Math.round(caloshow) + " Cal"));
                caloshow = 0;

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                if(getContext() != null)
                Toasty.info(getContext(), "Please Restart",Toasty.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.bottle1:
                if(isClicked1==false){
                    imbottle1.setImageResource(R.drawable.icon_bottle_filled);
                    isClicked1 = true;
                    water +=0.5;
                    watercount.setText(String.valueOf(water)+ "/2L");
                }
                else {
                    imbottle1.setImageResource(R.drawable.icon_bottle_unfilled);
                    isClicked1 = false;
                    water-=0.5;
                    watercount.setText(String.valueOf(water)+ "/2L");
                }
                break;
            case R.id.bottle2:
                if(isClicked2==false){
                    imbottle2.setImageResource(R.drawable.icon_bottle_filled);
                    isClicked2 = true;
                    water +=0.5;
                    watercount.setText(String.valueOf(water)+ "/2L");
                }
                else {
                    imbottle2.setImageResource(R.drawable.icon_bottle_unfilled);
                    isClicked2 = false;
                    water-=0.5;
                    watercount.setText(String.valueOf(water)+ "/2L");
                }
                break;
            case R.id.bottle3:
                if(isClicked3==false){
                    imbottle3.setImageResource(R.drawable.icon_bottle_filled);
                    isClicked3 = true;
                    water +=0.5;
                    watercount.setText(String.valueOf(water)+ "/2L");
                }
                else {
                    imbottle3.setImageResource(R.drawable.icon_bottle_unfilled);
                    isClicked3 = false;
                    water-=0.5;
                    watercount.setText(String.valueOf(water)+ "/2L");
                }
                break;
            case R.id.bottle4:
                if(isClicked4==false){
                    imbottle4.setImageResource(R.drawable.icon_bottle_filled);
                    isClicked4 = true;
                    water +=0.5;
                    watercount.setText(String.valueOf(water)+ "/2L");
                }
                else {
                    imbottle4.setImageResource(R.drawable.icon_bottle_unfilled);
                    isClicked4 = false;
                    water-=0.5;
                    watercount.setText(String.valueOf(water)+ "/2L");
                }
                break;
            case R.id.cardbreakfast:
                Intent breakfast = new Intent(getActivity(), A_Breakfast.class);
                breakfast.putExtra("key",string_datenow);
                breakfast.putExtra("session",string_breakfast);
                startActivity(breakfast);
                getActivity().finish();
                break;
            case R.id.cardlunch:
                Intent lunch = new Intent(getActivity(), A_Breakfast.class);
                lunch.putExtra("key",string_datenow);
                lunch.putExtra("session",string_lunch);
                startActivity(lunch);
                getActivity().finish();
                break;
            case R.id.carddinner:
                Intent dinner = new Intent(getActivity(), A_Breakfast.class);
                dinner.putExtra("key",string_datenow);
                dinner.putExtra("session",string_dinner);
                startActivity(dinner);
                getActivity().finish();
                break;
            case R.id.cardsnack:
                Intent snacks = new Intent(getActivity(), A_Breakfast.class);
                snacks.putExtra("key",string_datenow);
                snacks.putExtra("session",string_snacks);
                startActivity(snacks);
                getActivity().finish();
                break;
            case R.id.imageday_back:
                syncDataWithFirebase(getyesterday(string_datenow));
//                Toast.makeText(getActivity(),getyesterday(string_datenow),Toast.LENGTH_SHORT).show();
                string_datenow = getyesterday(string_datenow);
                viewmodel.string_datenow = string_datenow;
                date_home.setText(string_datenow);
                try {
                    date_home.setText(sdf2.format(sdf1.parse(string_datenow)));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.imageday_next:
                syncDataWithFirebase(getnextday(string_datenow));
//                Toast.makeText(getActivity(),getnextday(string_datenow),Toast.LENGTH_SHORT).show();
                string_datenow = getnextday(string_datenow);
                viewmodel.string_datenow = string_datenow;
                date_home.setText(string_datenow);
                try {
                    date_home.setText(sdf2.format(sdf1.parse(string_datenow)));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.imageexcercise:
                Intent excercise = new Intent(getActivity(), A_Excercise.class);
                excercise.putExtra("key",string_datenow);
                startActivity(excercise);
                getActivity().finish();
                break;

            case R.id.home_date:
                Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog dialog = new DatePickerDialog(getContext(), android.R.style.Theme_Holo_Dialog_MinWidth,setListener,year,month,day);
                dialog.show();
                break;
            case R.id.textview_calodaily:
                String temp =  text_calodaily.getText().toString();
                if(isClicked==false){
                    isClicked = true;
                    text_calodaily.clearComposingText();
                    text_calodaily.setText("Remaining Calories\n");
                    int b = Math.round(caloDaily) - Integer.parseInt( earnedcalo.getText().toString()) + Integer.parseInt(burnedcalo.getText().toString());
                    text_calodaily.append(String.valueOf(b));
                }
                else{
                    isClicked = false;
                    text_calodaily.clearComposingText();
                    text_calodaily.setText("Daily Calories Target\n");
                    text_calodaily.append(String.valueOf(Math.round(caloDaily)));
                }


                break;
        }
    }


    public void AnhXa(View view){
        viewmodel = ((MainActivity) getActivity()).viewmodel;
        foodateList = new ArrayList<>();
        exerciseList = new ArrayList<>();

        imbottle1 = view.findViewById(R.id.bottle1);
        imbottle2 = view.findViewById(R.id.bottle2);
        imbottle3 = view.findViewById(R.id.bottle3);
        imbottle4 = view.findViewById(R.id.bottle4);
        watercount = view.findViewById(R.id.watercount);
        date_home = view.findViewById(R.id.home_date);
        text_calodaily = view.findViewById(R.id.textview_calodaily);


        imageBreakfast = view.findViewById(R.id.imagebreakfast);
        imageLunch = view.findViewById(R.id.imagelunch);
        imageDinner = view.findViewById(R.id.imagedinner);
        imageSnack = view.findViewById(R.id.imagesnack);
        imageExcercise = view.findViewById(R.id.imageexcercise);

        caloshow_breakfast = view.findViewById(R.id.caloshow_breakfast);
        caloshow_lunch = view.findViewById(R.id.caloshow_lunch);
        caloshow_dinner = view.findViewById(R.id.caloshow_dinner);
        caloshow_snacks = view.findViewById(R.id.caloshow_snacks);

        burnedcalo = view.findViewById(R.id.burnedcalo);
        earnedcalo = view.findViewById(R.id.earnedcalo);

        backday = view.findViewById(R.id.imageday_back);
        nextday = view.findViewById(R.id.imageday_next);

        cardBreakfast = view.findViewById(R.id.cardbreakfast);
        cardLunch = view.findViewById(R.id.cardlunch);
        cardDinner = view.findViewById(R.id.carddinner);
        cardSnack = view.findViewById(R.id.cardsnack);

        imbottle1.setImageResource(R.drawable.icon_bottle_unfilled);
        imbottle2.setImageResource(R.drawable.icon_bottle_unfilled);
        imbottle3.setImageResource(R.drawable.icon_bottle_unfilled);
        imbottle4.setImageResource(R.drawable.icon_bottle_unfilled);
        isClicked = false;
        isClicked1 = false;
        isClicked2 = false;
        isClicked3 = false;
        isClicked4 = false;
        water = 0;
        daynextt = 1;
        daybackk = 1;
        caloshow=0;
        string_datenow = viewmodel.string_datenow;
        date_home.setText(string_datenow);
        try {
            date_home.setText(sdf2.format(sdf1.parse(string_datenow)));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        try {
            Intent intent = getActivity().getIntent();
            if(intent.getStringExtra("date") != null)
            {
                string_datenow = intent.getStringExtra("date");
                viewmodel.string_datenow = string_datenow;
                date_home.setText(sdf2.format(sdf1.parse(string_datenow)));

            }

        }catch (Exception ignored){

        }
        string_breakfast = "breakfast";
        string_snacks = "snacks";
        string_dinner = "dinner";
        string_lunch = "lunch";

        imbottle1.setOnClickListener(this);
        imbottle2.setOnClickListener(this);
        imbottle3.setOnClickListener(this);
        imbottle4.setOnClickListener(this);
        text_calodaily.setOnClickListener(this);
        cardBreakfast.setOnClickListener(this);
        cardDinner.setOnClickListener(this);
        cardLunch.setOnClickListener(this);
        cardSnack.setOnClickListener(this);
        backday.setOnClickListener(this);
        nextday.setOnClickListener(this);
        imageExcercise.setOnClickListener(this);


        date_home.setOnClickListener(this);


        setListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month+1;
                date_home.setText(dayOfMonth + " thg " + month);
                String thang = String.valueOf(month);
                String ngay = String.valueOf(dayOfMonth);
                if(month<10)
                {
                    thang = "0"+String.valueOf(month);
                }
                if(dayOfMonth<10)
                {
                    ngay = "0"+String.valueOf(dayOfMonth);
                }
                string_datenow =year+"-"+thang+"-"+ngay;
                viewmodel.string_datenow = string_datenow;
                date_home.setText(string_datenow);
                try {
                    date_home.setText(sdf2.format(sdf1.parse(string_datenow)));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                syncDataWithFirebase(string_datenow);
            }
        };
    }

    private String getcurrentday() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String date = sdf.format(calendar.getTime());
        return date;
    }
    private String getnextday(String date){
        String dt = date;
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            c.setTime(sdf.parse(dt));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        c.add(Calendar.DATE, daynextt);  // number of days to add
        dt = sdf.format(c.getTime());
        int day = c.get(Calendar.DATE);
        int month = c.get(Calendar.MONTH) + 1;
        return dt;
    }

    private String getyesterday(String date){
        String dt = date;
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            c.setTime(sdf.parse(dt));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        c.add(Calendar.DATE, -daybackk);  // number of days to add
        dt = sdf.format(c.getTime());
        int day = c.get(Calendar.DATE);
        int month = c.get(Calendar.MONTH) + 1;
        return dt;
    }

    public void Set_date_time(){
        Calendar c = Calendar.getInstance();
        int day = c.get(Calendar.DAY_OF_MONTH);
        int month = c.get(Calendar.MONTH) + 1;
    }


}