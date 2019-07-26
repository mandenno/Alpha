package com.swahilibox.mobile.vision.qrcode;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import cn.pedant.SweetAlert.SweetAlertDialog;
import es.dmoral.toasty.Toasty;

public class Dono_Id extends AppCompatActivity implements View.OnClickListener {
Button next;
EditText pno, notes;

    String status="";
    GpsTracker gps;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        setContentView(R.layout.activity_donorid);
        pno=(EditText)findViewById(R.id.notes);
        next=(Button)findViewById(R.id.next);
        next.setOnClickListener(this);
        Toolbar toolbar = findViewById(R.id.toolbar_donorid);
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


    }

    @Override
    public void onClick(View view) {
        if(view==next) {
            if (pno.getText().toString().equals("")) {
                Toasty.error(this, "Fill all the fields", Toast.LENGTH_SHORT).show();
            } else {


                new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Scan QR!")
                        .setContentText("SCAN the Blood bag QR code")
                        .setCancelText("CANCEL")
                        .setConfirmText("SCAN")
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
                                Intent intent = new Intent(Dono_Id.this, NewScanner.class);
                                intent.putExtra("patient_no", pno.getText().toString());

                                startActivity(intent);
                                sDialog.cancel();
                            }
                        })
                        .show();


            }
        }

    }
}
