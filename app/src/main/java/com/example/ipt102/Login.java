package com.example.ipt102;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Login extends AppCompatActivity {

    TextInputEditText password, email;
    Button loginbutton, cancelButton;
    ProgressBar progressbar;

    //Web host
    String URL = "https://jonathanniez.000webhostapp.com/IPT102_Project/login.php";

    //Local host
    String LocalHostURl = "http://192.168.1.149/IPT102_Project/login.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        password = findViewById(R.id.password);
        email = findViewById(R.id.email);
        loginbutton = findViewById(R.id.loginbutton);
        progressbar = findViewById(R.id.proggessbar);
        cancelButton = findViewById(R.id.cancelButton);

        cancelButton.setOnClickListener(v -> {
            finish();
        });


        loginbutton.setOnClickListener(v -> {

            progressbar.setVisibility(View.VISIBLE);
            loginbutton.setVisibility(View.GONE);
            String emailAdd = Objects.requireNonNull(email.getText()).toString().trim();
            String passWord = Objects.requireNonNull(password.getText()).toString().trim();

            if (emailAdd.isEmpty() || passWord.isEmpty()) {
                Toast.makeText(this, "Please Enter your Login Credentials", Toast.LENGTH_SHORT).show();
                progressbar.setVisibility(View.GONE);
                loginbutton.setVisibility(View.VISIBLE);
            } else {
                LoginMethod(emailAdd, passWord);
            }
        });
    }

    private void LoginMethod(String email, String password) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String result = jsonObject.getString("success");
                    JSONArray jsonArray = jsonObject.getJSONArray("login");

                    if (result.equals("1")) {
                        progressbar.setVisibility(View.GONE);
                        loginbutton.setVisibility(View.VISIBLE);
                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject object = jsonArray.getJSONObject(i);
                            String username = object.getString("Username").trim();
                            String email = object.getString("Email").trim();
                            String id = object.getString("ID").trim();
                            String firstname = object.getString("Firstname").trim();
                            String lastname = object.getString("Lastname").trim();
                            String address = object.getString("Address").trim();
                            String phoneNo = object.getString("PhoneNo").trim();

                            Toast.makeText(Login.this, "Logged In: " + username, Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(Login.this, MainActivity.class);

                            intent.putExtra("username", username);
                            intent.putExtra("email", email);
                            intent.putExtra("id", id);
                            intent.putExtra("firstname", firstname);
                            intent.putExtra("lastname", lastname);
                            intent.putExtra("address", address);
                            intent.putExtra("phoneNo", phoneNo);


                            startActivity(intent);
                            finish();
                        }
                    } else if (result.equals("0")) {
                        progressbar.setVisibility(View.GONE);
                        loginbutton.setVisibility(View.VISIBLE);

                        Toast.makeText(Login.this, "Email does not Exist", Toast.LENGTH_LONG).show();
                        Log.e("Login", "Email does not Exist" + result);
                    }
                } catch (JSONException e) {
                    Log.e("JSON Error", response);
                    Toast.makeText(Login.this, "Invalid Email or Password", Toast.LENGTH_LONG).show();
                    Log.e("Login", "Invalid Email or Password" + response);
                    Log.e("JSON Error", response);
                    progressbar.setVisibility(View.GONE);
                    loginbutton.setVisibility(View.VISIBLE);

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("JSON", error.getMessage());
                Toast.makeText(Login.this, "Internet Connection Error", Toast.LENGTH_SHORT).show();
                progressbar.setVisibility(View.GONE);
                loginbutton.setVisibility(View.VISIBLE);

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Email", email);
                params.put("Password", password);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}