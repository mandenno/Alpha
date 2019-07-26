package com.swahilibox.mobile.vision.qrcode;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class Donor_Reg extends AppCompatActivity implements View.OnClickListener {
    public static final String URL_SUBMIT = "https://alpha.nupola.com/donor_reg.php";

    EditText donor_id, age, weight, phone, donor_name;
    Button submit;
    TextView already;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donor__reg);
        Toolbar toolbar = findViewById(R.id.toolbar_donorreg);
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
        already=(TextView)findViewById(R.id.already);
        already.setOnClickListener(this);
        submit=(Button)findViewById(R.id.submit);
        submit.setOnClickListener(this);
        donor_id=(EditText)findViewById(R.id.donor_id);
        age=(EditText)findViewById(R.id.donor_age);
        weight=(EditText)findViewById(R.id.donor_weight);
        phone=(EditText)findViewById(R.id.donor_phone);
        donor_name=(EditText)findViewById(R.id.donor_name);


    }


    public void regDonor(final String name,final String age,final String weight,final String phone,final String donorid)
    {

        final SweetAlertDialog pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#FFC107"));
        pDialog.setTitleText("Creating...");
        pDialog.setCancelable(false);
        pDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_SUBMIT,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        pDialog.dismiss();
                        if(response.contains("success"))
                        {

                            new SweetAlertDialog(Donor_Reg.this, SweetAlertDialog.ERROR_TYPE)
                                    .setTitleText("Successful!")
                                    .setContentText("User registration is successfully. Details have been sent to the donor!")
                                    .showCancelButton(true)
                                    .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sDialog) {
                                            sDialog.cancel();
                                            finish();
                                        }
                                    })
                                    .show();

                        }

                        else if(response.contains("failed"))
                        {

                            new SweetAlertDialog(Donor_Reg.this, SweetAlertDialog.ERROR_TYPE)
                                    .setTitleText("Failed")
                                    .setContentText("We have encountered an error while processing")
                                    .show();

                        }
                        else if(response.contains("exist"))
                        {

                            new SweetAlertDialog(Donor_Reg.this, SweetAlertDialog.ERROR_TYPE)
                                    .setTitleText("Invalid")
                                    .setContentText("A donor with the that donor ID exist!")
                                    .show();

                        }



                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pDialog.dismiss();
                        new SweetAlertDialog(Donor_Reg.this, SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("Oops!")
                                .setContentText("Check your network connection and try again!")
                                .showCancelButton(true)
                                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sDialog) {
                                        sDialog.cancel();
                                        finish();
                                    }
                                })
                                .show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("age", age);
                params.put("weight", weight);
                params.put("phone", phone);
                params.put("name", name);
                params.put("donor_id", donorid);

                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(Donor_Reg.this);
        requestQueue.add(stringRequest);
    }

    @Override
    public void onClick(View view) {
        if (view==submit)
        {
            String d_age=age.getText().toString();
            String d_weight=weight.getText().toString();
            String d_phone=phone.getText().toString();
            String d_id=donor_id.getText().toString();
            String d_name=donor_name.getText().toString();
            if(d_age.equals("")||d_name.equals("")||d_phone.equals("")||d_weight.equals(""))
            {
                Toast.makeText(this, "Fill all the fields!", Toast.LENGTH_LONG).show();
            }
            else
                {
                regDonor(d_name, d_age, d_weight, d_phone, d_id);
            }
        }
    }
}
