package com.example.caloriesapp.fragment;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.caloriesapp.CaloDaily;
import com.example.caloriesapp.Exercise;
import com.example.caloriesapp.Foodate;
import com.example.caloriesapp.R;
import com.example.caloriesapp.activities.MainActivity;
import com.example.caloriesapp.viewmodel.FragmentStatisticViewModel;
import com.example.caloriesapp.viewmodel.MainActivityViewModel;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.ChartTouchListener;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.sql.Struct;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import es.dmoral.toasty.Toasty;

public class FragmentStatistic extends Fragment {

    private TextView totalCalories;
    private TextView averageCalories;
    private TextView percentBreakfast;
    private TextView percentLunch;
    private TextView percentDinner;
    private TextView percentSnacks;
    private TextView caloBreakfast;
    private TextView caloLunch;
    private TextView caloDinner;
    private TextView caloSnacks;
    private ImageView imageView;
    private TextView textView;
    private TextView goal;
    private TextView edittext_startdate;
    private TextView edittext_enddate;
    private CheckBox checkbox_showdetail;

    private LineChart lineChart;

    private List<Foodate> foodateList;
    private List<Exercise> exerciseList;
    private List<CaloDaily> caloDailyList;
    private float caloDaily;

    private float ftotalCalories;
    private float fcaloBreakfast;
    private float fcaloLunch;
    private float fcaloDinner;
    private float fcaloSnacks;
    private float fgoal;

    private int xChecked;

    ArrayList<Entry> goalValue;
    ArrayList<Entry> gainValue;
    LineDataSet set1;
    LineDataSet set2;
    LineData lineData;

    private View mView;
    ArrayList<String> stringArrayList;
    SimpleDateFormat sdf;
    private FragmentStatisticViewModel viewmodel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        mView = inflater.inflate(R.layout.fragment_statistic, container, false);
        sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

