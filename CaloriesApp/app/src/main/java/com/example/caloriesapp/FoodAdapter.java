package com.example.caloriesapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.caloriesapp.database.FoodStatic;

import java.util.List;

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.FoodViewHolder> {
    private List<FoodStatic> mListFood;

    public void setData(List<FoodStatic> list){
        this.mListFood = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_food_recycleview, parent, false);

        return new FoodViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodViewHolder holder, int position) {
        FoodStatic food = mListFood.get(position);
        if(food == null) return;

        holder.foodname.setText(String.valueOf(food.getNameFood()));
        holder.inforfood.setText("regular: " + String.valueOf(food.getGram() + " g"));
        holder.cal.setText(String.valueOf( (int)(food.getCalories()*food.getGram())) + " Cal");
    }

    @Override
    public int getItemCount() {
        if(mListFood != null)
        {
            return mListFood.size();
        }
        return 0;
    }

    public class FoodViewHolder extends RecyclerView.ViewHolder{

        private TextView foodname;
        private TextView inforfood;
        private TextView cal;

        public FoodViewHolder(@NonNull View itemView) {
            super(itemView);

            foodname = itemView.findViewById(R.id.textView_nameFood_itemFoodLayout);
            inforfood = itemView.findViewById(R.id.textView_infoFood_itemFoodLayout);
            cal = itemView.findViewById(R.id.textView_Cal_itemFoodLayout);
        }
















    }
}
