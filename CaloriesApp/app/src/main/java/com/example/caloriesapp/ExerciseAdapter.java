package com.example.caloriesapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.caloriesapp.database.FoodStatic;

import java.util.List;

public class ExerciseAdapter extends RecyclerView.Adapter<ExerciseAdapter.ExerciseViewHolder> {
    private List<Exercise> mListExercise;

    private OnRecycleViewClickListener listener;
    public interface OnRecycleViewClickListener{
        void OnItemClick(int position);
    }
    public void OnRecycleViewClickListener(OnRecycleViewClickListener listener)
    {
        this.listener = listener;
    }

    public void setData(List<Exercise> list){
        this.mListExercise = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ExerciseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_exercise_recycleview, parent, false);

        return new ExerciseViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull ExerciseViewHolder holder, int position) {
        Exercise exercise = mListExercise.get(position);
        if(exercise == null) return;

        holder.exercisename.setText(String.valueOf(exercise.getNameExercise()));
    }

    @Override
    public int getItemCount() {
        if(mListExercise != null)
        {
            return mListExercise.size();
        }
        return 0;
    }

    public class ExerciseViewHolder extends RecyclerView.ViewHolder{

        private TextView exercisename;

        public ExerciseViewHolder(@NonNull View itemView, OnRecycleViewClickListener listener) {
            super(itemView);

            exercisename = itemView.findViewById(R.id.textView_nameExercise_itemExerciseLayout);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener != null && getAbsoluteAdapterPosition() != RecyclerView.NO_POSITION)
                    {
                        listener.OnItemClick(getAbsoluteAdapterPosition());
                    }
                }
            });
        }
















    }
}
