package com.brianvega.tourmalineserviceexample.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.brianvega.tourmalineserviceexample.Constants;

public class SharedPreferencesUtil {

    public static String MY_PREFS_NAME = "email";

    public static void saveEmail(Context context, String email) {
        SharedPreferences.Editor editor = context.getSharedPreferences(MY_PREFS_NAME, Context.MODE_PRIVATE).edit();
        editor.putString("email", email);
        editor.apply();
    }

    public static String getEmail(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(MY_PREFS_NAME, Context.MODE_PRIVATE);
        return prefs.getString("email", Constants.TEXT_NO_EMAIL_DEFINED);
    }

}
