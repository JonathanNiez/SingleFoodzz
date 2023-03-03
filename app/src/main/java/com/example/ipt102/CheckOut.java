package com.example.ipt102;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class CheckOut extends AppCompatActivity {

    TextView price, foodName, textViewTotal, quantityTextView;
    Button checkOutButton, cancelButton, addButton, minusButton, cancelBtnDialog, confirmBtnDialog;
    ImageView foodImageCheckOut;
    ProgressBar progressBarCheckOut;

    //Local host
    String LocalHostURlQty = "http://192.168.1.149/IPT102_Project/updateFoodQuantityBought.php";
    String InsertOrderLocal = "http://192.168.1.149/IPT102_Project/insertOrders.php";

    //Web host
    String URlQty = "https://jonathanniez.000webhostapp.com/IPT102_Project/updateFoodQuantityBought.php";
    String InsertOrder = "https://jonathanniez.000webhostapp.com/IPT102_Project/insertOrders.php";
    int quantity = 1;
    Dialog confirmOrderDialog;
    ArrayList<FoodOrder> foodOrderArrayList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_out);

        confirmOrderDialog = new Dialog(CheckOut.this);
        confirmOrderDialog.setContentView(R.layout.confirm_order);
        confirmOrderDialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.dialog_background));
        confirmOrderDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        confirmOrderDialog.setCancelable(false);
        confirmOrderDialog.getWindow().getAttributes().windowAnimations = R.style.animation;

        cancelBtnDialog = confirmOrderDialog.findViewById(R.id.cancelBtnDialog);
        confirmBtnDialog = confirmOrderDialog.findViewById(R.id.confirmBtnDialog);
        price = findViewById(R.id.priceTextView);
        foodName = findViewById(R.id.foodNameTextView);
        checkOutButton = findViewById(R.id.checkOutButton);
        textViewTotal = findViewById(R.id.textViewTotal);
        quantityTextView = findViewById(R.id.quantityTextView);
        progressBarCheckOut = findViewById(R.id.progressBarCheckOut);
        cancelButton = findViewById(R.id.cancelButton);
        addButton = findViewById(R.id.addButton);
        minusButton = findViewById(R.id.minusButton);
        foodImageCheckOut = findViewById(R.id.foodImageCheckOut);

        Bundle bundle = getIntent().getExtras();
        String getFoodName = bundle.getString("foodName");
        String getPrice = bundle.getString("foodPrice");
        String getFoodImage = bundle.getString("foodImage");
        String getUserID = bundle.getString("userID");
        String getFoodID = bundle.getString("foodID");

        cancelBtnDialog.setOnClickListener(v -> {
            confirmOrderDialog.dismiss();
        });

        confirmBtnDialog.setOnClickListener(v -> {

        });


        double fPrice = Double.parseDouble(getPrice);
        double deliveryFee = 50;
        double singleItemTotal = fPrice * 1;
        quantityTextView.setText(String.valueOf(quantity));

        checkOut(getFoodName, getPrice, getFoodImage, quantity, Integer.parseInt(getUserID), singleItemTotal, Integer.parseInt(getFoodID));

        minusButton.setOnClickListener(v -> {
            if (quantity < 1) {
                quantity = 0;

                quantityTextView.setText(String.valueOf(quantity));
                Log.i("Minus Button", "");
            } else {
                quantity--;
                double total = fPrice * quantity;

                checkOut(getFoodName, String.valueOf(total), getFoodImage, quantity, Integer.parseInt(getUserID), total, Integer.parseInt(getFoodID));

                quantityTextView.setText(String.valueOf(quantity));
                textViewTotal.setText("₱" + String.valueOf(total) + "+" + " " + "₱" + deliveryFee);
            }
        });

        addButton.setOnClickListener(v -> {
            quantity++;

            if (quantity == 20) {
                quantity = 20;
                double total = fPrice * quantity;

                checkOut(getFoodName, String.valueOf(total), getFoodImage, quantity, Integer.parseInt(getUserID), total, Integer.parseInt(getFoodID));

                Log.i("Plus Button", "");

                quantityTextView.setText(String.valueOf(quantity));
                textViewTotal.setText("₱" + String.valueOf(total) + "+" + " " + "₱" + deliveryFee);
            } else {
                double total = fPrice * quantity;

                checkOut(getFoodName, String.valueOf(total), getFoodImage, quantity, Integer.parseInt(getUserID), total, Integer.parseInt(getFoodID));

                quantityTextView.setText(String.valueOf(quantity));
                textViewTotal.setText("₱" + String.valueOf(total) + "+" + " " + "₱" + deliveryFee);
            }
        });

        foodName.setText(getFoodName);
        price.setText("₱" + getPrice);
        textViewTotal.setText("₱" + getPrice + " " + "+" + " " + "₱" + deliveryFee);
        Glide.with(this).load(getFoodImage).into(foodImageCheckOut);

        cancelButton.setOnClickListener(v -> {
            finish();
        });

    }

    Bundle sendToProcessingOrder = new Bundle();

    public void checkOut(String xFoodName, String xPrice, String xFoodImage, int xQuantity, int xUserID, double xTotal, int xFoodID) {

        checkOutButton.setOnClickListener(v -> {


            checkOutButton.setVisibility(View.GONE);
            progressBarCheckOut.setVisibility(View.VISIBLE);

            sendToProcessingOrder.putString("foodName", xFoodName);
            sendToProcessingOrder.putString("foodPrice", xPrice);
            sendToProcessingOrder.putString("foodImage", xFoodImage);
            sendToProcessingOrder.putString("foodQuantity", String.valueOf(xQuantity));
            sendToProcessingOrder.putString("userID", String.valueOf(xUserID));


            //Update Quantity
            StringRequest stringRequest = new StringRequest(Request.Method.POST, URlQty, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String result = jsonObject.getString("success");
                        if (result.equals("2")) {
                            Log.i("Check Out", "Out of Stock" + result);
                        } else {

                            if (result.equals("1")) {
                                Log.i("Check Out", "Order Success " + result);
                                checkOutButton.setVisibility(View.VISIBLE);
                                progressBarCheckOut.setVisibility(View.GONE);


                                insertFoodOrdered(xFoodImage, xFoodName ,xUserID, xQuantity, xTotal, xFoodID);

                            } else if (result.equals("0")) {
                                Toast.makeText(CheckOut.this, "Order Failed", Toast.LENGTH_SHORT).show();
                                Log.e("Check Out", "Order Failed " + result);
                                checkOutButton.setVisibility(View.VISIBLE);
                                progressBarCheckOut.setVisibility(View.GONE);

                            }
                        }

                    } catch (JSONException e) {
                        Log.e("JSON", e.getMessage());
                        checkOutButton.setVisibility(View.VISIBLE);
                        progressBarCheckOut.setVisibility(View.GONE);

                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("JSON", "Error" + error);
                    checkOutButton.setVisibility(View.VISIBLE);
                    progressBarCheckOut.setVisibility(View.GONE);

                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("Quantity", String.valueOf(xQuantity));
                    params.put("FoodName", xFoodName);
                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(stringRequest);
        });

    }

    //Process food order by user
    public void insertFoodOrdered(String oFoodImage, String oFoodName, int oUserID, int oQuantity, double oTotal, int oFoodID) {
        Random random = new Random();
        int orderID = random.nextInt(999);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, InsertOrder, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String result = jsonObject.getString("success");

                    if (result.equals("1")) {
                        Log.i("Orders", "Order Success " + result);

                        sendToProcessingOrder.putString("orderID", String.valueOf(orderID));
                        Intent foodIntent = new Intent(CheckOut.this, ProcessingOrder.class);
                        foodIntent.putExtras(sendToProcessingOrder);
                        startActivity(foodIntent);
                        finish();

                    } else if (result.equals("0")) {
                        Log.e("Orders", "Order Failed " + result);
                    }

                } catch (JSONException e) {
                    Log.e("JSON", e.getMessage());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("JSON", "Error" + error);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("OrderID", String.valueOf(orderID));
                params.put("UserID", String.valueOf(oUserID));
                params.put("FoodID", String.valueOf(oFoodID));
                params.put("FoodOrdered", oFoodName);
                params.put("FoodImage", oFoodImage);
                params.put("TotalPrice", String.valueOf(oTotal));
                params.put("QuantityOrdered", String.valueOf(oQuantity));
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

}