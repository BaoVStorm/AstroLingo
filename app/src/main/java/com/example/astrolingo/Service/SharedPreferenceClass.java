package com.example.astrolingo.Service;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferenceClass {
    public static final boolean isAllowToken = true;
    public static final String USER_PREP = "astrolingo_prep";
    public static final String ANSWER_TEST_PREP = "answer_test_prep";

    private SharedPreferences appShared;
    private SharedPreferences.Editor editor;

    public SharedPreferenceClass(Context context) {
        appShared = context.getSharedPreferences(USER_PREP, Context.MODE_PRIVATE);
        this.editor = appShared.edit();
    }

    public SharedPreferenceClass(Context context, String PREP) {
        appShared = context.getSharedPreferences(PREP, Context.MODE_PRIVATE);
        this.editor = appShared.edit();
    }

    // int
    public int getValue_int(String key) {
        return appShared.getInt(key, 0);
    }
    public void setValue_int(String key, int value) {
        editor.putInt(key, value).commit();
    }

    // string
    public String getValue_string(String key) {
        return appShared.getString(key, "");
    }
    public void setValue_string(String key, String value) {
        editor.putString(key, value).commit();
    }

    // boolean
    public boolean getValue_boolean(String key) {
        return appShared.getBoolean(key, false);
    }
    public void setValue_boolean(String key, boolean value) {
        editor.putBoolean(key, value).commit();
    }

    public void removeValue(String key) {
        editor.remove(key).commit();
    }

    public void clearAll() {

        editor.clear().commit();
        editor.putString("user_id", "null").commit();
        editor.putString("token", "null").commit();
    }

    public boolean contains(String key) {
        return appShared.contains(key);
    }

    public void VerifyToken() {

    }
}
