package com.example.ipt102;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FoodOrder#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FoodOrder extends Fragment {

    private ArrayList<Foods> foodsArrayList = new ArrayList<>();
    private FoodAdapter foodAdapter;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager linearLayoutManager;

    //Web host
    String URL = "https://jonathanniez.000webhostapp.com/IPT102_Project/viewfoodsLocal.php";

    //Local host
    String LocalHostURl = "http://192.168.1.149/IPT102_Project/viewFoodsLocal.php";
    Bundle bundle;
    Orders orders;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FoodOrder() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FoodOrder.
     */
    // TODO: Rename and change types and number of parameters
    public static FoodOrder newInstance(String param1, String param2) {
        FoodOrder fragment = new FoodOrder();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

//    Button button1, button2, button3, button4;
//    TextView food1, food2, food3, food4, price1, price2, price3, price4, foodName, foodPrice;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_food_order, container, false);

        String getID = getArguments().getString("id");

        loadFoods(getID);

//        foodsArrayList  = new ArrayList<>();
//        foodsArrayList.add(new Foods(R.drawable.bangus1, "food na ni", 69420));
//        foodsArrayList.add(new Foods(R.drawable.bangus1, "food na ni", 69420));
//        foodsArrayList.add(new Foods(R.drawable.bangus1, "food na ni", 69420));
//        foodsArrayList.add(new Foods(R.drawable.bangus1, "food na ni", 69420));

        recyclerView = view.findViewById(R.id.recyclerView);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        foodAdapter = new FoodAdapter(getContext(), foodsArrayList);
        recyclerView.setAdapter(foodAdapter);

        return view;
    }

    private void loadFoods(String xGetID) {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("Response", response);
                try {
                    JSONArray foods = new JSONArray(response);

                    for (int i = 0; i < foods.length(); i++) {
                        JSONObject foodObject = foods.getJSONObject(i);

                        int foodID = foodObject.getInt("FoodID");
                        String foodImage = foodObject.getString("FoodImage");
                        String foodName = foodObject.getString("FoodName");
                        double price = foodObject.getDouble("Price");
                        int quantity = foodObject.getInt("Quantity");

                        Log.i("Foods", foodName + price);

                        foodsArrayList.add(new Foods(foodImage, foodName, price, foodID));
                    }

                    foodAdapter = new FoodAdapter(getContext(), foodsArrayList);
                    recyclerView.setAdapter(foodAdapter);

                    foodAdapter.setOnItemClickListener(new FoodAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(int position) {
                            foodsArrayList.get(position);
                            foodAdapter.notifyItemChanged(position);
                        }

                        @Override
                        public void onOrderClick(int position) {
                            foodsArrayList.get(position);
                            foodAdapter.notifyItemChanged(position);

//                            Bundle bundle = new Bundle();
//                            bundle.putString("foodName", foodsArrayList.get(position).foodName);
//                            bundle.putString("foodPrice", String.valueOf(foodsArrayList.get(position).foodPrice));
//                            Orders orders = new Orders();
//                            orders.setArguments(bundle);
//                            getFragmentManager().beginTransaction().replace(R.id.fragment_container,
//                                   orders).commit();

                            Bundle sendToCheckOut = new Bundle();
                            sendToCheckOut.putString("foodName", foodsArrayList.get(position).foodName);
                            sendToCheckOut.putString("foodPrice", String.valueOf(foodsArrayList.get(position).foodPrice));
                            sendToCheckOut.putString("foodImage", foodsArrayList.get(position).foodImage);
                            sendToCheckOut.putString("userID", xGetID);
                            sendToCheckOut.putString("foodID", String.valueOf(foodsArrayList.get(position).foodID));
                            Intent intent = new Intent(getContext(), CheckOut.class);
                            intent.putExtras(sendToCheckOut);
//                            intent.putExtra("foodName", foodsArrayList.get(position).foodName);
//                            intent.putExtra("foodPrice", String.valueOf(foodsArrayList.get(position).foodPrice));
//                            intent.putExtra("foodImage", foodsArrayList.get(position).foodImage);
                            startActivity(intent);
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
        });
        Volley.newRequestQueue(getContext()).add(stringRequest);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }
}

