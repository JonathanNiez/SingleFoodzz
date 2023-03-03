package com.example.ipt102;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

public class Register extends AppCompatActivity {

    TextInputEditText firstname, lastname, username, email, password, confirmPassword, address, phoneno;
    Button registerbutton;
    ProgressBar progressbar;

    //Web host
    String URL = "https://jonathanniez.000webhostapp.com/IPT102_Project/register.php";

    //Local host
    String LocalHostURl = "http://192.168.1.149/IPT102_Project/register.php";
    Random random = new Random();
    int userID = random.nextInt(999);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        firstname = findViewById(R.id.firstname);
        lastname = findViewById(R.id.lastname);
        username = findViewById(R.id.username);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        confirmPassword = findViewById(R.id.confirmPassword);
        address = findViewById(R.id.address);
        phoneno = findViewById(R.id.phoneno);
        registerbutton = findViewById(R.id.registerbutton);
        progressbar = findViewById(R.id.proggesbar);

        registerbutton.setOnClickListener(v -> {

            progressbar.setVisibility(View.VISIBLE);
            registerbutton.setVisibility(View.GONE);

            final String userName = Objects.requireNonNull(this.username.getText()).toString().trim();
            final String emailAddress = Objects.requireNonNull(this.email.getText()).toString().trim();
            final String passWord = Objects.requireNonNull(this.password.getText()).toString().trim();
            final String confirmPassWord = Objects.requireNonNull(this.confirmPassword.getText()).toString().trim();
            final String firstName = Objects.requireNonNull(this.firstname.getText()).toString().trim();
            final String lastName = Objects.requireNonNull(this.lastname.getText()).toString().trim();
            final String address = Objects.requireNonNull(this.address.getText()).toString().trim();
            final String phoneNo = Objects.requireNonNull(this.phoneno.getText()).toString().trim();

            if (userName.isEmpty()){
                progressbar.setVisibility(View.GONE);
                registerbutton.setVisibility(View.VISIBLE);

                Toast.makeText(this, "Please Enter Username", Toast.LENGTH_SHORT).show();
            }
            else if (passWord.isEmpty()) {
                progressbar.setVisibility(View.GONE);
                registerbutton.setVisibility(View.VISIBLE);

                Toast.makeText(this, "Please Enter Password", Toast.LENGTH_SHORT).show();
            }
            else if (!passWord.equals(confirmPassWord)){
                progressbar.setVisibility(View.GONE);
                registerbutton.setVisibility(View.VISIBLE);

                Toast.makeText(this, "Password do not Match", Toast.LENGTH_SHORT).show();
            }
            else {
                StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String result = jsonObject.getString("success");
                            if (result.equals("2")){
                                Toast.makeText(Register.this, "Email is Already Used", Toast.LENGTH_LONG).show();
                                Log.w("Register", "Email is Already Used");
                                progressbar.setVisibility(View.GONE);
                                registerbutton.setVisibility(View.VISIBLE);
                                email.setText("");
                            }
                            else {

                                if (result.equals("1")) {
                                    Toast.makeText(Register.this, "Register Success", Toast.LENGTH_SHORT).show();
                                    progressbar.setVisibility(View.GONE);
                                    registerbutton.setVisibility(View.VISIBLE);

                                    Intent intent = new Intent(Register.this, Login.class);
                                    startActivity(intent);
                                    finish();

                                    Log.i("Register", "Register Success " + result);

                                } else if (result.equals("0")) {
                                    progressbar.setVisibility(View.GONE);
                                    registerbutton.setVisibility(View.VISIBLE);

                                    Toast.makeText(Register.this, "Register Failed", Toast.LENGTH_SHORT).show();
                                    Log.e("Register", "Register Failed " + response);
                                }
                            }
                        }
                        catch (JSONException e){
                            progressbar.setVisibility(View.GONE);
                            registerbutton.setVisibility(View.VISIBLE);

                            Log.e("JSON", "Error");

                            Toast.makeText(Register.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressbar.setVisibility(View.GONE);
                        registerbutton.setVisibility(View.VISIBLE);

                        Log.e("JSON", "Error" + error);

                        Toast.makeText(Register.this, "Internet Connection Error", Toast.LENGTH_SHORT).show();
                    }
                })
                {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();
                        params.put("ID", String.valueOf(userID));
                        params.put("Username", userName);
                        params.put("Email", emailAddress);
                        params.put("Password", passWord);
                        params.put("Firstname", firstName);
                        params.put("Lastname", lastName);
                        params.put("Address", address);
                        params.put("PhoneNo", phoneNo);
                        return params;
                    }
                };
                RequestQueue requestQueue = Volley.newRequestQueue(this);
                requestQueue.add(stringRequest);
            }
        });
    }
}