package com.swahilibox.mobile.vision.qrcode;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.swahilibox.mobile.vision.qrcode.login.api.ApiClient;
import com.swahilibox.mobile.vision.qrcode.login.api.model.AccessToken;
import com.swahilibox.mobile.vision.qrcode.login.api.model.STKPush;
import com.swahilibox.mobile.vision.qrcode.login.util.SharedPrefsUtil;
import com.swahilibox.mobile.vision.qrcode.login.util.Utils;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

import static com.swahilibox.mobile.vision.qrcode.login.util.AppConstants.BUSINESS_SHORT_CODE;
import static com.swahilibox.mobile.vision.qrcode.login.util.AppConstants.CALLBACKURL;
import static com.swahilibox.mobile.vision.qrcode.login.util.AppConstants.PARTYB;
import static com.swahilibox.mobile.vision.qrcode.login.util.AppConstants.PASSKEY;
import static com.swahilibox.mobile.vision.qrcode.login.util.AppConstants.TRANSACTION_TYPE;

public class ErrorSite extends AppCompatActivity implements View.OnClickListener {
Button pay, lodge;
    private String code = "+254";
    Button btn_pay_fine;
    private ProgressDialog mProgressDialog;
    private ApiClient mApiClient;
    private SharedPrefsUtil mSharedPrefsUtil;
    private String mFireBaseRegId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_error_site);
        pay=(Button)findViewById(R.id.payx);
        lodge=(Button)findViewById(R.id.lodge_case);
        pay.setOnClickListener(this);
        lodge.setOnClickListener(this);
        mProgressDialog = new ProgressDialog(this);
        mSharedPrefsUtil = new SharedPrefsUtil(this);
        mApiClient = new ApiClient();
        mApiClient.setIsDebug(true); //Set True to enable logging, false to disable.

        getAccessToken();

        getFirebaseRegId();
    }

    private void getFirebaseRegId() {
        mFireBaseRegId = mSharedPrefsUtil.getFirebaseRegistrationID();

        if (!TextUtils.isEmpty(mFireBaseRegId)) {
            mSharedPrefsUtil.saveFirebaseRegistrationID(mFireBaseRegId);
        }
    }
    public void getAccessToken() {
        mApiClient.setGetAccessToken(true);
        mApiClient.mpesaService().getAccessToken().enqueue(new Callback<AccessToken>() {
            @Override
            public void onResponse(@NonNull Call<AccessToken> call, @NonNull Response<AccessToken> response) {

                if (response.isSuccessful()) {
                    mApiClient.setAuthToken(response.body().accessToken);
                }
            }

            @Override
            public void onFailure(@NonNull Call<AccessToken> call, @NonNull Throwable t) {

            }
        });
    }

    @Override
    public void onClick(View view) {
        if(view==pay)
        {

            showCheckoutDialog();



        }
        else if(view==lodge)
        {
            Intent intent = new Intent(ErrorSite.this, Business_Details.class);
            intent.putExtra("case_no", ErrorSite.this.getIntent().getExtras().getString("case_no"));
            intent.putExtra("court_date", ErrorSite.this.getIntent().getExtras().getString("court_date") );
            startActivity(intent);
            finish();
        }
    }

    public void showCheckoutDialog() {

        final Dialog dialog = new Dialog(this);

        dialog.setContentView(R.layout.pay_fine_dialog);
        Button payNOw, clear;
        final EditText phone = dialog.findViewById(R.id.et_phone_dialog);
        phone.setInputType(InputType.TYPE_CLASS_PHONE);
        phone.setHint(getString(R.string.hint_phone_number));
        payNOw = (Button) dialog.findViewById(R.id.btn_pay_now);
        clear = (Button) dialog.findViewById(R.id.clear);
        Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.TOP);
        dialog.show();

        payNOw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phone_number = phone.getText().toString();
                performSTKPush(phone_number);
            }
        });

        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog.cancel();
            }
        });


    }
    public void performSTKPush(String phone_number) {
        mProgressDialog.setMessage(getString(R.string.dialog_message_processing));
        mProgressDialog.setTitle(getString(R.string.title_wait));
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.show();
        String timestamp = com.swahilibox.mobile.vision.qrcode.login.util.Utils.getTimestamp();
        STKPush stkPush = new STKPush(
                BUSINESS_SHORT_CODE,
                com.swahilibox.mobile.vision.qrcode.login.util.Utils.getPassword(BUSINESS_SHORT_CODE, PASSKEY, timestamp),
                timestamp,
                TRANSACTION_TYPE,
               1,
                com.swahilibox.mobile.vision.qrcode.login.util.Utils.sanitizePhoneNumber(phone_number),
                PARTYB,
                Utils.sanitizePhoneNumber(phone_number),
                CALLBACKURL + mFireBaseRegId,
                "test", //The account reference
                "test"  //The transaction description
        );

        mApiClient.setGetAccessToken(false);

        mApiClient.mpesaService().sendPush(stkPush).enqueue(new Callback<STKPush>() {
            @Override
            public void onResponse(@NonNull Call<STKPush> call, @NonNull Response<STKPush> response) {
                mProgressDialog.dismiss();
                try {
                    if (response.isSuccessful()) {
                        Timber.d("post submitted to API. %s", response.body());
                        new SweetAlertDialog(ErrorSite.this, SweetAlertDialog.SUCCESS_TYPE)
                                .setTitleText("PUSH SENT")
                                .setContentText("Waiting for Customer to pay!")
                                .setConfirmText("OK")
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sDialog) {
                                        Intent intent = new Intent(ErrorSite.this, MainActivity.class);
                                        startActivity(intent);
                                        finish();
                                        sDialog.cancel();
                                    }
                                })
                                .show();
                    } else {
                        Timber.e("Response %s", response.errorBody().string());
                        new SweetAlertDialog(ErrorSite.this, SweetAlertDialog.WARNING_TYPE)
                                .setTitleText("FAILED")
                                .setContentText("Failed to send payment request to customer. Try again!")
                                .setConfirmText("OK")
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sDialog) {
                                        sDialog.cancel();
                                    }
                                })
                                .show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(@NonNull Call<STKPush> call, @NonNull Throwable t) {
                mProgressDialog.dismiss();
                Timber.e(t);
                new SweetAlertDialog(ErrorSite.this, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("FAILED")
                        .setContentText(t.getMessage())
                        .setConfirmText("OK")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.cancel();
                            }
                        })
                        .show();
            }
        });
    }
}
