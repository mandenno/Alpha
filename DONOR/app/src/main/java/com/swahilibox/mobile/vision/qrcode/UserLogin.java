package com.swahilibox.mobile.vision.qrcode;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;
import es.dmoral.toasty.Toasty;

public class UserLogin extends AppCompatActivity implements View.OnClickListener {

    LoginDataBaseAdapter loginDataBaseAdapter;
    Button lngbtn, agent;
    private static CheckBox login_type;
    TextView forgot, signup;
    EditText uphone, password;
    ProgressDialog pDialog;
    ImageView icon;
    public static final String URL_LOGIN = "https://alpha.nupola.com/donor_login.php";
    private GpsTracker gpsTracker;
    private static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = findViewById(R.id.toolbar_v);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
        toolbar.setNavigationIcon(R.drawable.ic_back_arrow);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        uphone=(EditText)findViewById(R.id.phone);
        forgot=(TextView) findViewById(R.id.forgot);
        forgot.setOnClickListener(this);
        lngbtn=(Button)findViewById(R.id.phone_sign_in_button);
        lngbtn.setOnClickListener(this);

        password=(EditText)findViewById(R.id.password);
        loginDataBaseAdapter=new LoginDataBaseAdapter(this);
        loginDataBaseAdapter=loginDataBaseAdapter.open();

    }


    public void userLogin() {


        final String user_phone = uphone.getText().toString();
        final String user_pass = password.getText().toString();

        if (user_pass.equals("") || user_phone.equals("")) {
            Toasty.error(this, "Please fill all fields!", Toast.LENGTH_LONG, true).show();

        }
        else if(user_phone.length()>10||user_phone.length()<10)
        {

            Toasty.error(this, "Enter 10 digits phone number!", Toast.LENGTH_LONG, true).show();
        }
        else {
            final SweetAlertDialog pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
            pDialog.getProgressHelper().setBarColor(Color.parseColor("#2a628e"));
            pDialog.setTitleText("Verifying...Please Wait!");
            pDialog.setCancelable(false);
            pDialog.show();
            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_LOGIN,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if(response.contains("success"))
                            {
                            
                                    pDialog.dismiss();
                                Intent intent = new Intent(UserLogin.this, MainActivity.class);
                                startActivity(intent);
                                loginDataBaseAdapter.insertEntry(user_phone, user_pass,"user");
                            }

                            else if(response.contains("failed"))
                            {
                                if (pDialog.isShowing())
                                    pDialog.dismiss();
                                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(UserLogin.this);
                                alertDialogBuilder.setMessage("You have entered invalid login details!");
                                alertDialogBuilder.setPositiveButton("OK",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface arg0, int arg1) {
                                                arg0.dismiss();
                                            }
                                        });

                                alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });

                                AlertDialog alertDialog = alertDialogBuilder.create();
                                alertDialog.show();
                            }
                            else
                            {
                                if (pDialog.isShowing())
                                    pDialog.dismiss();
                                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(UserLogin.this);
                                alertDialogBuilder.setMessage("We have encountered a technical error! Please try again later.");
                                alertDialogBuilder.setPositiveButton("OK",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface arg0, int arg1) {
                                                arg0.dismiss();
                                            }
                                        });

                                alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                      dialog.dismiss();
                                    }
                                });

                                AlertDialog alertDialog = alertDialogBuilder.create();
                                alertDialog.show();
                            }


                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            if (pDialog.isShowing())
                                pDialog.dismiss();
                            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(UserLogin.this);
                            alertDialogBuilder.setMessage("Check your network connection and try again!");
                            alertDialogBuilder.setPositiveButton("OK",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface arg0, int arg1) {
                                            finish();
                                        }
                                    });

                            alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    finish();
                                }
                            });

                            AlertDialog alertDialog = alertDialogBuilder.create();
                            alertDialog.show();
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() {

                    Map<String, String> params = new HashMap<String, String>();
                    params.put("phone", user_phone);
                    params.put("password", user_pass);


                    return params;
                }

            };

            RequestQueue requestQueue = Volley.newRequestQueue(UserLogin.this);
            requestQueue.add(stringRequest);


        }
    }

    @Override
    public void onClick(View view) {
    if(view==lngbtn)
        {
     userLogin();


        }

        else if(view==forgot)
        {
            Intent intent = new Intent(UserLogin.this, ForgotPass.class);

            startActivity(intent);

        }


    }



}
