package com.example.ipt102;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

public class ProcessingOrder extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_processing_order);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Bundle bundle = getIntent().getExtras();
        String getFoodName = bundle.getString("foodName");
        String getPrice = bundle.getString("foodPrice");
        String getFoodImage = bundle.getString("foodImage");
        String getQuantity = bundle.getString("foodQuantity");
        String getUserID = bundle.getString("userID");
        String getOrderID = bundle.getString("orderID");

        new Handler().postDelayed(() -> {
            Bundle sendToFoodOnTheWay = new Bundle();
            sendToFoodOnTheWay.putString("foodName", getFoodName);
            sendToFoodOnTheWay.putString("foodPrice", getPrice);
            sendToFoodOnTheWay.putString("foodImage", getFoodImage);
            sendToFoodOnTheWay.putString("foodQuantity", getQuantity);
            sendToFoodOnTheWay.putString("userID", getUserID);
            sendToFoodOnTheWay.putString("orderID", getOrderID);

            Intent intent = new Intent(this, FoodOnTheWay.class);
            intent.putExtras(sendToFoodOnTheWay);
            startActivity(intent);
            finish();
        }, 4000);
    }

    @Override
    public void onBackPressed() {

    }
}