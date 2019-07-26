package com.swahilibox.mobile.vision.qrcode;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.vision.barcode.Barcode;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;
import es.dmoral.toasty.Toasty;
import info.androidhive.barcode.BarcodeReader;


public class NewScanner extends AppCompatActivity implements BarcodeReader.BarcodeReaderListener, View.OnClickListener {
    String code="";
    BarcodeReader barcodeReader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner);


    }

    @Override
    public void onScanned(final Barcode barcode) {
        Intent intent = new Intent(NewScanner.this, Test.class);
        intent.putExtra("code", barcode.displayValue);
        intent.putExtra("pno", NewScanner.this.getIntent().getExtras().getString("patient_no"));

        startActivity(intent);
        finish();
        Toasty.success(NewScanner.this, "Scanned: "+barcode.displayValue+"", Toast.LENGTH_LONG).show();


    }

    @Override
    public void onScannedMultiple(List<Barcode> list) {

    }

    @Override
    public void onBitmapScanned(SparseArray<Barcode> sparseArray) {

    }


    @Override
    public void onScanError(String s) {
        Toast.makeText(getApplicationContext(), "Error occurred while scanning " + s, Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onClick(View view) {


    }

}
