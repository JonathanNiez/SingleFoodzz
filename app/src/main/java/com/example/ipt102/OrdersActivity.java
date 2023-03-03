package com.example.ipt102;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class OrdersActivity extends AppCompatActivity {

    private ArrayList<FoodOrdered> foodOrderedArrayList = new ArrayList<>();
    private FoodOrderedAdapter foodOrderedAdapter;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager linearLayoutManager;
    private TextView textViewInfo;
    private Button backButton;

    //Local host
    String ViewFoodOrderedLocal = "http://192.168.1.149/IPT102_Project/viewfoodsOrdered.php";
    String CancelOrderLocal = "http://192.168.1.149/IPT102_Project/cancelOrder.php";

    //Web host
    String ViewFoodOrdered = "https://jonathanniez.000webhostapp.com/IPT102_Project/viewfoodsOrdered.php";
    String CancelOrder = "https://jonathanniez.000webhostapp.com/IPT102_Project/cancelOrder.php";
    double deliveryFee = 50;

    @Override
    public void onBackPressed() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);

        textViewInfo = findViewById(R.id.textViewInfo);
        backButton = findViewById(R.id.backButton);

        backButton.setOnClickListener(v -> {
            finish();
            overridePendingTransition(0, 0);
        });

        Bundle bundle = getIntent().getExtras();
        overridePendingTransition(0, 0);

        if (bundle != null) {

            loadRecyclerView();

            String getFoodName = bundle.getString("foodName");
            String getPrice = bundle.getString("foodPrice");
            String getFoodImage = bundle.getString("foodImage");
            String getFoodQuantity = bundle.getString("foodQuantity");
            String getUserID = bundle.getString("userID");
            String getOrderID = bundle.getString("orderID");

            loadFoodOrdered(Integer.parseInt(getUserID));

        } else {
            loadRecyclerView();
            loadFoodOrderedByUser();
        }
    }

    private void loadRecyclerView() {
        recyclerView = findViewById(R.id.recyclerViewOrders);
        linearLayoutManager = new LinearLayoutManager(OrdersActivity.this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        foodOrderedAdapter = new FoodOrderedAdapter(OrdersActivity.this, foodOrderedArrayList);
        recyclerView.setAdapter(foodOrderedAdapter);
    }

    private void loadFoodOrderedByUser() {
        int getUserIDStatic = LoginData.UserId;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, ViewFoodOrdered, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("LoadFoodOrderedByUser", response);
                try {
                    JSONArray foods = new JSONArray(response);

                    for (int i = 0; i < foods.length(); i++) {
                        JSONObject foodObject = foods.getJSONObject(i);

                        int userID = foodObject.getInt("UserID");
                        int orderID = foodObject.getInt("OrderID");
                        String foodImage = foodObject.getString("FoodImage");
                        String foodOrdered = foodObject.getString("FoodOrdered");
                        double totalPrice = foodObject.getDouble("TotalPrice");
                        int quantityOrdered = foodObject.getInt("QuantityOrdered");

                        Log.i("OrdersActivity", "User ID: " + getUserIDStatic);

//                        textViewInfo.setText(getUserIDStatic + " " + userID);

                        foodOrderedArrayList.add(new FoodOrdered(orderID, foodImage, foodOrdered, totalPrice + deliveryFee, quantityOrdered));
                    }

                    foodOrderedAdapter = new FoodOrderedAdapter(OrdersActivity.this, foodOrderedArrayList);
                    recyclerView.setAdapter(foodOrderedAdapter);

                    foodOrderedAdapter.setOnItemClickListener(new FoodOrderedAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(int position) {
                            foodOrderedArrayList.get(position);
                            foodOrderedAdapter.notifyItemChanged(position);
                        }

                        @Override
                        public void onCancelClick(int position) {

                            AlertDialog.Builder alert = new AlertDialog.Builder(OrdersActivity.this);
                            alert.setTitle("Order");
                            alert.setMessage("Do you want to Cancel Order?");
                            alert.setCancelable(true);
                            alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    int orderIDArray = foodOrderedArrayList.get(position).getFoodOrderID();

                                    if (foodOrderedArrayList == null){
                                        textViewInfo.setVisibility(View.VISIBLE);
                                        textViewInfo.setText("No Foods Ordered");

                                    }
                                    else {

                                        //Cancel Order
                                        StringRequest stringRequest = new StringRequest(Request.Method.POST, CancelOrder, new Response.Listener<String>() {
                                            @Override
                                            public void onResponse(String response) {
                                                try {
                                                    JSONObject jsonObject = new JSONObject(response);
                                                    String result = jsonObject.getString("success");

                                                    if (result.equals("1")) {
                                                        Log.i("Orders", "Cancel Success " + result);

                                                        foodOrderedArrayList.remove(position);
                                                        foodOrderedAdapter.notifyItemRemoved(position);


                                                    } else if (result.equals("0")) {
                                                        Log.e("Orders", "Cancel Failed " + result);
                                                    }

                                                } catch (JSONException e) {
                                                    Log.e("JSON", "Error");
                                                }
                                            }
                                        }, new Response.ErrorListener() {
                                            @Override
                                            public void onErrorResponse(VolleyError error) {
                                                Log.e("JSON", "Error" + error);
                                                textViewInfo.setText(error.getMessage());
                                            }
                                        }) {
                                            @Override
                                            protected Map<String, String> getParams() throws AuthFailureError {
                                                Map<String, String> params = new HashMap<>();
                                                params.put("OrderID", String.valueOf(orderIDArray));
                                                return params;
                                            }
                                        };
                                        RequestQueue requestQueue = Volley.newRequestQueue(OrdersActivity.this);
                                        requestQueue.add(stringRequest);
                                    }
                                }
                            });
                            alert.setNegativeButton("No", (dialog, which) -> dialog.cancel());
                            alert.show();
                        }

                    });

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Response", error.toString());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("UserID", String.valueOf(getUserIDStatic));
                return params;
            }
        };
        Volley.newRequestQueue(this).add(stringRequest);
    }

    private void loadFoodOrdered(int xUserID) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ViewFoodOrdered, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("LoadFoodOrdered", response);
                try {
                    JSONArray foods = new JSONArray(response);

                    for (int i = 0; i < foods.length(); i++) {
                        JSONObject foodObject = foods.getJSONObject(i);

                        int userID = foodObject.getInt("UserID");
                        int orderID = foodObject.getInt("OrderID");
                        String foodImage = foodObject.getString("FoodImage");
                        String foodOrdered = foodObject.getString("FoodOrdered");
                        double totalPrice = foodObject.getDouble("TotalPrice");
                        int quantityOrdered = foodObject.getInt("QuantityOrdered");

                        Log.i("Foods", String.valueOf(xUserID));

                        foodOrderedArrayList.add(new FoodOrdered(orderID, foodImage, foodOrdered, totalPrice + deliveryFee, quantityOrdered));
                    }

                    foodOrderedAdapter = new FoodOrderedAdapter(OrdersActivity.this, foodOrderedArrayList);
                    recyclerView.setAdapter(foodOrderedAdapter);

                    foodOrderedAdapter.setOnItemClickListener(new FoodOrderedAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(int position) {
                            foodOrderedArrayList.get(position);
                            foodOrderedAdapter.notifyItemChanged(position);
                        }

                        @Override
                        public void onCancelClick(int position) {

                            AlertDialog.Builder alert = new AlertDialog.Builder(OrdersActivity.this);
                            alert.setTitle("Order");
                            alert.setMessage("Do you want to Cancel Order?");
                            alert.setCancelable(true);
                            alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    int orderIDArray = foodOrderedArrayList.get(position).getFoodOrderID();

                                    StringRequest stringRequest = new StringRequest(Request.Method.POST, CancelOrder, new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {
                                            try {
                                                JSONObject jsonObject = new JSONObject(response);
                                                String result = jsonObject.getString("success");

                                                if (result.equals("1")) {
                                                    Log.i("Orders", "Cancel Success " + result);

                                                    foodOrderedArrayList.remove(position);
                                                    foodOrderedAdapter.notifyItemRemoved(position);


                                                } else if (result.equals("0")) {
                                                    Log.e("Orders", "Cancel Failed " + result);
                                                }

                                            } catch (JSONException e) {
                                                Log.e("JSON", "Error");
                                            }
                                        }
                                    }, new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            Log.e("JSON", "Error" + error);
                                            textViewInfo.setText(error.getMessage());
                                        }
                                    }) {
                                        @Override
                                        protected Map<String, String> getParams() throws AuthFailureError {
                                            Map<String, String> params = new HashMap<>();
                                            params.put("OrderID", String.valueOf(orderIDArray));
                                            return params;
                                        }
                                    };
                                    RequestQueue requestQueue = Volley.newRequestQueue(OrdersActivity.this);
                                    requestQueue.add(stringRequest);

                                }
                            });
                            alert.setNegativeButton("No", (dialog, which) -> dialog.cancel());
                            alert.show();
                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Response", error.toString());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("UserID", String.valueOf(xUserID));
                return params;
            }
        };
        Volley.newRequestQueue(this).add(stringRequest);
    }
}