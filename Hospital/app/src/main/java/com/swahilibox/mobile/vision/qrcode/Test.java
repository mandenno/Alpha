package com.swahilibox.mobile.vision.qrcode;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class Test extends AppCompatActivity {
    LoginDataBaseAdapter loginDataBaseAdapter;
    public static final String URL_SUBMIT = "https://alpha.nupola.com/medic_reg_blood.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        loginDataBaseAdapter=new LoginDataBaseAdapter(this);
        loginDataBaseAdapter=loginDataBaseAdapter.open();
        final String pno=Test.this.getIntent().getExtras().getString("pno");
        final String qr_code=Test.this.getIntent().getExtras().getString("code");

        new SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE)
                .setTitleText("CODE SCANNED!")
                .setContentText("Update blood donation status")
                .setCancelText("CANCEL")
                .setConfirmText("UPDATE")
                .showCancelButton(true)
                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {

                        sDialog.cancel();
                        finish();
                    }
                })
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        submitCode(pno, qr_code);
                        sDialog.cancel();
                    }
                })
                .show();
    }


    public void submitCode(final String pno, final String qr_code )
    {

        final SweetAlertDialog pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#B71C1C"));
        pDialog.setTitleText("Updating...");
        pDialog.setCancelable(false);
        pDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_SUBMIT,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        pDialog.dismiss();
                        if(response.contains("success"))
                        {


                            new SweetAlertDialog(Test.this, SweetAlertDialog.SUCCESS_TYPE)
                                    .setTitleText("Success")
                                    .setContentText("Donation status updated successfully!")
                                    //.setCancelText("CANCEL")
                                    .setConfirmText("OKAY")
                                    //.showCancelButton(true)

                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sDialog) {
                                            finish();
                                            sDialog.cancel();
                                        }
                                    })
                                    .show();

                        }

                        else if(response.contains("failed"))
                        {

                            new SweetAlertDialog(Test.this, SweetAlertDialog.ERROR_TYPE)
                                    .setTitleText("Failed")
                                    .setContentText("We have encountered an error while processing: "+response)
                                    .show();

                        }
                        else if(response.contains("exist"))
                        {

                            new SweetAlertDialog(Test.this, SweetAlertDialog.ERROR_TYPE)
                                    .setTitleText("Error")
                                    .setContentText("The screen records for this blood bag is has already been captured!")
                                    //.setCancelText("CANCEL")
                                    .setConfirmText("CANCEL")
                                    //.showCancelButton(true)

                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sDialog) {
                                            finish();
                                            sDialog.cancel();
                                        }
                                    })
                                    .show();

                        }
                        else
                        {

                            new SweetAlertDialog(Test.this, SweetAlertDialog.ERROR_TYPE)
                                    .setTitleText("Oops...")
                                    .setContentText("We have encountered a technical error! Please try again later."+response)
                                    .show();
                            //finish();

                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pDialog.dismiss();
                        new SweetAlertDialog(Test.this, SweetAlertDialog.ERROR_TYPE)
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
                params.put("user_phone", loginDataBaseAdapter.getSinlgeEntry1());
                params.put("pno", pno);
                params.put("qr_code", qr_code);

                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(Test.this);
        requestQueue.add(stringRequest);
    }
}
