package com.example.ipt102;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toolbar;

import com.google.android.material.navigation.NavigationView;

public class Account extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    TextView username, email, fullName, phoneNo, address;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_account, container, false);

        username = v.findViewById(R.id.username);
        email = v.findViewById(R.id.email);
        fullName = v.findViewById(R.id.fullName);
        phoneNo = v.findViewById(R.id.phoneNumber);
        address = v.findViewById(R.id.address);

        Bundle bundle = getArguments();

        if(bundle != null){
            String getUsername = bundle.getString("username");
            String getEmail = bundle.getString("email");
            String getFirstname = bundle.getString("firstname");
            String getLastname = bundle.getString("lastname");
            String getAddress = bundle.getString("address");
            String getPhoneNo = bundle.getString("phoneNo");

            username.setText("Username: " + getUsername);
            email.setText("Email: " + getEmail);
            fullName.setText("Name: " + getFirstname + " " + getLastname);
            phoneNo.setText("Phone No: " + getPhoneNo);
            address.setText("Address: " + getAddress);

            Log.i("Account", "Login Data Received");
        }
        return v;
    }
}