package me.t3sl4.upcortex.Utils.Service;

import android.content.Context;
import android.content.SharedPreferences;

import me.t3sl4.upcortex.Model.User.User;
import me.t3sl4.upcortex.Utils.Bluetooth.BluetoothUtil;

public class UserDataService {

    private static final String USER_DATA = "UserData";

    private static final String KEY_USER_ID = "userID";
    private static final String KEY_ACCESS_TOKEN = "accessToken";

    private static final String KEY_USER_IDENTITY_NUMBER = "identityNumber";
    private static final String KEY_USER_EMAIL = "userEmail";
    private static final String KEY_USER_NAME_SURNAME = "userNameSurname";
    private static final String KEY_USER_PHONE = "userPhone";

    private static final String KEY_USER_STATE = "userState";

    private static final String KEY_USER_FIRSTNAME = "userFirstName";
    private static final String KEY_USER_LASTNAME = "userLastName";
    private static final String KEY_USER_ADDRESS = "userAddress";
    private static final String KEY_USER_DIAL_CODE = "userDialCode";
    private static final String KEY_USER_ZIP_CODE = "userZipCode";
    private static final String KEY_USER_CITY = "userCity";

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

    public static void setUserFirstName(Context context, String userFirstName) {
        SharedPreferences prefs = getPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(KEY_USER_FIRSTNAME, userFirstName);
        editor.apply();
    }

    public static void setUserLastName(Context context, String userLastName) {
        SharedPreferences prefs = getPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(KEY_USER_LASTNAME, userLastName);
        editor.apply();
    }

    public static void setUserAddress(Context context, String userAddress) {
        SharedPreferences prefs = getPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(KEY_USER_ADDRESS, userAddress);
        editor.apply();
    }

    public static void setUserDialCode(Context context, String userDialCode) {
        SharedPreferences prefs = getPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(KEY_USER_DIAL_CODE, userDialCode);
        editor.apply();
    }

    public static void setUserZipCode(Context context, String userZipCode) {
        SharedPreferences prefs = getPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(KEY_USER_ZIP_CODE, userZipCode);
        editor.apply();
    }

    public static void setUserCity(Context context, String userCity) {
        SharedPreferences prefs = getPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(KEY_USER_CITY, userCity);
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

    public static String getUserFirstName(Context context) {
        SharedPreferences prefs = getPreferences(context);
        return prefs.getString(KEY_USER_FIRSTNAME, "");
    }

    public static String getUserLastName(Context context) {
        SharedPreferences prefs = getPreferences(context);
        return prefs.getString(KEY_USER_LASTNAME, "");
    }

    public static String getUserAddress(Context context) {
        SharedPreferences prefs = getPreferences(context);
        return prefs.getString(KEY_USER_ADDRESS, "");
    }

    public static String getUserDialCode(Context context) {
        SharedPreferences prefs = getPreferences(context);
        return prefs.getString(KEY_USER_DIAL_CODE, "");
    }

    public static String getUserZipCode(Context context) {
        SharedPreferences prefs = getPreferences(context);
        return prefs.getString(KEY_USER_ZIP_CODE, "");
    }

    public static String getUserCity(Context context) {
        SharedPreferences prefs = getPreferences(context);
        return prefs.getString(KEY_USER_CITY, "");
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
        new BluetoothUtil().clearSavedDeviceAddress(context);

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

        sharedUser.setName(getUserFirstName(context));
        sharedUser.setSurname(getUserLastName(context));
        sharedUser.setAddress(getUserAddress(context));
        sharedUser.setDialCode(getUserDialCode(context));
        sharedUser.setZipCode(getUserZipCode(context));
        sharedUser.setCity(getUserCity(context));

        return sharedUser;
    }
}