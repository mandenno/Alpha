package com.swahilibox.mobile.vision.qrcode;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

public class Biz_No extends AppCompatActivity {

    private EditText edt1;
    private EditText edt2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_biz_no);


        edt1 = findViewById(R.id.edt1);
        edt1.setError("This field should start with characters only");
        edt2 = findViewById(R.id.edt2);
        edt2.setError("The first three inputs should be character followed by numbers");

        edt1.setFilters(new InputFilter[] {
                new InputFilter.LengthFilter(3)});
        edt1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(editable.toString().length() > 2) {
                    edt2.requestFocus();
                    openKeyboard();
                }
            }
        });

        edt2.setFilters(new InputFilter[] {
                new InputFilter.LengthFilter(9)});
        edt2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(final Editable editable) {
                if(editable.toString().length() > 8){
                    removeKeyboard();
                }
            }
        });



    }


    private void openKeyboard(){

        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        if(imm != null) {
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
        }
    }

    private void removeKeyboard(){

        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);

        if(imm != null) {
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }


    }

}
