package com.swahilibox.mobile.vision.qrcode;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;
import es.dmoral.toasty.Toasty;

public class Business_Details extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener{
EditText case_no, courtdate, bus_desc, cust_name, cust_phone;
    LoginDataBaseAdapter loginDataBaseAdapter;
    Button lodge;
    public static String service_type="";
    public static final String URL_LODGE= "https://www.fastagas.com/swahilibox/lodge_case.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business__details);
        loginDataBaseAdapter=new LoginDataBaseAdapter(this);
        loginDataBaseAdapter=loginDataBaseAdapter.open();
        lodge=(Button)findViewById(R.id.lodge);
        lodge.setOnClickListener(this);
        case_no=(EditText)findViewById(R.id.case_no);
        bus_desc=(EditText)findViewById(R.id.bus_desc);
        cust_name=(EditText)findViewById(R.id.cust_name);
        cust_phone=(EditText)findViewById(R.id.cust_phone);
        courtdate=(EditText)findViewById(R.id.court_date);
        String caseno=Business_Details.this.getIntent().getExtras().getString("case_no");
        String court_date=Business_Details.this.getIntent().getExtras().getString("court_date");
        case_no.setText(caseno);
        courtdate.setText(court_date);
        case_no.setEnabled(false);
        courtdate.setEnabled(false);



    }

    public void lodgeCase()
    {

        final SweetAlertDialog pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#FFC107"));
        pDialog.setTitleText("Lodging Case...");
        pDialog.setCancelable(false);
        pDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_LODGE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        pDialog.dismiss();
                        if(response.contains("right"))
                        {

                            new SweetAlertDialog(Business_Details.this, SweetAlertDialog.SUCCESS_TYPE)
                                    .setTitleText("Case Lodged!")
                                    .setContentText("The case has been Lodged successfully!")
                                    .show();
                            Intent intent = new Intent(Business_Details.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }

                        else
                        {

                            new SweetAlertDialog(Business_Details.this, SweetAlertDialog.ERROR_TYPE)
                                    .setTitleText("Oops...")
                                    .setContentText("We have encountered a technical error! Please try again later.")
                                    .show();
                            finish();

                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pDialog.dismiss();
                        new SweetAlertDialog(Business_Details.this, SweetAlertDialog.ERROR_TYPE)
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
                params.put("case_no", Business_Details.this.getIntent().getExtras().getString("case_no"));
                params.put("court_date", Business_Details.this.getIntent().getExtras().getString("court_date"));
                params.put("cname", cust_phone.getText().toString());
                params.put("cphone", cust_name.getText().toString());
                params.put("desc", bus_desc.getText().toString());

                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(Business_Details.this);
        requestQueue.add(stringRequest);
    }

    @Override
    public void onClick(View view) {
        if(view==lodge)
        {


            new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("Lodge Case")
                    .setContentText("Submit this Case?")
                    .setCancelText("CANCEL")
                    .setConfirmText("SUBMIT")
                    .showCancelButton(true)
                    .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            sDialog.cancel();
                        }
                    })
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                        lodgeCase();
                            sDialog.cancel();
                        }
                    })
                    .show();

        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        String item = parent.getItemAtPosition(position).toString();
        service_type=item;
        ((TextView) parent.getChildAt(0)).setTextColor(Color.WHITE);
        ((TextView) parent.getChildAt(0)).setTextSize(5);

    }

    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub

    }
}
