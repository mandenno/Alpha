package com.swahilibox.mobile.vision.qrcode.login.login;

import android.app.ProgressDialog;
import android.text.TextUtils;
import android.widget.ProgressBar;

import com.swahilibox.mobile.vision.qrcode.R;


public class LoginPresenterImplementation implements LoginPresenter {
    private LoginModel loginModel;
    private LoginView loginView;


    public LoginPresenterImplementation(LoginView loginView) {
        this.loginView = loginView;
        this.loginModel = new LoginModelImplementation();
    }
    @Override
    public void validateCredentials(String phone, String password) {
        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            loginView.setPasswordError(R.string.error_invalid_password);
            return;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(phone)) {
            loginView.setUsernameError(R.string.error_field_required);
            return;

        } else if (!isPhoneValid(phone)) {
            loginView.setUsernameError(R.string.error_invalid_phone_number);
            return;
        }


//        loginView.showProgress(true);
         loginModel.login(phone,password);

    }
    private boolean isPhoneValid(String phone) {

        return phone.contains("+254");
    }

    private boolean isPasswordValid(String password) {

        return password.length() > 4;
    }
}
