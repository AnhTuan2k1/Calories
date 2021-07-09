package com.example.caloriesapp.viewmodel;

import androidx.lifecycle.ViewModel;

import com.example.caloriesapp.R;


public class FragmentStatisticViewModel extends ViewModel {
    public String edittext_startdate;
    public String edittext_enddate;
    public String textView;
    public boolean checkbox_showdetail;
    public boolean checkbox_showGoalline;
    public boolean checkbox_showGainline;
    public boolean checkbox_showBurnline;
    public boolean checkbox_showGainBurnline;

    public FragmentStatisticViewModel() {
        this.edittext_startdate = "";
        this.edittext_enddate = "";
        this.textView = "Last Week";
        this.checkbox_showdetail = false;
        this.checkbox_showGoalline = true;
        this.checkbox_showGainline = true;
        this.checkbox_showBurnline = false;
        this.checkbox_showGainBurnline = false;
    }

}
