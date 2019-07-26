package com.swahilibox.mobile.vision.qrcode.login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.swahilibox.mobile.vision.qrcode.LoginDataBaseAdapter;
import com.swahilibox.mobile.vision.qrcode.R;
import com.swahilibox.mobile.vision.qrcode.login.login.Login;

import java.util.concurrent.TimeUnit;

import cn.pedant.SweetAlert.SweetAlertDialog;
import es.dmoral.toasty.Toasty;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    LoginDataBaseAdapter loginDataBaseAdapter;
    private static final String TAG = "Camera";
    SweetAlertDialog pDialog;
    EditText etPhone;
    Button btSendOtp, btResendOtp;
    ProgressDialog progressDialog;

    //creating a field of firebase auth
    private FirebaseAuth mAuth;
    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    String mVerificationId;
    PhoneAuthProvider.ForceResendingToken mResendToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainx);
        try {
            if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED ) {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{android.Manifest.permission.READ_PHONE_STATE}, 101);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        init();
        //auth instance
        mAuth = FirebaseAuth.getInstance();

        //auth initialisation
        initFirebaseCallBacks();
        loginDataBaseAdapter=new LoginDataBaseAdapter(this);
        loginDataBaseAdapter=loginDataBaseAdapter.open();
    }

    private void initFirebaseCallBacks() {
        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                pDialog.dismiss();
                Toast.makeText(MainActivity.this, "Verification Complete", Toast.LENGTH_LONG).show();
                Log.d(TAG, "onVerificationCompleted:" + credential);
                signInWithPhoneAuthCredential(credential);

            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                pDialog.dismiss();
                pDialog.dismiss();
                Log.w(TAG, "onVerificationFailed", e);

                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    // Invalid request
                    // ...
                    Log.w(TAG, "invalid request", e);
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded
                    // ...
                }
                Toast.makeText(MainActivity.this, "Verification Failed", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCodeSent(String verificationId,
                                   PhoneAuthProvider.ForceResendingToken token) {

                Toast.makeText(MainActivity.this, "Code Sent", Toast.LENGTH_SHORT).show();
                pDialog.setTitleText("Waiting for SMS!");
                pDialog.show();
                mVerificationId = verificationId;
                mResendToken = token;
                Log.d(TAG, "onVerificationCompleted:" + mVerificationId);
            }
        };
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");

                            //Toast.makeText(Camera.this, "", Toast.LENGTH_SHORT).show();
                            loginDataBaseAdapter.insertEntry(etPhone.getText().toString(), "","user");
                            //FirebaseUser user = task.getResult().getUser();
                            Intent intent = new Intent(getApplicationContext(), Login.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);

//                            // ...
                        } else {
                            // Sign in failed, display a message and update the UI
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                                Toast.makeText(MainActivity.this, "Wrong credentials,please confirm verification code and reenter!!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }

    //initialising fields
    private void init() {
        etPhone = findViewById(R.id.et_phone);
//        etOtp = findViewById(R.id.et_otp);
       pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#2a628e"));
        pDialog.setTitleText("Please Wait...");
        pDialog.setCancelable(false);

        btSendOtp = findViewById(R.id.bt_send_otp);
        btResendOtp = findViewById(R.id.bt_resend_otp);
        btResendOtp.setOnClickListener(this);
        btSendOtp.setOnClickListener(this);
    }


    //performing action
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_send_otp:

                if(etPhone.getText().toString().equals(""))
                {
                    Toasty.error(this, "Enter your phone number please!", Toast.LENGTH_LONG).show();
                }
                else {
                    pDialog.show();
                    PhoneAuthProvider.getInstance().verifyPhoneNumber(
                            etPhone.getText().toString(),        // Phone number to verify
                            1,                 // Timeout duration
                            TimeUnit.MINUTES,   // Unit of timeout
                            this,               // Activity (for callback binding)
                            mCallbacks);
                }
                break;
            case R.id.bt_resend_otp:
                if(etPhone.getText().toString().equals(""))
                {
                    Toasty.error(this, "Enter your phone number please!", Toast.LENGTH_LONG).show();
                }
                else {
                    pDialog.show();
                    PhoneAuthProvider.getInstance().verifyPhoneNumber(
                            etPhone.getText().toString(),        // Phone number to verify
                            1,               // Timeout duration
                            TimeUnit.MINUTES,   // Unit of timeout
                            this,               // Activity (for callback binding)
                            mCallbacks,         // OnVerificationStateChangedCallbacks
                            mResendToken);
                }
                break;
//            case R.id.bt_verify_otp:
//                PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, etOtp.getText().toString());
//                mAuth.signInWithCredential(credential)
//                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//                            @Override
//                            public void onComplete(@NonNull Task<AuthResult> task) {
//                                if (task.isSuccessful()) {
//                                    Toast.makeText(Camera.this, "Verification Success", Toast.LENGTH_SHORT).show();
//                                } else {
//                                    if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
//                                        Toast.makeText(Camera.this, "Verification Failed, Invalid credentials", Toast.LENGTH_SHORT).show();
//                                    }
//                                }
//                            }
//                        });
//                break;

        }
    }

}
