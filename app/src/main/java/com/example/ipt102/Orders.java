package com.example.ipt102;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Orders#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Orders extends Fragment {

    private ArrayList<FoodOrdered> foodOrderedArrayList = new ArrayList<>();
    private FoodOrderedAdapter foodOrderedAdapter;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager linearLayoutManager;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Orders() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Orders.
     */
    // TODO: Rename and change types and number of parameters
    public static Orders newInstance(String param1, String param2) {
        Orders fragment = new Orders();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    TextView foodNameOrders, priceOrders, textViewInfo;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_orders, container, false);

//        foodOrderedArrayList.add(new FoodOrdered("", "dingus", 100));
        textViewInfo = view.findViewById(R.id.textViewInfo);

        recyclerView = view.findViewById(R.id.recyclerViewOrders);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        foodOrderedAdapter = new FoodOrderedAdapter(getContext() ,foodOrderedArrayList);
        recyclerView.setAdapter(foodOrderedAdapter);

        if (foodOrderedAdapter != null){
            foodOrderedAdapter.setOnItemClickListener(new FoodOrderedAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(int position) {
                    foodOrderedArrayList.get(position);
                    foodOrderedAdapter.notifyItemChanged(position);
                }

                @Override
                public void onCancelClick(int position) {
                    AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                    alert.setTitle("Order");
                    alert.setMessage("Do you want to Cancel Order?");
                    alert.setCancelable(true);
                    alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            foodOrderedArrayList.remove(position);
                            foodOrderedAdapter.notifyItemRemoved(position);
                        }
                    });
                    alert.setNegativeButton("No", (dialog, which) -> dialog.cancel());
                    alert.show();

                    Toast.makeText(getContext(), "Button Click", Toast.LENGTH_SHORT).show();

//                Bundle bundle = new Bundle();
//                bundle.putString("foodName", foodOrderedArrayList.get(position).foodName);
//                bundle.putString("foodPrice", String.valueOf(foodOrderedArrayList.get(position).foodPrice));
//                Orders orders = new Orders();
//                orders.setArguments(bundle);
//                getFragmentManager().beginTransaction().replace(R.id.fragment_container,
//                        orders).commit();
//
//                Intent intent = new Intent(getContext(), CheckOut.class);
//                intent.putExtra("foodName", foodOrderedArrayList.get(position).foodName);
//                intent.putExtra("foodPrice", String.valueOf(foodOrderedArrayList.get(position).foodPrice));
//                startActivity(intent);
                }
            });
        }
        else {
            textViewInfo.setVisibility(View.VISIBLE);
            textViewInfo.setText("You have not ordered a Food");
        }

        Bundle bundle = this.getArguments();

        if (bundle == null){
//            foodNameOrders.setText("No food ordered");
//            priceOrders.setText("");
        }
        else {
            String getFoodName = bundle.getString("foodName");
            String getPrice = bundle.getString("foodPrice");

//            foodNameOrders.setText(getFoodName);
//            priceOrders.setText(getPrice);
        }
        return view;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }
}