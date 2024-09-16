package me.t3sl4.upcortex.Service;

import android.content.Context;
import android.content.SharedPreferences;

import me.t3sl4.upcortex.Model.User.User;

public class UserDataService {

    private static final String USER_PREFS = "UserPrefs";
    private static final String KEY_USER_ID = "userID";
    private static final String KEY_ACCESS_TOKEN = "accessToken";
    private static final String KEY_REFRESH_TOKEN = "refreshToken";
    private static final String KEY_USER_ROLE = "userRole";
    private static final String KEY_USER_NAME = "userName";
    private static final String KEY_USER_EMAIL = "userEmail";
    private static final String KEY_USER_NAME_SURNAME = "userNameSurname";
    private static final String KEY_USER_PHONE = "userPhone";
    private static final String KEY_USER_COMPANY = "userCompany";
    private static final String KEY_USER_CREATED_AT = "userCreatedAt";
    private static final String KEY_USER_IS_ACTIVE = "isActive";
    private static final String KEY_USER_PREFERENCES_LANGUAGE = "selectedLanguage";
    private static final String KEY_USER_PREFERENCES_NIGHT_MODE = "selectedNightMode";

    private static SharedPreferences getPreferences(Context context) {
        return context.getSharedPreferences(USER_PREFS, Context.MODE_PRIVATE);
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

    public static void setRefreshToken(Context context, String refreshToken) {
        SharedPreferences prefs = getPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(KEY_REFRESH_TOKEN, refreshToken);
        editor.apply();
    }

    public static void setUserRole(Context context, String role) {
        SharedPreferences prefs = getPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(KEY_USER_ROLE, role);
        editor.apply();
    }

    public static void setUserName(Context context, String userName) {
        SharedPreferences prefs = getPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(KEY_USER_NAME, userName);
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

    public static void setCompanyName(Context context, String companyName) {
        SharedPreferences prefs = getPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(KEY_USER_COMPANY, companyName);
        editor.apply();
    }

    public static void setCreatedAt(Context context, String createdAt) {
        SharedPreferences prefs = getPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(KEY_USER_CREATED_AT, createdAt);
        editor.apply();
    }

    public static void setIsActive(Context context, String isActive) {
        SharedPreferences prefs = getPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(KEY_USER_IS_ACTIVE, isActive);
        editor.apply();
    }

    public static void setSelectedLanguage(Context context, String selectedLanguage) {
        SharedPreferences prefs = getPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(KEY_USER_PREFERENCES_LANGUAGE, selectedLanguage);
        editor.apply();
    }

    public static void setSelectedNightMode(Context context, String selectedNightMode) {
        SharedPreferences prefs = getPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(KEY_USER_PREFERENCES_NIGHT_MODE, selectedNightMode);
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

    public static String getRefreshToken(Context context) {
        SharedPreferences prefs = getPreferences(context);
        return prefs.getString(KEY_REFRESH_TOKEN, "");
    }

    public static String getUserRole(Context context) {
        SharedPreferences prefs = getPreferences(context);
        return prefs.getString(KEY_USER_ROLE, "");
    }

    public static String getUserName(Context context) {
        SharedPreferences prefs = getPreferences(context);
        return prefs.getString(KEY_USER_NAME, "");
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

    public static String getCompanyName(Context context) {
        SharedPreferences prefs = getPreferences(context);
        return prefs.getString(KEY_USER_COMPANY, "");
    }

    public static String getCreatedAt(Context context) {
        SharedPreferences prefs = getPreferences(context);
        return prefs.getString(KEY_USER_CREATED_AT, "");
    }

    public static String getIsActive(Context context) {
        SharedPreferences prefs = getPreferences(context);
        return prefs.getString(KEY_USER_IS_ACTIVE, "");
    }

    public static String getSelectedLanguage(Context context) {
        SharedPreferences prefs = getPreferences(context);
        return prefs.getString(KEY_USER_PREFERENCES_LANGUAGE, "");
    }

    public static String getSelectedNightMode(Context context) {
        SharedPreferences prefs = getPreferences(context);
        return prefs.getString(KEY_USER_PREFERENCES_NIGHT_MODE, "");
    }

    // Clear user data from SharedPreferences
    public static void clearUser(Context context) {
        SharedPreferences prefs = getPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();

        editor.remove(KEY_USER_ID);
        editor.remove(KEY_ACCESS_TOKEN);
        editor.remove(KEY_REFRESH_TOKEN);
        editor.remove(KEY_USER_ROLE);
        editor.remove(KEY_USER_NAME);
        editor.remove(KEY_USER_EMAIL);
        editor.remove(KEY_USER_NAME_SURNAME);
        editor.remove(KEY_USER_PHONE);
        editor.remove(KEY_USER_COMPANY);
        editor.remove(KEY_USER_CREATED_AT);
        editor.remove(KEY_USER_IS_ACTIVE);
        editor.remove(KEY_USER_PREFERENCES_LANGUAGE);
        editor.remove(KEY_USER_PREFERENCES_NIGHT_MODE);

        editor.apply();
    }

    public static User getUserFromPreferences(Context context) {
        String userID = getUserID(context);
        String accessToken = getAccessToken(context);
        String refreshToken = getRefreshToken(context);
        String role = getUserRole(context);
        String userName = getUserName(context);
        String eMail = geteMail(context);
        String nameSurname = getNameSurname(context);
        String phoneNumber = getPhoneNumber(context);
        String companyName = getCompanyName(context);
        String createdAt = getCreatedAt(context);
        String isActive = getIsActive(context);
        String selectedLanguage = getSelectedLanguage(context);
        String selectedNightMode = getSelectedNightMode(context);

        User sharedUser = new User(role, userName, eMail, nameSurname, phoneNumber, companyName, createdAt, isActive, selectedLanguage, selectedNightMode);
        sharedUser.setUserID(userID);
        sharedUser.setAccessToken(accessToken);
        sharedUser.setRefreshToken(refreshToken);

        return sharedUser;
    }
}