package com.example.caloriesapp.fragment;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.media.Image;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.service.autofill.Dataset;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.caloriesapp.CaloDaily;
import com.example.caloriesapp.FoodAdapter;
import com.example.caloriesapp.Foodate;
import com.example.caloriesapp.FoodateStatisticAdapter;
import com.example.caloriesapp.R;
import com.example.caloriesapp.activities.MainActivity;
import com.example.caloriesapp.database.FoodDatabase;
import com.example.caloriesapp.database.FoodStatic;
import com.example.caloriesapp.viewmodel.FragmentStatisticViewModel;
import com.example.caloriesapp.viewmodel.MainActivityViewModel;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import es.dmoral.toasty.Toasty;


public class FragmentSubStatistic extends Fragment {

    private View view;
    private ImageView left;
    private ImageView down;
    private ImageView right;
    private TextView textView_date;

    private TextView totalCalories;
    private TextView averageCalories;
    private TextView Goal;
    private PieChart piechart;

    private TextView percentBreakfast;
    private TextView percentLunch;
    private TextView percentDinner;
    private TextView percentSnacks;
    private TextView caloBreakfast;
    private TextView caloLunch;
    private TextView caloDinner;
    private TextView caloSnacks;

    private Calendar currentdate;
    private int xChecked;
    private MainActivityViewModel viewmodel;
    private List<Foodate> foodateList;
    private RecyclerView recyclerView;

