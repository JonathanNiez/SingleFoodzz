package com.example.ipt102;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    Toolbar toolbar;
    TextView getemail, getusername;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    View navheader;
    Bundle sendToFoodOrders = new Bundle();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.i("State","onCreate");

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        navheader = navigationView.getHeaderView(0);

        String getUsername = getIntent().getStringExtra("username");
        String getEmail = getIntent().getStringExtra("email");
        String getID = getIntent().getStringExtra("id");
        String getFirstname = getIntent().getStringExtra("firstname").trim();
        String getLastname = getIntent().getStringExtra("lastname").trim();
        String getAddress = getIntent().getStringExtra("address").trim();
        String getPhoneNo = getIntent().getStringExtra("phoneNo").trim();

        getusername = navheader.findViewById(R.id.getusername);
        getemail = navheader.findViewById(R.id.getemail);

        toolbar.setTitle(getUsername);
        getusername.setText(getUsername);
        getemail.setText(getEmail);

        LoginData.UserId = Integer.parseInt(getID);
        LoginData.UserName = getUsername;
        LoginData.Email = getEmail;
        LoginData.Firstname = getFirstname;
        LoginData.Lastname = getLastname;
        LoginData.Address = getAddress;
        LoginData.PhoneNo = Integer.parseInt(getPhoneNo);

        Log.i("MainActivity", "User ID: " + getID);

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.nav_drawer_open, R.string.nav_drawer_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        if (savedInstanceState == null){

            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new Home()).commit();
            navigationView.setCheckedItem(R.id.home);
        }
    }

    public void getAccountData(String xGetUsername, String xGetEmail,
                               String xFirstname, String xLastname,
                               String xAddress, int xPhoneNumber){
        Bundle sendToAccount = new Bundle();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        sendToAccount.putString("username", xGetUsername);
        sendToAccount.putString("email", xGetEmail);
        sendToAccount.putString("firstname", xFirstname);
        sendToAccount.putString("lastname", xLastname);
        sendToAccount.putString("address", xAddress);
        sendToAccount.putString("phoneNo", String.valueOf(xPhoneNumber));
        Account account = new Account();
        account.setArguments(sendToAccount);
        fragmentTransaction.replace(R.id.fragment_container, account).commit();
    }

    private void getUserData(int xGetID) {
        sendToFoodOrders.putString("id", String.valueOf(xGetID));
        FoodOrder foodOrder = new FoodOrder();
        foodOrder.setArguments(sendToFoodOrders);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                foodOrder).commit();
    }

    private void getUserID(int xGetID){
        Intent ordersIntent = new Intent(this, OrdersActivity.class);
        startActivity(ordersIntent);

    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.home:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new Home()).commit();
                break;
            case R.id.orderfood:
                getUserData(LoginData.UserId);
                break;
            case R.id.orders:
                getUserID(LoginData.UserId);
                break;
            case R.id.account:
                getAccountData(LoginData.UserName, LoginData.Email, LoginData.Firstname, LoginData.Lastname, LoginData.Address, LoginData.PhoneNo);
                break;
            case R.id.settings:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new Settings()).commit();
                break;
            case R.id.share:
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_SUBJECT, "Body");
                intent.putExtra(Intent.EXTRA_TEXT, "Subject");
                startActivity(Intent.createChooser(intent, "Share with"));
                break;
            case  R.id.logout:
                AlertDialog.Builder alert = new AlertDialog.Builder(this);
                alert.setTitle("Logout");
                alert.setMessage("Do you want to Logout?");
                alert.setCancelable(true);
                alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        LoginData.UserId = 0;
                        LoginData.Email = null;
                        LoginData.UserName = null;
                        LoginData.Firstname = null;
                        LoginData.Lastname = null;
                        LoginData.Address = null;
                        LoginData.PhoneNo = 0;

                        Intent intent1 = new Intent(MainActivity.this, LoggingIn.class);
                        startActivity(intent1);
                        finish();

                        Toast.makeText(MainActivity.this, "Logged Out", Toast.LENGTH_SHORT).show();
                    }
                });
                alert.setNegativeButton("No", (dialog, which) -> dialog.cancel());
                alert.show();
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }



    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else {

        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i("State","onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("State","onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i("State","onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i("State","onStop");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i("State","onRestart");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("State","onDestroy");
    }
}