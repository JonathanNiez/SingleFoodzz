package com.example.ipt102;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class FoodOrderedAdapter extends RecyclerView.Adapter<FoodOrderedAdapter.FoodOrderedHolder>{

    Context context;
    ArrayList<FoodOrdered> foodOrderedArrayList;

    private OnItemClickListener mListener;

    public interface OnItemClickListener{
        void onItemClick(int position);
        void onCancelClick(int position);

    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }


    static class FoodOrderedHolder extends RecyclerView.ViewHolder {

        public TextView foodName, foodPrice, foodQuantity, foodOrderID;
        public ImageView foodImage;
        public Button cancelOrderButton;

        public FoodOrderedHolder(@NonNull View itemView, FoodOrderedAdapter.OnItemClickListener listener) {
            super(itemView);

            foodName = itemView.findViewById(R.id.foodName);
            foodPrice = itemView.findViewById(R.id.foodPrice);
            foodImage = itemView.findViewById(R.id.foodImage);
            foodQuantity = itemView.findViewById(R.id.foodQuantity);
            foodOrderID = itemView.findViewById(R.id.foodOrderID);
            cancelOrderButton = itemView.findViewById(R.id.cancelOrderButton);

            itemView.setOnClickListener(v -> {
                if (listener != null){
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION){
                        listener.onItemClick(position);
                    }
                }
            });

            cancelOrderButton.setOnClickListener(v -> {
                if (listener != null){
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION){
                        listener.onCancelClick(position);
                    }
                }
            });
        }
    }
    public FoodOrderedAdapter(Context context ,ArrayList<FoodOrdered> foodOrderedArrayList) {
        this.foodOrderedArrayList = foodOrderedArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public FoodOrderedHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.food_ordered, parent, false);
        return new FoodOrderedHolder(view, mListener);

    }

    @Override
    public void onBindViewHolder(@NonNull FoodOrderedHolder holder, int position) {
        FoodOrdered foodOrdered = foodOrderedArrayList.get(position);
        holder.foodQuantity.setText("Quantity: " + foodOrdered.getFoodQuantity());
        holder.foodName.setText(foodOrdered.getFoodName());
        holder.foodPrice.setText("₱" + foodOrdered.getFoodPrice());
        Glide.with(context).load(foodOrdered.getFoodImage()).into(holder.foodImage);
    }

    @Override
    public int getItemCount() {
//        return foodsArrayList == null ? 0 : foodsArrayList.size();
        return foodOrderedArrayList.size();
    }

}