    private float ftotalCalories;
    private float fcaloBreakfast;
    private float fcaloLunch;
    private float fcaloDinner;
    private float fcaloSnacks;
    private float fgoal;
    SimpleDateFormat sdf;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_sub_statistic, container, false);
        anhxa();
        updateUI();
        down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showOptionDialog();
            }
        });
        left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                decreasedate();
            }
        });
        right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                increasedate();
            }
        });
        textView_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateTimeDialog(textView_date);
            }
        });

        return view;
    }

    private void increasedate() {
        currentdate.add(Calendar.DAY_OF_MONTH, 1);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMM dd", Locale.getDefault());
        textView_date.setText(simpleDateFormat.format(currentdate.getTime()));
        viewmodel.textView_date = textView_date.getText().toString();
        viewmodel.currentdate.setTime(currentdate.getTime());
        updateUI();
    }

    private void decreasedate() {
        currentdate.add(Calendar.DAY_OF_MONTH, -1);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMM dd", Locale.getDefault());
        textView_date.setText(simpleDateFormat.format(currentdate.getTime()));
        viewmodel.textView_date = textView_date.getText().toString();
        viewmodel.currentdate.setTime(currentdate.getTime());

        updateUI();
    }

    private void showOptionDialog() {
        String[] a = {"Today", "Last Week", "Last 30 Days", "Last 60 Days"};
        new MaterialAlertDialogBuilder(getContext())
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        updateUI();
                    }
                }).setSingleChoiceItems(a, getCheckedItem(), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which)
                {
                    case 0:
                        viewmodel.textView = "Today";
                        textView_date.setText(viewmodel.textView);
                        currentdate = Calendar.getInstance();
                        viewmodel.currentdate.setTime(currentdate.getTime());
                        break;
                    case 1:
                        viewmodel.textView = "Last Week";
                        textView_date.setText(viewmodel.textView);
                        break;
                    case 2:
                        viewmodel.textView = "Last 30 Days";
                        textView_date.setText(viewmodel.textView);
                        break;
                    case 3:
                        viewmodel.textView = "Last 60 Days";
                        textView_date.setText(viewmodel.textView);
                        break;
                }
            }
        }).show();
    }

    private void updateUI() {

        switch(textView_date.getText().toString())
        {
            case "Last Week":
                ((MainActivity) getActivity()).openStatisticFragment();
                break;
            case "Last 30 Days":
                ((MainActivity) getActivity()).openStatisticFragment();
                break;
            case "Today":
                viewmodel.textView_date = "Today";
                viewmodel.currentdate = Calendar.getInstance();
                loadDateOption();
                break;
            case "Last 60 Days":
                ((MainActivity) getActivity()).openStatisticFragment();
                break;
            default:
                loadDateOption();
        }
    }

    private void loadDateOption() {

        if(textView_date.getText().toString().isEmpty()) return;

        updateCalodaily(currentdate);
        updateFoodlist(currentdate);
    }

    private void updateCalodaily(Calendar currentdate) {
        fgoal = 0;
        FirebaseDatabase.getInstance().getReference().child("users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("calodaily").child(sdf.format(currentdate.getTime())).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                CaloDaily calo = snapshot.getValue(CaloDaily.class);
                if(calo != null)
                    fgoal = calo.getCalories();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toasty.info(Objects.requireNonNull(getContext()), error.getMessage(),Toasty.LENGTH_SHORT).show();
            }
        });
    }

    private void updateFoodlist(Calendar currentdate) {
        foodateList.clear();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("foodate");

        reference.child(sdf.format(currentdate.getTime())).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren())
                {
                    Foodate foodate = dataSnapshot.getValue(Foodate.class);
                    foodateList.add(foodate);

                }
     //          updategraph()
                updateview();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toasty.info(getContext(), error.getMessage(),Toasty.LENGTH_SHORT).show();
            }
        });
    }

    private void updategraph(float fcaloBreakfast, float fcaloLunch, float fcaloDinner, float fcaloSnacks) {
        piechart.setUsePercentValues(true);
        piechart.getDescription().setEnabled(true);
        piechart.setExtraOffsets(5,10,5,5);
        piechart.setDragDecelerationFrictionCoef(0.95f);

        piechart.setDrawHoleEnabled(true);
        //piechart.setHoleColor(Color.WHITE);
        piechart.setTransparentCircleRadius(61f);

        ArrayList<PieEntry> value = new ArrayList<>();
        if(fcaloBreakfast != 0)
            value.add(new PieEntry(fcaloBreakfast, "Breakfast"));
        if(fcaloLunch != 0)
            value.add(new PieEntry(fcaloLunch, "Lunch"));
        if(fcaloDinner != 0)
            value.add(new PieEntry(fcaloDinner, "Dinner"));
        if(fcaloSnacks != 0)
            value.add(new PieEntry(fcaloSnacks, "Snacks"));

        PieDataSet dataSet = new PieDataSet(value, "");
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);

        PieData pieData = new PieData(dataSet);
        pieData.setValueTextSize(10f);

        piechart.setData(pieData);
        piechart.animateY(1000, Easing.EaseOutCubic);

        updateFoodate();

    }

    private synchronized void updateFoodate() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (foodateList){
                    List<FoodStatic> list = new ArrayList<>();

                    for(int i = 0; i<foodateList.size(); i++)
                    {
                        int x = 0;

                        for(int j = 0; j<list.size(); j++)
                        {

                            if(list.get(j).getNameFood().equals(foodateList.get(i).getNameFood()))
                            {
                                list.get(j).setGram(list.get(j).getGram() + 1);   // update soluong

                                list.get(j).setCalories(list.get(j).getCalories() +
                                        foodateList.get(i).getGram()*foodateList.get(i).getCalories());   // update calo
                                x = 1;
                                break;
                            }
                        }

                        if(x == 0)
                        {
                            list.add(new FoodStatic(foodateList.get(i).getNameFood(),
                                    foodateList.get(i).getGram()* foodateList.get(i).getCalories(), 1));
                        }

                    }

                    //xap xep
                    for(int i = 0; i<list.size() - 1; i++)
                    {
                        for(int j = i + 1; j<list.size(); j++)
                        {
                            if(list.get(j).getGram()>list.get(i).getGram())
                            {
                                list.add(i, list.get(j));
                                list.add(j + 1, list.get(i + 1));

                                list.remove(i+1);
                                list.remove(j+1);
                            }
                        }
                    }


                    FoodateStatisticAdapter foodAdapter = new FoodateStatisticAdapter();
                    try {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                foodAdapter.setData(list);
                                recyclerView.setAdapter(foodAdapter);
                            }
                        });
                    }catch (Exception ignored){

                    }

                }
            }
        }).start();
    }


    private void updateview() {
        ftotalCalories = calculateTotalCalories(foodateList);
        fcaloBreakfast =  calculateTotalCalories(foodateList, "breakfast");
        fcaloLunch =  calculateTotalCalories(foodateList, "lunch");
        fcaloDinner =  calculateTotalCalories(foodateList, "dinner");
        fcaloSnacks =  calculateTotalCalories(foodateList, "snacks");

        updategraph(fcaloBreakfast, fcaloLunch, fcaloDinner, fcaloSnacks);

        float x = (fcaloBreakfast + fcaloLunch + fcaloDinner + fcaloSnacks)* 0.01f;


        totalCalories.setText(String.valueOf((int)ftotalCalories));
        caloBreakfast.setText(String.valueOf((int)fcaloBreakfast));
        caloLunch.setText(String.valueOf((int)fcaloLunch));
        caloDinner.setText(String.valueOf((int)fcaloDinner));
        caloSnacks.setText(String.valueOf((int)fcaloSnacks));

        percentBreakfast.setText(String.valueOf((int)(fcaloBreakfast*100/x)/100f));
        percentLunch.setText(String.valueOf((int)(fcaloLunch*100/x)/100f));
        percentDinner.setText(String.valueOf((int)(fcaloDinner*100/x)/100f));
        percentSnacks.setText(String.valueOf((int)(fcaloSnacks*100/x)/100f));

        Goal.setText(String.valueOf(fgoal));
        float s = ((int)(ftotalCalories*10000/fgoal))/100f;
        averageCalories.setText(String.valueOf(s));


    }

    private void showDateTimeDialog(TextView editText_datetime) {

        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                currentdate.set(Calendar.YEAR, year);
                currentdate.set(Calendar.MONTH, month);
                currentdate.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMM dd");

                textView_date.setText(simpleDateFormat.format(currentdate.getTime()));
                viewmodel.currentdate.setTime(currentdate.getTime());
                updateUI();
            }
        };
        new DatePickerDialog(getContext(), dateSetListener, currentdate.get(Calendar.YEAR),
                currentdate.get(Calendar.MONTH),currentdate.get(Calendar.DAY_OF_MONTH)).show();
    }

    private float calculateTotalCalories(final List<Foodate> foodateList) {
        float total = 0;

        for (int i = 0; i<foodateList.size(); i++)
        {
            total += foodateList.get(i).getCalories()*foodateList.get(i).getGram();
        }

        return total;
    }

    private float calculateTotalCalories(final List<Foodate> foodateList, String sessionofday) {
        float total = 0;

        for (int i = 0; i<foodateList.size(); i++)
        {
            if(foodateList.get(i).getSessionofday().equals(sessionofday))
                total += foodateList.get(i).getCalories()*foodateList.get(i).getGram();
        }
        return total;
    }

    private int getCheckedItem() {
        if(textView_date.getText() == "Today")
            xChecked = 0;
        else if(textView_date.getText() == "Last Week")
            xChecked = 1;
        else if(textView_date.getText() == "Last 30 Days")
            xChecked = 2;
        else if(textView_date.getText() == "Last 60 Days")
            xChecked = 3;

        return xChecked;
    }

    private void anhxa() {

        viewmodel = ((MainActivity) Objects.requireNonNull(getActivity())).viewmodel;
        left = view.findViewById(R.id.imageView_left_substatistic);
        down = view.findViewById(R.id.imageView_down_substatistic);
        right = view.findViewById(R.id.imageView_right__substatistic);
        textView_date = view.findViewById(R.id.textView_substatistic);
        textView_date.setText(viewmodel.textView_date);

        totalCalories = view.findViewById(R.id.totalCalories_substatistic);
        averageCalories = view.findViewById(R.id.averageCalories_substatistic);
        Goal = view.findViewById(R.id.textviewGoal_substatistic);
        piechart = view.findViewById(R.id.piechart_substatistic);

        percentBreakfast = view.findViewById(R.id.percentBreakfast_substatistic);
        percentLunch = view.findViewById(R.id.percentLunch_substatistic);
        percentDinner = view.findViewById(R.id.percentDinner_substatistic);
        percentSnacks = view.findViewById(R.id.percentSnacks_substatistic);
        caloBreakfast = view.findViewById(R.id.caloBreakfast_substatistic);
        caloLunch = view.findViewById(R.id.caloLunch_substatistic);
        caloDinner = view.findViewById(R.id.caloDinner_substatistic);
        caloSnacks = view.findViewById(R.id.caloSnacks_substatistic);

        sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        foodateList = new ArrayList<>();
        currentdate = Calendar.getInstance();
        currentdate.setTime(viewmodel.currentdate.getTime());
        xChecked = 0;
        fgoal = 0;
        fcaloBreakfast = 0;
        fcaloLunch = 0;
        fcaloDinner = 0;
        fcaloSnacks = 0;
        ftotalCalories = 0;

        recyclerView = view.findViewById(R.id.recycleview_foodate_substatistic);

    }
}