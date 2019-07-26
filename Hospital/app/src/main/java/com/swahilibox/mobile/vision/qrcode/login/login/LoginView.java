package com.swahilibox.mobile.vision.qrcode.login.login;

public interface LoginView {
    void showProgress(boolean showProgress);
    void setUsernameError(int messageResId);
    void setPasswordError(int messageResId);
}
