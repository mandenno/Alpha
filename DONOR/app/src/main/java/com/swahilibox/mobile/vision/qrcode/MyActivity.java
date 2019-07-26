package com.swahilibox.mobile.vision.qrcode;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;



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


        loader=(RelativeLayout)findViewById(R.id.loader);
        loginDataBaseAdapter=new LoginDataBaseAdapter(this);
        loginDataBaseAdapter=loginDataBaseAdapter.open();

        final String storedname = loginDataBaseAdapter.getSinlgeEntry1();

        // Toast.makeText(this,"User:"+storedname+" Pass: "+storedpass,Toast.LENGTH_LONG).show();
        if (storedname.equals("NOT EXIST")) {

            Intent intent = new Intent(this, UserLogin.class);
            startActivity(intent);
            finish();
        }
        else  {

                Intent intent = new Intent(MyActivity.this, MainActivity.class);
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