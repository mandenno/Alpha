package com.swahilibox.mobile.vision.qrcode;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class PayActivity extends AppCompatActivity {

    private EditText text,bsID,pay_fine;
    private String code = "+254";
    Button btn_pay_fine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pay_activity);
        //instances
        text = findViewById(R.id.et_phone);
        bsID = findViewById(R.id.et_business_id);
        pay_fine = findViewById(R.id.et_phone_dialog);
        btn_pay_fine = findViewById(R.id.btn_pay_fine);
        final Dialog dialog = new Dialog(this);

        //phone
        text.setCompoundDrawablesWithIntrinsicBounds(new TextDrawable(code), null, null, null);
        text.setCompoundDrawablePadding(code.length()*10);

        //business ID
        InputFilter filter = new InputFilter() {
            public CharSequence filter(CharSequence source, int start,
                                       int end, Spanned dest, int dstart, int dend) {

                for (int i = start;i < end;i++) {
                    if (!Character.isLetterOrDigit(source.charAt(i)) &&
                            !Character.toString(source.charAt(i)).equals("_") &&
                            !Character.toString(source.charAt(i)).equals("-"))
                    {
                        return "";
                    }
                }
                return null;
            }
        };


        bsID.setFilters(new InputFilter[] { filter });


//paying fine
        btn_pay_fine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.setContentView(R.layout.pay_fine_dialog);
                Button payNOw;
                final EditText phone = dialog.findViewById(R.id.et_phone_dialog);
                phone.setCompoundDrawablesWithIntrinsicBounds(new TextDrawable(code), null, null, null);
                phone.setCompoundDrawablePadding(code.length()*10);
                payNOw = (Button) dialog.findViewById(R.id.btn_pay_now);
                Window window = dialog.getWindow();
                window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                window.setGravity(Gravity.TOP);
                dialog.show();

                payNOw.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // perform your action here
                        Intent intent = new Intent(PayActivity.this, PayActivity.class);
                        intent.putExtra("phone", phone.getText().toString());
                        startActivity(intent);
                    }
                });

            }
        });


    }

}
