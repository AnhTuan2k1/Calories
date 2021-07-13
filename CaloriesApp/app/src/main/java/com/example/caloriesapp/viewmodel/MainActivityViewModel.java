package com.example.caloriesapp.viewmodel;

import androidx.lifecycle.ViewModel;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class MainActivityViewModel extends ViewModel {
    public int currentFragment;
    public String edittext_startdate;
    public String edittext_enddate;
    public String textView;
    public boolean checkbox_showdetail;
    public boolean checkbox_showGoalline;
    public boolean checkbox_showGainline;
    public boolean checkbox_showBurnline;
    public boolean checkbox_showGainBurnline;

    public String textView_date;
    public Calendar currentdate;

    public String string_datenow;

    public MainActivityViewModel() {
        this.currentFragment = 0;
        this.edittext_startdate = "";
        this.edittext_enddate = "";
        this.textView = "Last Week";
        this.checkbox_showdetail = false;
        this.checkbox_showGoalline = true;
        this.checkbox_showGainline = true;
        this.checkbox_showBurnline = true;
        this.checkbox_showGainBurnline = false;
        textView_date = "Today";
        currentdate = Calendar.getInstance();
        string_datenow = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                .format(currentdate.getTime());
    }
}
