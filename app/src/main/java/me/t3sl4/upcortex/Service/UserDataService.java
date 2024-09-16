package me.t3sl4.upcortex.Service;

import android.content.Context;
import android.content.SharedPreferences;

import me.t3sl4.upcortex.Model.User.User;

public class UserDataService {

    private static final String USER_DATA = "UserData";

    private static final String KEY_USER_ID = "userID";
    private static final String KEY_ACCESS_TOKEN = "accessToken";

    private static final String KEY_USER_IDENTITY_NUMBER = "identityNumber";
    private static final String KEY_USER_EMAIL = "userEmail";
    private static final String KEY_USER_NAME_SURNAME = "userNameSurname";
    private static final String KEY_USER_PHONE = "userPhone";

    private static final String KEY_USER_STATE = "userState";

    private static SharedPreferences getPreferences(Context context) {
        return context.getSharedPreferences(USER_DATA, Context.MODE_PRIVATE);
    }

    public static void setUserID(Context context, String userID) {
        SharedPreferences prefs = getPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(KEY_USER_ID, userID);
        editor.apply();
    }

    public static void setAccessToken(Context context, String accessToken) {
        SharedPreferences prefs = getPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(KEY_ACCESS_TOKEN, accessToken);
        editor.apply();
    }

    public static void setIdentityNumber(Context context, String identityNumber) {
        SharedPreferences prefs = getPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(KEY_USER_IDENTITY_NUMBER, identityNumber);
        editor.apply();
    }

    public static void seteMail(Context context, String eMail) {
        SharedPreferences prefs = getPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(KEY_USER_EMAIL, eMail);
        editor.apply();
    }

    public static void setNameSurname(Context context, String nameSurname) {
        SharedPreferences prefs = getPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(KEY_USER_NAME_SURNAME, nameSurname);
        editor.apply();
    }

    public static void setPhoneNumber(Context context, String phoneNumber) {
        SharedPreferences prefs = getPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(KEY_USER_PHONE, phoneNumber);
        editor.apply();
    }

    public static void setUserState(Context context, String userState) {
        SharedPreferences prefs = getPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(KEY_USER_STATE, userState);
        editor.apply();
    }

    // Get individual user properties from SharedPreferences
    public static String getUserID(Context context) {
        SharedPreferences prefs = getPreferences(context);
        return prefs.getString(KEY_USER_ID, "");
    }

    public static String getAccessToken(Context context) {
        SharedPreferences prefs = getPreferences(context);
        return prefs.getString(KEY_ACCESS_TOKEN, "");
    }

    public static String getIdentityNumber(Context context) {
        SharedPreferences prefs = getPreferences(context);
        return prefs.getString(KEY_USER_IDENTITY_NUMBER, "");
    }

    public static String geteMail(Context context) {
        SharedPreferences prefs = getPreferences(context);
        return prefs.getString(KEY_USER_EMAIL, "");
    }

    public static String getNameSurname(Context context) {
        SharedPreferences prefs = getPreferences(context);
        return prefs.getString(KEY_USER_NAME_SURNAME, "");
    }

    public static String getPhoneNumber(Context context) {
        SharedPreferences prefs = getPreferences(context);
        return prefs.getString(KEY_USER_PHONE, "");
    }

    public static String getUserState(Context context) {
        SharedPreferences prefs = getPreferences(context);
        return prefs.getString(KEY_USER_STATE, "");
    }

    // Clear user data from SharedPreferences
    public static void clearUser(Context context) {
        SharedPreferences prefs = getPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();

        editor.remove(KEY_USER_ID);
        editor.remove(KEY_ACCESS_TOKEN);

        editor.remove(KEY_USER_IDENTITY_NUMBER);
        editor.remove(KEY_USER_EMAIL);
        editor.remove(KEY_USER_NAME_SURNAME);
        editor.remove(KEY_USER_PHONE);
        editor.remove(KEY_USER_STATE);

        editor.apply();
    }

    public static User getUserFromPreferences(Context context) {
        String userID = getUserID(context);
        String accessToken = getAccessToken(context);

        String identityNumber = getIdentityNumber(context);
        String eMail = geteMail(context);
        String nameSurname = getNameSurname(context);
        String phoneNumber = getPhoneNumber(context);

        String userState = getUserState(context);

        User sharedUser = new User(userID, accessToken, identityNumber, eMail, nameSurname, phoneNumber, userState);

        return sharedUser;
    }
}