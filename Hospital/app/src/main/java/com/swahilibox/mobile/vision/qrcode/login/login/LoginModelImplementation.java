package com.swahilibox.mobile.vision.qrcode.login.login;

import android.os.AsyncTask;

import com.swahilibox.mobile.vision.qrcode.login.events.CancelledEvent;
import com.swahilibox.mobile.vision.qrcode.login.events.PasswordErrorEvent;
import com.swahilibox.mobile.vision.qrcode.login.events.SuccessEvent;

import org.greenrobot.eventbus.EventBus;


public class LoginModelImplementation implements LoginModel{
    public static final String[] DUMMY_CREDENTIAL = new String[]{
            "+254728638230:password", "+254725634225:password"
    };

    @Override
    public void login(String phone, String password) {
        new UserLoginTask(phone,password).execute();
    }
    public class UserLoginTask extends AsyncTask<Void,Void,Boolean>{

        private String phone;
        private String password;

        public UserLoginTask(String phone,String password){
            this.phone = phone;
            this.password = password;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            try {

                Thread.sleep(2000);
            } catch (InterruptedException e) {
                return false;
            }

            for (String credential : DUMMY_CREDENTIAL) {
                String[] pieces = credential.split(":");
                if (pieces[0].equals(phone)) {
                    // Account exists, return true if the password matches.
                    return pieces[1].equals(password);
                }
            }



            return false;
        }

        @Override
        protected void onPostExecute(Boolean success) {
            super.onPostExecute(success);
            if (success) {
                EventBus.getDefault().post(new SuccessEvent());
            } else {
                EventBus.getDefault().post(new PasswordErrorEvent());

            }
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            EventBus.getDefault().post(new CancelledEvent());

        }
    }
}
