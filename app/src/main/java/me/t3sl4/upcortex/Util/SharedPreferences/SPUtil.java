package me.t3sl4.upcortex.Util.SharedPreferences;

import android.content.Context;
import android.content.SharedPreferences;

public class SPUtil {
    private static final String PREF_NAME = "register_prefs";
    private static final String PREF_NAME_FIRST_TIME = "me.t3sl4.upcortex.PREFERENCE_FILE_KEY";
    private static final String KEY_IS_FIRST_TIME = "isFirstTime";

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public SPUtil(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void saveString(String key, String value) {
        editor.putString(key, value);
        editor.apply();
    }

    public String getString(String key) {
        return sharedPreferences.getString(key, "");
    }

    public void saveBoolean(String key, boolean value) {
        editor.putBoolean(key, value);
        editor.apply();
    }

    public boolean getBoolean(String key) {
        return sharedPreferences.getBoolean(key, false);
    }

    public void clear() {
        editor.clear();
        editor.apply();
    }

    public static boolean isFirstTime(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME_FIRST_TIME, Context.MODE_PRIVATE);
        return prefs.getBoolean(KEY_IS_FIRST_TIME, true);
    }

    public static void setFirstTime(Context context, boolean isFirstTime) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME_FIRST_TIME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(KEY_IS_FIRST_TIME, isFirstTime);
        editor.apply();
    }
}