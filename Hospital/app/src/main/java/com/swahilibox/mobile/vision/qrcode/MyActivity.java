package com.swahilibox.mobile.vision.qrcode;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.swahilibox.mobile.vision.qrcode.login.MainActivity;
import com.swahilibox.mobile.vision.qrcode.login.login.Login;

import java.util.HashMap;
import java.util.Map;

import es.dmoral.toasty.Toasty;


public class MyActivity extends AppCompatActivity {

RelativeLayout loader;
    private Uri uriContact;
    private String contactID;     // contacts unique ID
    LoginDataBaseAdapter loginDataBaseAdapter;
private Button schat;
    private ImageView iv;
    ProgressDialog progress;
    TextView tvm;
    final Context context = this;
    GpsTracker gps;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        setContentView(R.layout.mainx);
        try {
            if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 101);
            }
        } catch (Exception e){
            e.printStackTrace();
        }

        gps = new GpsTracker(this);

        // Check if GPS enabled
        if (gps.canGetLocation()) {

           String lati = String.valueOf(gps.getLatitude());
            String longi =  String.valueOf(gps.getLongitude());

            // \n is for new line
         //   Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + lati + "\nLong: " + longi, Toast.LENGTH_LONG).show();
        }
        else {
            // Can't get location.
            // GPS or network is not enabled.
            // Ask user to enable GPS/network in settings.
            gps.showSettingsAlert();
        }

loader=(RelativeLayout)findViewById(R.id.loader);
        loginDataBaseAdapter=new LoginDataBaseAdapter(this);
        loginDataBaseAdapter=loginDataBaseAdapter.open();

        final String storedname = loginDataBaseAdapter.getSinlgeEntry1();

        // Toast.makeText(this,"User:"+storedname+" Pass: "+storedpass,Toast.LENGTH_LONG).show();
        if (storedname.equals("NOT EXIST")) {
            try {
                if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED ) {
                    ActivityCompat.requestPermissions(MyActivity.this, new String[]{android.Manifest.permission.READ_PHONE_STATE}, 101);
                }
            } catch (Exception e){
                e.printStackTrace();
            }
          //  Toast.makeText(this,"Do you have an account?", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }
        else  {

                Intent intent = new Intent(MyActivity.this, Login.class);
                startActivity(intent);
                finish();

        }


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Close The Database
        loginDataBaseAdapter.close();
    }
}