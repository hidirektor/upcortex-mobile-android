package me.t3sl4.upcortex.Utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.yariksoffice.lingver.Lingver;

import me.t3sl4.upcortex.R;
import me.t3sl4.upcortex.UI.Components.Sneaker.Sneaker;
import me.t3sl4.upcortex.Utils.Permission.PermissionUtil;

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

    public static void showPermissionPopup(int permStatus, Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogView = inflater.inflate(R.layout.permission_explanation_popup, null);

        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
        alertBuilder.setView(dialogView);

        ImageView permIcon = dialogView.findViewById(R.id.permIcon);
        TextView permTitleText = dialogView.findViewById(R.id.permTitle);
        TextView permDescText = dialogView.findViewById(R.id.permDesc);

        Button acceptButton = dialogView.findViewById(R.id.allowButton);
        Button denyButton = dialogView.findViewById(R.id.denyButton);

        AlertDialog alert = alertBuilder.create();

        String locationPermTitle = context.getString(R.string.permLocationTitle);
        String locationPermDesc = context.getString(R.string.permLocationDesc);

        String notificationPermTitle = context.getString(R.string.permNotificationTitle);
        String notificationPermDesc = context.getString(R.string.permNotificationDesc);

        String bluetoothPermTitle = context.getString(R.string.permBluetoothTitle);
        String bluetoothPermDesc = context.getString(R.string.permBluetoothDesc);

        if (permStatus == 1) {
            permIcon.setImageDrawable(context.getDrawable(R.drawable.ikon_location_perm));
            permTitleText.setText(locationPermTitle);
            permDescText.setText(locationPermDesc);
        } else if (permStatus == 2) {
            permIcon.setImageDrawable(context.getDrawable(R.drawable.ikon_notification_perm));
            permTitleText.setText(notificationPermTitle);
            permDescText.setText(notificationPermDesc);
        } else if (permStatus == 3) {
            permIcon.setImageDrawable(context.getDrawable(R.drawable.ikon_bluetooth_perm));
            permTitleText.setText(bluetoothPermTitle);
            permDescText.setText(bluetoothPermDesc);
        }

        alertBuilder.setCancelable(true);
        acceptButton.setOnClickListener(v -> {
            alert.dismiss();
            if (permStatus == 1) {
                PermissionUtil.requestLocationPermission((Activity) context);
            } else if (permStatus == 2) {
                PermissionUtil.requestNotificationPermission((Activity) context);
            } else if (permStatus == 3) {
                PermissionUtil.requestBluetoothPermission((Activity) context);
            }
        });

        denyButton.setOnClickListener(v -> {
            String permErrorMsg = context.getString(R.string.locationPermError);
            String errorTitleMsg = context.getString(R.string.error_title);
            if (permStatus == 1) {
                Sneaker.with((Activity) context).setTitle(errorTitleMsg).setMessage(permErrorMsg).sneakError();
            }
            alert.dismiss();
        });

        alert.show();
        alert.getWindow().setBackgroundDrawableResource(R.drawable.background_permission_dialog);
    }
}
