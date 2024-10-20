package me.t3sl4.upcortex.Utils.SharedPreferences;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Set;

public class SharedPreferencesManager {
    public static <T> void writeSharedPref(String variable, T data, Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(variable, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        if (data instanceof Boolean) {
            editor.putBoolean(variable, (Boolean) data);
        } else if (data instanceof Integer) {
            editor.putInt(variable, (Integer) data);
        } else if (data instanceof Float) {
            editor.putFloat(variable, (Float) data);
        } else if (data instanceof Long) {
            editor.putLong(variable, (Long) data);
        } else if (data instanceof String) {
            editor.putString(variable, (String) data);
        } else {
            throw new IllegalArgumentException("Unsupported data type");
        }

        editor.apply();
    }

    @SuppressWarnings("unchecked")
    public static <T> T getSharedPref(String variable, Context context, T defaultValue) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(variable, Context.MODE_PRIVATE);

        if (defaultValue instanceof Boolean) {
            return (T) (Boolean) sharedPreferences.getBoolean(variable, (Boolean) defaultValue);
        } else if (defaultValue instanceof Float) {
            return (T) (Float) sharedPreferences.getFloat(variable, (Float) defaultValue);
        } else if (defaultValue instanceof Integer) {
            return (T) (Integer) sharedPreferences.getInt(variable, (Integer) defaultValue);
        } else if (defaultValue instanceof String) {
            return (T) sharedPreferences.getString(variable, (String) defaultValue);
        } else if (defaultValue instanceof Set<?>) {
            return (T) sharedPreferences.getStringSet(variable, (Set<String>) defaultValue);
        } else {
            throw new IllegalArgumentException("Unsupported data type for shared preferences");
        }
    }

}
