package me.t3sl4.upcortex.Utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;

import com.yariksoffice.lingver.Lingver;

public class BaseUtil {
    public static String SUPPORT_URL = "https://github.com/hidirektor";

    /*public static void setSystemLanguage(Context context) {
        String userLanguage = UserDataService.getSelectedLanguage(context);
        String nextLang;

        if (userLanguage.equals("true")) {
            nextLang = "tr";
        } else if(userLanguage.equals("false")) {
            nextLang = "en";
        } else {
            nextLang = "tr";
        }

        updateLocale(context, nextLang);
    }

    public static void changeSystemLanguage(Context context) {
        String userLanguage = UserDataService.getSelectedLanguage(context);
        String nextLang;

        if (userLanguage.equals("true")) {
            nextLang = "en";
            UserDataService.setSelectedLanguage(context, "false");
        } else {
            nextLang = "tr";
            UserDataService.setSelectedLanguage(context, "true");
        }

        //UserService.updatePreferences(context, UserDataService.getUserID(context), UserDataService.getSelectedLanguage(context), UserDataService.getSelectedNightMode(context));

        updateLocale(context, nextLang);
    }*/

    public static void updateLocale(Context context, String nextLang) {
        Lingver.getInstance().setLocale(context, nextLang);
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager != null) {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                Network network = connectivityManager.getActiveNetwork();
                if (network != null) {
                    NetworkCapabilities networkCapabilities = connectivityManager.getNetworkCapabilities(network);
                    return networkCapabilities != null &&
                            (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                                    networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                                    networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET));
                }
            } else {
                // below Android Marshmallow (API 23)
                NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
                return activeNetworkInfo != null && activeNetworkInfo.isConnected();
            }
        }
        return false;
    }
}