        anhxa();
     //   if(viewmodel.gainValue.isEmpty() && ftotalCalories == 0)
            syncDataWithFirebase(getcurrentday());
        //else retrieveData();


        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showOptionDialog();
            }
        });
        edittext_enddate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateTimeDialog(edittext_enddate, 2);
            }
        });
        edittext_startdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateTimeDialog(edittext_startdate, 1);
            }
        });
        checkbox_showdetail.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                viewmodel.checkbox_showdetail = isChecked;
                if(isChecked){
                    showDetail();
                }
                else{
                    hideDetail();
                }
            }
        });

        return mView;
    }


    private void showDetail() {
        if(set1 == null || set2 == null) return;

        set1.setDrawCircles(true);
        set1.setValueTextSize(10);
        set1.setDrawValues(true);

        set2.setDrawCircles(true);
        set2.setValueTextSize(10);
        set2.setDrawValues(true);


        lineChart.forceLayout();
    }

    private void hideDetail() {
        if(set2 == null || set1 == null) return;
        set1.setDrawCircles(false);
        set1.setDrawValues(false);

        set2.setDrawCircles(false);
        set2.setDrawValues(false);

        lineChart.forceLayout();
    }

    private void setupgraph(ArrayList<Entry> goalValue, ArrayList<Entry> gainValue) {
        ArrayList<ILineDataSet> dataSets = new ArrayList<>();

        set1 = new LineDataSet(goalValue, "goal");
        set1.setFillAlpha(100);
        set1.setValueTextColor(Color.RED);
        set1.setColor(Color.RED);
        set1.setDrawCircles(false);
        set1.setDrawValues(false);
        set1.setLineWidth(1.5f);
        dataSets.add(set1);

        set2 = new LineDataSet(gainValue, "gain");
        set2.setValueTextColor(Color.BLUE);
        set2.setColor(Color.BLUE);
        set2.setDrawValues(false);
        set2.setLineWidth(1.5f);
        dataSets.add(set2);

        lineData = new LineData(dataSets);
        lineChart.setData(lineData);
        lineChart.setScaleEnabled(true);
        lineChart.getXAxis().setDrawLabels(false);
        lineChart.getAxisRight().setEnabled(false);
        lineChart.getXAxis().setValueFormatter(new MyAxisValueFormatter(stringArrayList));
        lineChart.getXAxis().setDrawLabels(true);

        int size = goalValue.size();
        int s = (int) (fgoal/size);

        int size2 = goalValue.size();
        int s2 = (int) (ftotalCalories/size2);

        try{
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    goal.setText(String.valueOf(s));
                    averageCalories.setText(String.valueOf(s2));
                    lineChart.forceLayout();
                    if(checkbox_showdetail.isChecked())
                    {
                        showDetail();
                    }
                    else {
                        hideDetail();
                    }
                }
            });
        }catch (Exception e)
        {

        }

    }

    static class MyAxisValueFormatter extends ValueFormatter {

        private final ArrayList<String> mValues;

        public MyAxisValueFormatter(ArrayList<String> mValues) {
            this.mValues = mValues;
        }

        @Override
        public String getAxisLabel(float value, AxisBase axis) {
            try{
                super.getAxisLabel(value, axis);
                return mValues.get((int)value);
            }catch (Exception exception)
            {
                return super.getAxisLabel(value, axis);
            }
        }
    }

    private void showOptionDialog() {
        String[] a = {"7 days ago", "30 days ago", "today", "60 days ago"};
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
                        viewmodel.textView = "7 days ago";
                        textView.setText(viewmodel.textView);
                        break;
                    case 1:
                        viewmodel.textView = "30 days ago";
                        textView.setText(viewmodel.textView);
                        break;
                    case 2:
                        viewmodel.textView = "today";
                        textView.setText(viewmodel.textView);
                        break;
                    case 3:
                        viewmodel.textView = "60 days ago";
                        textView.setText(viewmodel.textView);
                        break;
                }
            }
        }).show();
    }

    private int getCheckedItem() {
        if(textView.getText() == "7 days ago")
            xChecked = 0;
        else if(textView.getText() == "30 days ago")
            xChecked = 1;
        else if(textView.getText() == "today")
            xChecked = 2;
        else if(textView.getText() == "60 days ago")
            xChecked = 3;

        return xChecked;
    }

    private void syncDataWithFirebase(String date) {
        updateUI();
        //updateExercise(date);
        //updateCaloDaily(date);
    }

    private void updateUI() {

        switch(textView.getText().toString())
        {
            case "7 days ago":
                load7daysago();
                viewmodel.edittext_enddate = "";
                viewmodel.edittext_startdate = "";
                edittext_startdate.setText(viewmodel.edittext_startdate);
                edittext_enddate.setText(viewmodel.edittext_enddate);
                break;
            case "30 days ago":
                loadamonthago();
                viewmodel.edittext_enddate = "";
                viewmodel.edittext_startdate = "";
                edittext_startdate.setText(viewmodel.edittext_startdate);
                edittext_enddate.setText(viewmodel.edittext_enddate);
                break;
            case "today":
                loadtoday();
                viewmodel.edittext_enddate = "";
                viewmodel.edittext_startdate = "";
                edittext_startdate.setText(viewmodel.edittext_startdate);
                edittext_enddate.setText(viewmodel.edittext_enddate);
                break;
            case "60 days ago":
                load2monthago();
                viewmodel.edittext_enddate = "";
                viewmodel.edittext_startdate = "";
                edittext_startdate.setText(viewmodel.edittext_startdate);
                edittext_enddate.setText(viewmodel.edittext_enddate);
                break;
            default:
                loadDateOption(edittext_startdate.getText().toString(), edittext_enddate.getText().toString());
        }
    }


    private void loadDateOption(final String start,final String end) {

        if(start == null || end == null || start.equals("") || end.equals(""))
            return;

        Calendar startdate = Calendar.getInstance();
        Calendar enddate = Calendar.getInstance();
        Calendar loopdate = Calendar.getInstance();
        Calendar loopdate2 = Calendar.getInstance();

        try {
            startdate.setTime(sdf.parse(start));
            loopdate.setTime(sdf.parse(start));
            loopdate2.setTime(sdf.parse(start));
            enddate.setTime(sdf.parse(end));
        } catch (ParseException e) {
            e.printStackTrace();
            return;
        }

        if(comparedate(startdate, enddate) == 1){
            return;
        }
        else if(comparedate(startdate, enddate) == 0)
        {
            viewmodel.textView = "today";
            textView.setText(viewmodel.textView);
            loadtoday();
        }
        else{
            updateCaloDailyList(startdate, enddate, loopdate);
            updateFoodList(startdate, enddate, loopdate2);
        }



    }

    private void updateCaloDailyList(Calendar startdate, Calendar enddate, Calendar loopdate) {
        caloDailyList.clear();
        while (comparedate(loopdate, enddate) < 1)
        {
            FirebaseDatabase.getInstance().getReference().child("users")
                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .child("calodaily").child(sdf.format(loopdate.getTime())).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    CaloDaily caloDaily = snapshot.getValue(CaloDaily.class);
                    if(caloDaily != null)
                        caloDailyList.add(caloDaily);

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toasty.info(getContext(), error.getMessage(),Toasty.LENGTH_SHORT).show();
                }
            });

            loopdate.add(Calendar.DAY_OF_MONTH, 1);
        }
    }

    // and update UI chart
    private void updateFoodList(Calendar startdate, Calendar enddate, Calendar loopdate) {
        foodateList.clear();
//        int xx = loopdate.get(Calendar.DAY_OF_MONTH);
//        int yy = enddate.get(Calendar.DAY_OF_MONTH);
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("foodate");
        while (comparedate(loopdate, enddate) < 0)
        {
            String date = sdf.format(loopdate.getTime());
            reference.child(date).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for(DataSnapshot dataSnapshot : snapshot.getChildren())
                    {
                        Foodate foodate = dataSnapshot.getValue(Foodate.class);
                            foodateList.add(foodate);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toasty.info(getContext(), error.getMessage(),Toasty.LENGTH_SHORT).show();
                }
            });
            loopdate.add(Calendar.DAY_OF_MONTH, 1);
        }

        int x = loopdate.get(Calendar.DAY_OF_MONTH);
        int y = enddate.get(Calendar.DAY_OF_MONTH);
        //final data
        if(x == y)
        {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            reference.child(sdf.format(loopdate.getTime())).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for(DataSnapshot dataSnapshot : snapshot.getChildren())
                    {
                        Foodate foodate = dataSnapshot.getValue(Foodate.class);
                            foodateList.add(foodate);

                    }

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            updategraph(foodateList, startdate, enddate);
                        }
                    }).start();

                    updateview();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toasty.info(getContext(), error.getMessage(),Toasty.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void updateview() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                ftotalCalories = calculateTotalCalories(foodateList);
                fcaloBreakfast =  calculateTotalCalories(foodateList, "breakfast");
                fcaloLunch =  calculateTotalCalories(foodateList, "lunch");
                fcaloDinner =  calculateTotalCalories(foodateList, "dinner");
                fcaloSnacks =  calculateTotalCalories(foodateList, "snacks");

                float x = (fcaloBreakfast + fcaloLunch + fcaloDinner + fcaloSnacks)* 0.01f;

                try {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            totalCalories.setText(String.valueOf((int)ftotalCalories));
                            caloBreakfast.setText(String.valueOf((int)fcaloBreakfast));
                            caloLunch.setText(String.valueOf((int)fcaloLunch));
                            caloDinner.setText(String.valueOf((int)fcaloDinner));
                            caloSnacks.setText(String.valueOf((int)fcaloSnacks));

                            percentBreakfast.setText(String.valueOf((int)(fcaloBreakfast*100/x)/100f));
                            percentLunch.setText(String.valueOf((int)(fcaloLunch*100/x)/100f));
                            percentDinner.setText(String.valueOf((int)(fcaloDinner*100/x)/100f));
                            percentSnacks.setText(String.valueOf((int)(fcaloSnacks*100/x)/100f));
                        }
                    });
                }catch (Exception exception){
                }
            }
        }).start();
    }

    private void updategraph(final List<Foodate> foodateList, Calendar startdate, Calendar enddate) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMM dd", Locale.getDefault());
        gainValue.clear();
        stringArrayList.clear();

        Calendar loopdate = Calendar.getInstance();
        loopdate.setTime(startdate.getTime());

        int x = 0;
        while (comparedate(loopdate, enddate) < 1)
        {
            stringArrayList.add(simpleDateFormat.format(loopdate.getTime()));
            float y = 0;
            for(int i = 0; i<foodateList.size(); i++)
            {
                if(sdf.format(loopdate.getTime()).equals(foodateList.get(i).getDate()))
                {
                    y += foodateList.get(i).getCalories()*foodateList.get(i).getGram();
                }
            }

            gainValue.add(new Entry(x, y));

            x++;
            loopdate.add(Calendar.DAY_OF_MONTH, 1);
        }

        updategoalValue(startdate, enddate);
        setupgraph(goalValue, gainValue);
    }

    private void updategoalValue(Calendar startdate, Calendar enddate) {

        fgoal = 0;
        goalValue.clear();
        Calendar loopdate = Calendar.getInstance();
        loopdate.setTime(startdate.getTime());

        int x = 0;
        while (comparedate(loopdate, enddate) < 1)
        {

            float y = 0;
            for(int i = 0; i<caloDailyList.size(); i++)
            {
                if(sdf.format(loopdate.getTime()).equals(caloDailyList.get(i).getDate()))
                {
                    y = caloDailyList.get(i).getCalories();
                    break;
                }
            }

            fgoal += y;
            goalValue.add(new Entry(x, y));

            x++;
            loopdate.add(Calendar.DAY_OF_MONTH, 1);
        }
    }

    private int convertcalendartoint(Calendar calendar) {

        int day = calendar.get(Calendar.DAY_OF_MONTH);

        int month = calendar.get(Calendar.MONTH) + 1;

        int year = calendar.get(Calendar.YEAR) - 2000;
        int date = year*10000 + month*100 + day;

//        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());
//        Calendar startdate = Calendar.getInstance();
//        try {
//            startdate.setTime(sdf.parse("20210625"));
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//        Toasty.info(getApplicationContext(),sdf.format(startdate.getTime()), Toast.LENGTH_LONG).show();

        return date;
    }

    private void loadtoday() {

    }

    private void loadamonthago() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, -30);
        loadDateOption(sdf.format(calendar.getTime()), sdf.format(Calendar.getInstance().getTime()));
    }

    private void load2monthago() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, -60);
        loadDateOption(sdf.format(calendar.getTime()), sdf.format(Calendar.getInstance().getTime()));
    }

    private void load7daysago() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, -7);

        loadDateOption(sdf.format(calendar.getTime()), sdf.format(Calendar.getInstance().getTime()));
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

    private void updateExercise(String date) {
        FirebaseDatabase.getInstance().getReference().child("users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("exercise").child(date).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren())
                {
                    Exercise exercise = dataSnapshot.getValue(Exercise.class);
                    exerciseList.add(exercise);
                }

                // update ui here with exerciseList
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toasty.info(getContext(), error.getMessage(),Toasty.LENGTH_SHORT).show();
            }
        });
    }

    private void updateCaloDaily(String date) {
        FirebaseDatabase.getInstance().getReference().child("users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("calodaily").child(date).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.getValue(CaloDaily.class) != null)
                {
                    caloDaily = snapshot.getValue(CaloDaily.class).getCalories();

                    // update ui here with caloDaily
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toasty.info(getContext(), error.getMessage(),Toasty.LENGTH_SHORT).show();
            }
        });
    }

    private int comparedate(Calendar loopdate, Calendar enddate) {
        if(loopdate.get(Calendar.YEAR) < enddate.get(Calendar.YEAR))
        {
            return -1;
        }
        else if(loopdate.get(Calendar.YEAR) == enddate.get(Calendar.YEAR))
        {
            if(loopdate.get(Calendar.MONTH) < enddate.get(Calendar.MONTH))
            {
                return -1;
            }
            else if(loopdate.get(Calendar.MONTH) == enddate.get(Calendar.MONTH))
            {
                if(loopdate.get(Calendar.DAY_OF_MONTH) < enddate.get(Calendar.DAY_OF_MONTH))
                {
                    return -1;
                }
                else if(loopdate.get(Calendar.DAY_OF_MONTH) == enddate.get(Calendar.DAY_OF_MONTH))
                {
                    return 0;
                }
                else{
                    return 1;
                }
            }
            else{
                return 1;
            }
        }
        else {
            return 1;
        }
    }

    private String getcurrentday() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String date = sdf.format(calendar.getTime());
        return date;
    }

    private void showDateTimeDialog(TextView editText_datetime, int x) {
        final Calendar cld = Calendar.getInstance();

        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                cld.set(Calendar.YEAR, year);
                cld.set(Calendar.MONTH, month);
                cld.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");


                if(x == 1)
                {
                    viewmodel.edittext_startdate = simpleDateFormat.format(cld.getTime());
                    editText_datetime.setText(viewmodel.edittext_startdate);
                }
                else if(x == 2)
                {
                    viewmodel.edittext_enddate = simpleDateFormat.format(cld.getTime());
                    editText_datetime.setText(viewmodel.edittext_enddate);
                }


                String s = "From:  " + edittext_startdate.getText().toString() +
                        "   To:  " + edittext_enddate.getText().toString();
                viewmodel.textView = s;
                textView.setText(viewmodel.textView);
                updateUI();
            }
        };
        new DatePickerDialog(getContext(), dateSetListener, cld.get(Calendar.YEAR),
                cld.get(Calendar.MONTH),cld.get(Calendar.DAY_OF_MONTH)).show();
    }


    private void anhxa() {
        viewmodel = new ViewModelProvider(this,
                new ViewModelProvider.AndroidViewModelFactory(getActivity().getApplication())).get(FragmentStatisticViewModel.class);

        totalCalories = mView.findViewById(R.id.totalCalories_statistic);
        averageCalories = mView.findViewById(R.id.averageCalories_statistic);
        percentBreakfast = mView.findViewById(R.id.percentBreakfast_statistic);
        percentLunch = mView.findViewById(R.id.percentLunch_statistic);
        percentDinner = mView.findViewById(R.id.percentDinner_statistic);
        percentSnacks = mView.findViewById(R.id.percentSnacks_statistic);
        caloBreakfast = mView.findViewById(R.id.caloBreakfast_statistic);
        caloLunch = mView.findViewById(R.id.caloLunch_statistic);
        caloDinner = mView.findViewById(R.id.caloDinner_statistic);
        caloSnacks = mView.findViewById(R.id.caloSnacks_statistic);
        imageView = mView.findViewById(R.id.imageView_statistic);
        textView = mView.findViewById(R.id.textView_statistic);
        textView.setText(viewmodel.textView);                                                    //viewmodel
        goal = mView.findViewById(R.id.textviewGoal_statistic);
        edittext_enddate = mView.findViewById(R.id.edittext_enddate_statistic);
        edittext_enddate.setText(viewmodel.edittext_enddate);                                    //viewmodel
        edittext_startdate = mView.findViewById(R.id.edittext_startdate_statistic);
        edittext_startdate.setText(viewmodel.edittext_startdate);                               //viewmodel
        checkbox_showdetail = mView.findViewById(R.id.checkbox_showdetail_statistic);
        checkbox_showdetail.setChecked(viewmodel.checkbox_showdetail);                          //viewmodel

        lineChart = mView.findViewById(R.id.linechart_statistic);
        goalValue = new ArrayList<>();
        gainValue = new ArrayList<>();

        foodateList = new ArrayList<>();
        exerciseList = new ArrayList<>();
        caloDailyList = new ArrayList<>();
        stringArrayList = new ArrayList<>();
        caloDaily = 0;
        fcaloBreakfast = 0;
        fcaloLunch = 0;
        fcaloDinner = 0;
        fcaloSnacks = 0;
        ftotalCalories = 0;
        fgoal = 0;
        xChecked = 0;
    }
}