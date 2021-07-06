package com.example.caloriesapp.fragment;

import android.content.DialogInterface;
import android.media.Image;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.caloriesapp.R;
import com.example.caloriesapp.activities.MainActivity;
import com.example.caloriesapp.viewmodel.FragmentStatisticViewModel;
import com.github.mikephil.charting.charts.PieChart;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.Calendar;


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
//    private FragmentStatisticViewModel viewmodel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_sub_statistic, container, false);
        anhxa();
        down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showOptionDialog();
            }
        });

        return view;
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
//                        viewmodel.textView = "Today";
                        textView_date.setText("Today");
                        break;
                    case 1:
//                        viewmodel.textView = "Last Week";
                        textView_date.setText("Last Week");
                        break;
                    case 2:
//                        viewmodel.textView = "Last 30 Days";
                        textView_date.setText("Last 30 Days");
                        break;
                    case 3:
//                        viewmodel.textView = "Last 60 Days";
                        textView_date.setText("Last 60 Days");
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
                currentdate = Calendar.getInstance();
                loadDateOption(currentdate);
                break;
            case "Last 60 Days":
                ((MainActivity) getActivity()).openStatisticFragment();
                break;
            default:
                loadDateOption(currentdate);
        }
    }

    private void loadDateOption(Calendar currentdate) {

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

//        viewmodel = new ViewModelProvider(this,
//                new ViewModelProvider.AndroidViewModelFactory(getActivity().getApplication())).get(FragmentStatisticViewModel.class);

        left = view.findViewById(R.id.imageView_left_substatistic);
        down = view.findViewById(R.id.imageView_down_substatistic);
        right = view.findViewById(R.id.imageView_right__substatistic);
        textView_date = view.findViewById(R.id.textView_substatistic);

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

        currentdate = Calendar.getInstance();
        xChecked = 0;

    }
}