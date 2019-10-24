package com.brianvega.tourmalineserviceexample.tourmalineService;

import android.content.Context;
import android.content.SharedPreferences;

public class Preferences {

    public static Preferences instance;

    private Preferences(final SharedPreferences preferences){ this.preferences = preferences; }

    public static synchronized Preferences getInstance(Context context) {
        if(instance == null){
            final SharedPreferences preferences = context.getSharedPreferences("tourmalinelabs", Context.MODE_PRIVATE);
            instance = new Preferences(preferences);
        }
        return instance;
    }

    private SharedPreferences preferences;

    public boolean containsKey(final String key) {
        return preferences.contains(key);
    }
    public String getString(final String key, final String defaultValue) {
        return preferences.getString(key, defaultValue);
    }
    public int getInt(final String key, final int defaultValue) {
        return preferences.getInt(key, defaultValue);
    }
    public long getLong(final String key, final long defaultValue) {
        return preferences.getLong(key, defaultValue);
    }
    public float getFloat(final String key, final float defaultValue) {
        return preferences.getFloat(key, defaultValue);
    }
    public boolean getBoolean(final String key, boolean defaultValue) {
        return preferences.getBoolean(key, defaultValue);
    }

    public void removeKey(final String key){
        preferences.edit().remove(key).apply();
    }
    public void putString(String key, String value) {
        preferences.edit().putString(key, value).apply();
    }
    public void putInt(String key, int value) {
        preferences.edit().putInt(key, value).apply();
    }
    public void putLong(String key, long value) {
        preferences.edit().putLong(key, value).apply();
    }
    public void putFloat(String key, float value) {
        preferences.edit().putFloat(key, value).apply();
    }
    public void putBoolean(String key, boolean value) {
        preferences.edit().putBoolean(key, value).apply();
    }

}
