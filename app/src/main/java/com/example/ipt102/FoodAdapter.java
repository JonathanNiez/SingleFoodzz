package com.example.ipt102;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.FoodViewHolder>{

    Context context;
    ArrayList<Foods> foodsArrayList;

    private OnItemClickListener mListener;

    public interface OnItemClickListener{
        void onItemClick(int position);
        void onOrderClick(int position);

    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }


    static class FoodViewHolder extends RecyclerView.ViewHolder {

        public TextView foodName, foodPrice, foodID;
        public ImageView foodImage;
        public Button orderButton;

        public FoodViewHolder(@NonNull View itemView, OnItemClickListener listener) {
            super(itemView);

            foodName = itemView.findViewById(R.id.foodName);
            foodPrice = itemView.findViewById(R.id.foodPrice);
            foodImage = itemView.findViewById(R.id.foodImage);
            orderButton = itemView.findViewById(R.id.orderButton);
            foodID = itemView.findViewById(R.id.foodID);

            itemView.setOnClickListener(v -> {
                if (listener != null){
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION){
                        listener.onItemClick(position);
                    }
                }
            });

            orderButton.setOnClickListener(v -> {
                if (listener != null){
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION){
                        listener.onOrderClick(position);
                    }
                }
            });
        }
    }
    public FoodAdapter(Context context ,ArrayList<Foods> foodsArrayList) {
        this.foodsArrayList = foodsArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.foods, parent, false);
        return new FoodViewHolder(view, mListener);

    }

    @Override
    public void onBindViewHolder(@NonNull FoodViewHolder holder, int position) {
        Foods foods = foodsArrayList.get(position);
        holder.foodName.setText(foods.getFoodName());
        holder.foodPrice.setText("₱" + (foods.getFoodPrice()));
        holder.foodID.setText(String.valueOf(foods.getFoodID()));
//        holder.foodImage.setImageResource(foods.getFoodImage());

        Glide.with(context).load(foods.getFoodImage()).into(holder.foodImage);
    }

    @Override
    public int getItemCount() {
//        return foodsArrayList == null ? 0 : foodsArrayList.size();
        return foodsArrayList.size();
    }
}
