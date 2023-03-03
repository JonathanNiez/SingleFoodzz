package com.example.ipt102;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class FoodOnTheWay extends AppCompatActivity {

    Button homeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_on_the_way);

        Bundle bundle = getIntent().getExtras();
        String getFoodName = bundle.getString("foodName");
        String getPrice = bundle.getString("foodPrice");
        String getFoodImage = bundle.getString("foodImage");
        String getFoodQuantity = bundle.getString("foodQuantity");
        String getUserID = bundle.getString("userID");
        String getOrderID = bundle.getString("orderID");

        homeButton = findViewById(R.id.homeButton);

        homeButton.setOnClickListener(v -> {
            Bundle sendToOrdersActivity = new Bundle();
            sendToOrdersActivity.putString("foodName", getFoodName);
            sendToOrdersActivity.putString("foodPrice", getPrice);
            sendToOrdersActivity.putString("foodImage", getFoodImage);
            sendToOrdersActivity.putString("foodQuantity", getFoodQuantity);
            sendToOrdersActivity.putString("userID", getUserID);
            sendToOrdersActivity.putString("orderID", getOrderID);

            Intent intent = new Intent(this, OrdersActivity.class);
            intent.putExtras(sendToOrdersActivity);
            startActivity(intent);
            finish();
        });
    }

    @Override
    public void onBackPressed() {

    }

}