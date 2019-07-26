/*
 *
 *  * Copyright (C) 2017 Safaricom, Ltd.
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  * http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */

package com.swahilibox.mobile.vision.qrcode.login.ui.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessaging;
import com.swahilibox.mobile.vision.qrcode.R;
import com.swahilibox.mobile.vision.qrcode.TextDrawable;
import com.swahilibox.mobile.vision.qrcode.login.api.ApiClient;
import com.swahilibox.mobile.vision.qrcode.login.api.model.AccessToken;
import com.swahilibox.mobile.vision.qrcode.login.api.model.STKPush;
import com.swahilibox.mobile.vision.qrcode.login.ui.RecyclerviewListDecorator;
import com.swahilibox.mobile.vision.qrcode.login.ui.adapter.CartListAdapter;
import com.swahilibox.mobile.vision.qrcode.login.ui.callback.PriceTransfer;
import com.swahilibox.mobile.vision.qrcode.login.util.NotificationUtils;
import com.swahilibox.mobile.vision.qrcode.login.util.SharedPrefsUtil;
import com.swahilibox.mobile.vision.qrcode.login.util.Utils;


import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

import static com.swahilibox.mobile.vision.qrcode.login.util.AppConstants.BUSINESS_SHORT_CODE;
import static com.swahilibox.mobile.vision.qrcode.login.util.AppConstants.CALLBACKURL;
import static com.swahilibox.mobile.vision.qrcode.login.util.AppConstants.PARTYB;
import static com.swahilibox.mobile.vision.qrcode.login.util.AppConstants.PASSKEY;
import static com.swahilibox.mobile.vision.qrcode.login.util.AppConstants.PUSH_NOTIFICATION;
import static com.swahilibox.mobile.vision.qrcode.login.util.AppConstants.REGISTRATION_COMPLETE;
import static com.swahilibox.mobile.vision.qrcode.login.util.AppConstants.TOPIC_GLOBAL;
import static com.swahilibox.mobile.vision.qrcode.login.util.AppConstants.TRANSACTION_TYPE;


public class MainActivity extends AppCompatActivity implements PriceTransfer {

