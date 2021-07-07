package com.example.caloriesapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.caloriesapp.database.FoodStatic;

import java.util.List;

public class FoodateAdapter extends RecyclerView.Adapter<FoodateAdapter.FoodateViewHolder> {


    private List<Foodate> foodateList;


    @NonNull
    @Override
    public FoodateViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_foodate_recycleview,parent,false);

        return  new FoodateViewHolder(view);
    }

    public void setData(List<Foodate> list){
        this.foodateList = list;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull FoodateViewHolder holder, int position) {
        Foodate food = foodateList.get(position);
        if(food == null) return;

        holder.foodname.setText(String.valueOf(food.getNameFood()));
        holder.inforfood.setText(String.valueOf(food.getGram()));
        holder.cal.setText(String.valueOf( (int)(food.getCalories()*food.getGram()) ));



        boolean isExpanded = foodateList.get(position).isExpanded();
        holder.expandableLayout.setVisibility(isExpanded ? View.VISIBLE : View.GONE);


    }

    @Override
    public int getItemCount() {
        if(foodateList != null)
        {
            return foodateList.size();
        }
        return 0;
    }

    public class FoodateViewHolder extends RecyclerView.ViewHolder{
        private TextView foodname;
        private TextView inforfood;
        private TextView cal;
        private ConstraintLayout expandableLayout;
        public FoodateViewHolder(@NonNull View itemView) {
            super(itemView);

            foodname = itemView.findViewById(R.id.text_nameFood);
            inforfood = itemView.findViewById(R.id.text_gram);
            cal = itemView.findViewById(R.id.text_calo);
            expandableLayout = itemView.findViewById(R.id.expandable_layout);

            foodname.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Foodate foodate =  foodateList.get(getBindingAdapterPosition());
                    foodate.setExpanded(!foodate.isExpanded());
                    notifyItemChanged(getBindingAdapterPosition());
                }
            });
        }
    }
}
