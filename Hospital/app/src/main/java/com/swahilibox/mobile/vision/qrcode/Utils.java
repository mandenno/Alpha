package com.swahilibox.mobile.vision.qrcode;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

public class Utils {

	static Context context;
	public static final String TAG = "Utils";
	public static final String UserName = "UserName";

	public static final String PROPERTY_REG_ID = "registration_id";
	private static final String PROPERTY_APP_VERSION = "appVersion";

	public Utils(Context context) {
		Utils.context = context;
	}

	public SharedPreferences getGCMPreferences() {
		return context.getSharedPreferences(((AppCompatActivity) context)
				.getClass().getSimpleName(), Context.MODE_PRIVATE);
	}

	public void savePreferences(String key, String value) {
		final SharedPreferences prefs = getGCMPreferences();
		Log.i(TAG, key + " : " + value);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putString(key, value);
		editor.commit();
	}

	public String getFromPreferences(String key) {
		final SharedPreferences prefs = getGCMPreferences();
		String value = prefs.getString(key, "");
		if (value.isEmpty()) {
			Log.i(TAG, key + " not found.");
			return "";
		}
		return value;
	}

	String getRegistrationId() {
		final SharedPreferences prefs = getGCMPreferences();
		String registrationId = prefs.getString(PROPERTY_REG_ID, "");
		if (registrationId.isEmpty()) {
			Log.i(TAG, "Registration not found.");
			return "";
		}
		// Check if app was updated; if so, it must clear the registration ID
		// since the existing regID is not guaranteed to work with the new
		// app version.
		int registeredVersion = prefs.getInt(PROPERTY_APP_VERSION,
				Integer.MIN_VALUE);
		int currentVersion = getAppVersion();
		if (registeredVersion != currentVersion) {
			Log.i(TAG, "App version changed.");
			return "";
		}
		return registrationId;
	}

	static int getAppVersion() {
		try {
			PackageInfo packageInfo = context.getPackageManager()
					.getPackageInfo(context.getPackageName(), 0);
			return packageInfo.versionCode;
		} catch (PackageManager.NameNotFoundException e) {
			// should never happen
			throw new RuntimeException("Could not get package name: " + e);
		}
	}

	public void storeRegistrationId(String regId) {
		final SharedPreferences prefs = getGCMPreferences();
		int appVersion = Utils.getAppVersion();
		Log.i(TAG, "Saving regId on app version " + appVersion);
		Log.i(TAG, "Reg ID : " + regId);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putString(PROPERTY_REG_ID, regId);
		editor.putInt(PROPERTY_APP_VERSION, appVersion);
		editor.commit();
	}

	public String getCurrentIPAddress() {
		return "http://www.tbcapp.org/";
	}

	public void showToast(final String txt) {
		((Activity) context).runOnUiThread(new Runnable() {
			public void run() {
				Toast.makeText(context, txt, Toast.LENGTH_LONG).show();
			}
		});

	}

}
