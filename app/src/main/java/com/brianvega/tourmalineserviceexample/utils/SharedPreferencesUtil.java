package com.brianvega.tourmalineserviceexample.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.brianvega.tourmalineserviceexample.Constants;

public class SharedPreferencesUtil {

    private static String MY_EMAIL = "email";
    private static String MY_PASSWORD = "password";

    public static void saveEmail(Context context, String email) {
        SharedPreferences.Editor editor = context.getSharedPreferences(MY_EMAIL, Context.MODE_PRIVATE).edit();
        editor.putString("email", email);
        editor.apply();
    }

    public static String getEmail(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(MY_EMAIL, Context.MODE_PRIVATE);
        return prefs.getString("email", null);
    }

    public static void savePassword(Context context, String password) {
        SharedPreferences.Editor editor = context.getSharedPreferences(MY_PASSWORD, Context.MODE_PRIVATE).edit();
        editor.putString("password", password);
        editor.apply();
    }

    public static String getPassword(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(MY_PASSWORD, Context.MODE_PRIVATE);
        return prefs.getString("password", null);
    }

}
