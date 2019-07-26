package com.swahilibox.mobile.vision.qrcode.login.login;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.swahilibox.mobile.vision.qrcode.LoginDataBaseAdapter;
import com.swahilibox.mobile.vision.qrcode.MainActivity;
import com.swahilibox.mobile.vision.qrcode.R;
import com.swahilibox.mobile.vision.qrcode.login.events.CancelledEvent;
import com.swahilibox.mobile.vision.qrcode.login.events.PasswordErrorEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;
import es.dmoral.toasty.Toasty;


public class Login extends AppCompatActivity implements LoginView {
    private LoginPresenter loginPresenter;
    TelephonyManager telephonyManager;
    LoginDataBaseAdapter loginDataBaseAdapter;
    int attempts = 0;
    int rem = 4;
    // UI references.
    private AutoCompleteTextView phone;
    private EditText mPasswordView;
    private TextView rem_login;
    private View mProgressView;
    private View mLoginFormView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        telephonyManager = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
        loginDataBaseAdapter = new LoginDataBaseAdapter(this);
        loginDataBaseAdapter = loginDataBaseAdapter.open();
        // Set up the login form.
        phone = (AutoCompleteTextView) findViewById(R.id.phone);
        rem_login = (TextView) findViewById(R.id.rem_login);
        phone.setText(loginDataBaseAdapter.getSinlgeEntry1());
        mPasswordView = (EditText) findViewById(R.id.password);

        Button mEmailSignInButton = (Button) findViewById(R.id.phone_sign_in_button);
        mEmailSignInButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);

        loginPresenter = new LoginPresenterImplementation(this);
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        // Reset errors.
        phone.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String username = phone.getText().toString();
        String password = mPasswordView.getText().toString();
        if(username.equals("")||password.equals(""))
        {
           Toasty.error(this, "The phone number and password are required!", Toast.LENGTH_LONG).show();
        }
        else {
            LoginValidation(username, password);
        }
        //loginPresenter.validateCredentials(username,password);
        // Toast.makeText(this, "Go To Home", Toast.LENGTH_LONG).show();
        //startActivity(new Intent(getApplicationContext(), MapLocation.class));


    }

    private void LoginValidation(final String username, final String password) {
        final SweetAlertDialog pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#2a628e"));
        pDialog.setTitleText("Verifying...Please Wait!");
        pDialog.setCancelable(false);
        pDialog.show();
        final String storedname = loginDataBaseAdapter.getSinlgeEntry1();


        //Creating a string request
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://alpha.nupola.com/rc_login.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        pDialog.dismiss();
                        if (response.contains("success")) {
                            Toasty.success(Login.this, "Access Granted!", Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(Login.this, MainActivity.class);
                            startActivity(intent);
                            finish();


                        } else if (response.contains("failed")) {
                          rem_login.setVisibility(View.VISIBLE);
                            Toasty.error(Login.this, "Access Denied!", Toast.LENGTH_SHORT).show();
                            attempts = attempts + 1;
                            rem = rem - 1;
                            if(rem==0)
                            {
                                rem_login.setText("Your account will be blocked! Last attempt!");
                            }
                            else {
                                rem_login.setText("You've " + rem + " attempts remaining!");
                            }
                        }
                    }

                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pDialog.dismiss();
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Login.this);
                        alertDialogBuilder.setMessage("Network connection problem please retry again!: Error:- "+error.getMessage());
                        alertDialogBuilder.setPositiveButton("TRY AGAIN",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface arg0, int arg1) {

                                        LoginValidation(username, password);
                                    }
                                });

                        alertDialogBuilder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();

                            }
                        });

                        AlertDialog alertDialog = alertDialogBuilder.create();
                        alertDialog.show();
                        //You can handle error here if you want
                        //Toast.makeText(Manage_data.this, "Network connection error!", Toast.LENGTH_SHORT).show();
                        // progress.dismiss();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                    try {
                        if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED ) {
                            ActivityCompat.requestPermissions(Login.this, new String[]{android.Manifest.permission.READ_PHONE_STATE}, 101);
                        }
                    } catch (Exception e){
                        e.printStackTrace();
                    }
                    final String token = telephonyManager.getDeviceId();

                params.put("user_phone", storedname);
                params.put("password", password);
                params.put("attempts", attempts+"");
                params.put("token", token);


                //returning parameter
                return params;
            }
        };

        //Adding the string request to the queue
        RequestQueue requestQueue = Volley.newRequestQueue(Login.this);
        requestQueue.add(stringRequest);
    }
    @Override
    public void showProgress( final boolean showProgress) {
        int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

        mLoginFormView.setVisibility(showProgress? View.GONE : View.VISIBLE);
        mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                showProgress ? 0 : 1).setListener(new AnimatorListenerAdapter() {

            @Override
            public void onAnimationEnd(Animator animation) {
                mLoginFormView.setVisibility(showProgress ? View.GONE : View.VISIBLE);
            }
        });

        mProgressView.setVisibility(showProgress ? View.VISIBLE : View.GONE);
        mProgressView.animate().setDuration(shortAnimTime).alpha(
                showProgress ? 1 : 0).setListener(new AnimatorListenerAdapter() {

            @Override
            public void onAnimationEnd(Animator animation) {
                mProgressView.setVisibility(showProgress ? View.VISIBLE : View.GONE);
            }
        });
    }

    @Override
    public void setUsernameError(int messageResId) {
        phone.setError(getString(messageResId));
        //want the user to make changes
        phone.requestFocus();
    }

    @Override
    public void setPasswordError(int messageResId) {
        mPasswordView.setError(getString(messageResId));
        mPasswordView.requestFocus();

    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onPasswordErrorEvent(PasswordErrorEvent passwordErrorEvent) {
        showProgress(false);
        setPasswordError(R.string.error_incorrect_password);


    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCanceledEvent(CancelledEvent cancelledEvent) {
        showProgress(false);

    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }
}
