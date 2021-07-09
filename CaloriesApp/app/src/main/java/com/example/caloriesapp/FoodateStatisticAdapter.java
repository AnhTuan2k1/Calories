package com.example.caloriesapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.caloriesapp.database.FoodStatic;

import java.util.List;

public class FoodateStatisticAdapter extends RecyclerView.Adapter<FoodateStatisticAdapter.FoodatetimesViewHolder> {
    private List<FoodStatic> mListFood;

    public void setData(List<FoodStatic> list){
        this.mListFood = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public FoodateStatisticAdapter.FoodatetimesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_foodate_statistic, parent, false);

        return new FoodateStatisticAdapter.FoodatetimesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodateStatisticAdapter.FoodatetimesViewHolder holder, int position) {
        FoodStatic food = mListFood.get(position);
        if(food == null) return;

        holder.foodname.setText(String.valueOf(food.getNameFood()));
        holder.times.setText(String.valueOf(food.getGram()));
        holder.cal.setText(String.valueOf( (int)(food.getCalories())));
    }

    @Override
    public int getItemCount() {
        if(mListFood != null)
        {
            return mListFood.size();
        }
        return 0;
    }

    public class FoodatetimesViewHolder extends RecyclerView.ViewHolder{

        private TextView foodname;
        private TextView times;
        private TextView cal;

        public FoodatetimesViewHolder(@NonNull View itemView) {
            super(itemView);

            foodname = itemView.findViewById(R.id.namefoodate_exercise);
            times = itemView.findViewById(R.id.timesfoodate_exercise);
            cal = itemView.findViewById(R.id.calofoodate_exercise);
        }

    }
}