    @BindView(R.id.cart_list)
    RecyclerView mRecyclerViewCartList;
    @BindView(R.id.txt_response)
    TextView mTVResponse;
    @BindView(R.id.buttonCheckout)
    Button mButtonCheckout;
    private String code = "+254";
    private String mFireBaseRegId;
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private ProgressDialog mProgressDialog;
    private SharedPrefsUtil mSharedPrefsUtil;
    private ApiClient mApiClient;
    private ArrayList<Integer> mPriceArrayList = new ArrayList<>();
    EditText text;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_pay);
        ButterKnife.bind(this);
        text = findViewById(R.id.et_phone);
        text.setCompoundDrawablesWithIntrinsicBounds(new TextDrawable(code), null, null, null);
        text.setCompoundDrawablePadding(code.length()*10);

        mProgressDialog = new ProgressDialog(this);
        mSharedPrefsUtil = new SharedPrefsUtil(this);
        mApiClient = new ApiClient();
        mApiClient.setIsDebug(true); //Set True to enable logging, false to disable.

        getAccessToken();

        LinearLayoutManager layoutManager = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.VERTICAL, false);

        mRecyclerViewCartList.setLayoutManager(layoutManager);
        mRecyclerViewCartList.addItemDecoration(new RecyclerviewListDecorator(MainActivity.this,
                LinearLayoutManager.HORIZONTAL));

        ArrayList<String> cartItems = new ArrayList<>();
        cartItems.add("Tomatoes");
        cartItems.add("Apples");
        cartItems.add("Bananas");

        ArrayList<String> cartPrices = new ArrayList<>();
        cartPrices.add("1");
        cartPrices.add("200");
        cartPrices.add("120");

        mRecyclerViewCartList.setAdapter(new CartListAdapter(this, cartItems, cartPrices, MainActivity.this));

        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                // checking for type intent filter
                if (intent.getAction().equals(REGISTRATION_COMPLETE)) {
                    // gcm successfully registered
                    // now subscribe to `global` topic to receive app wide notifications
                    FirebaseMessaging.getInstance().subscribeToTopic(TOPIC_GLOBAL);
                    getFirebaseRegId();

                } else if (intent.getAction().equals(PUSH_NOTIFICATION)) {
                    String message = intent.getStringExtra("message");
                    NotificationUtils.createNotification(getApplicationContext(), message);
                    showResultDialog(message);
                }
            }
        };

        getFirebaseRegId();
    }

    @OnClick({R.id.buttonCheckout})
    public void onClickViews(View view) {
        switch (view.getId()) {
            case R.id.buttonCheckout:
                if (mPriceArrayList.size() > 0)
                    //Calling getPhoneNumber method.
                    showCheckoutDialog();
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);//Menu Resource, Menu
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        unregisterReceiver(mRegistrationBroadcastReceiver);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_checkout:
                Snackbar.make(findViewById(R.id.pay_layout), "Item 1 Selected", Snackbar.LENGTH_LONG)
                        .setActionTextColor(Color.RED)
                        .show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
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

    public void showCheckoutDialog() {

        final Dialog dialog = new Dialog(this);

        dialog.setContentView(R.layout.pay_fine_dialog);
        Button payNOw, clear;
        final EditText phone = dialog.findViewById(R.id.et_phone_dialog);
        phone.setInputType(InputType.TYPE_CLASS_PHONE);
        phone.setHint(getString(R.string.hint_phone_number));
        phone.setCompoundDrawablesWithIntrinsicBounds(new TextDrawable(code), null, null, null);
        phone.setCompoundDrawablePadding(code.length()*10);
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
                mPriceArrayList.clear();
                mButtonCheckout.setText(getString(R.string.checkout));
                dialog.cancel();
            }
        });


    }

    public void performSTKPush(String phone_number) {
        mProgressDialog.setMessage(getString(R.string.dialog_message_processing));
        mProgressDialog.setTitle(getString(R.string.title_wait));
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.show();
        String timestamp = Utils.getTimestamp();
        STKPush stkPush = new STKPush(
                BUSINESS_SHORT_CODE,
                Utils.getPassword(BUSINESS_SHORT_CODE, PASSKEY, timestamp),
                timestamp,
                TRANSACTION_TYPE,
                10,
                Utils.sanitizePhoneNumber(phone_number),
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
                    } else {
                        Timber.e("Response %s", response.errorBody().string());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(@NonNull Call<STKPush> call, @NonNull Throwable t) {
                mProgressDialog.dismiss();
                Timber.e(t);
            }
        });
    }

    private void getFirebaseRegId() {
        mFireBaseRegId = mSharedPrefsUtil.getFirebaseRegistrationID();

        if (!TextUtils.isEmpty(mFireBaseRegId)) {
            mSharedPrefsUtil.saveFirebaseRegistrationID(mFireBaseRegId);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        // register GCM registration complete receiver
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(REGISTRATION_COMPLETE));

        // register new push message receiver
        // by doing this, the activity will be notified each time a new message arrives
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(PUSH_NOTIFICATION));

        // clear the notification area when the app is opened
        NotificationUtils.clearNotifications(getApplicationContext());
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }

    @Override
    public void setPrices(ArrayList<Integer> prices) {
        this.mPriceArrayList = prices;
        mButtonCheckout.setText(getString(R.string.checkout_value, getTotal(prices)));
    }

    public int getTotal(ArrayList<Integer> prices) {
        int sum = 0;
        for (int i = 0; i < prices.size(); i++) {
            sum = sum + prices.get(i);
        }

        if (prices.size() == 0) {
            Toast.makeText(MainActivity.this, String.valueOf("Total: " + sum), Toast.LENGTH_SHORT).show();
            return 0;
        } else
            return sum;
    }


    public void showResultDialog(String result) {
        Timber.d(result);
        if (!mSharedPrefsUtil.getIsFirstTime()) {
            // run your one time code
            mSharedPrefsUtil.saveIsFirstTime(true);

            new SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE)
                    .setTitleText(getString(R.string.title_success))
                    .setContentText(getString(R.string.dialog_message_success))
                    .setConfirmText("SCAN QR")
                    .showCancelButton(true)

                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                             sDialog.dismissWithAnimation();
                            mSharedPrefsUtil.saveIsFirstTime(false);
                        }
                    })
                    .show();
        }
    }
}
