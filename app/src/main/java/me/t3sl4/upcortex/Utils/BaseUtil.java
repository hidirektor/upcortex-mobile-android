package me.t3sl4.upcortex.Utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.text.format.Formatter;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.yariksoffice.lingver.Lingver;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import me.t3sl4.upcortex.R;
import me.t3sl4.upcortex.UI.Components.Sneaker.Sneaker;
import me.t3sl4.upcortex.Utils.Permission.PermissionUtil;
import me.t3sl4.upcortex.Utils.SharedPreferences.SharedPreferencesManager;

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

    public static String getUserIp(Context context) {
        try {
            WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            if (wifiManager != null && wifiManager.isWifiEnabled()) {
                int ipAddress = wifiManager.getConnectionInfo().getIpAddress();
                return Formatter.formatIpAddress(ipAddress);
            } else {
                List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
                for (NetworkInterface networkInterface : interfaces) {
                    List<InetAddress> addresses = Collections.list(networkInterface.getInetAddresses());
                    for (InetAddress address : addresses) {
                        if (!address.isLoopbackAddress() && address instanceof InetAddress) {
                            return address.getHostAddress();
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "IP address not available";
    }

    public static String getCurrentDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }

    public static String getValidBirthDate(String birthDate) {
        DateTimeFormatter isoFormatter = DateTimeFormatter.ISO_DATE;

        try {
            LocalDate.parse(birthDate, isoFormatter);
            return birthDate;
        } catch (DateTimeParseException e) {
            System.out.println("Invalid date format. Setting default birthDate.");
            return "2000-01-01";
        }
    }

    public static void clearRegisterData(Context context) {
        SharedPreferencesManager.writeSharedPref("name", "", context);
        SharedPreferencesManager.writeSharedPref("surname", "", context);
        SharedPreferencesManager.writeSharedPref("idNumber", "", context);
        SharedPreferencesManager.writeSharedPref("birthDate", "", context);
        SharedPreferencesManager.writeSharedPref("countryCode", "", context);
        SharedPreferencesManager.writeSharedPref("phoneNumber", "", context);
        SharedPreferencesManager.writeSharedPref("password", "", context);
        SharedPreferencesManager.writeSharedPref("repeatPassword", "", context);
        SharedPreferencesManager.writeSharedPref("termsAccepted", false, context);
        SharedPreferencesManager.writeSharedPref("addressName", "", context);
        SharedPreferencesManager.writeSharedPref("addressName_name", "", context);
        SharedPreferencesManager.writeSharedPref("addressName_surname", "", context);
        SharedPreferencesManager.writeSharedPref("addressName_countryCode", "", context);
        SharedPreferencesManager.writeSharedPref("addressName_phoneNumber", "", context);
        SharedPreferencesManager.writeSharedPref("city", "", context);
        SharedPreferencesManager.writeSharedPref("district", "", context);
        SharedPreferencesManager.writeSharedPref("neighborhood", "", context);
        SharedPreferencesManager.writeSharedPref("addressDetail", "", context);
        SharedPreferencesManager.writeSharedPref("confirmAddress", false, context);
        SharedPreferencesManager.writeSharedPref("cardNumber", "", context);
        SharedPreferencesManager.writeSharedPref("holderName", "", context);
        SharedPreferencesManager.writeSharedPref("expiryDate", "", context);
        SharedPreferencesManager.writeSharedPref("cvv", "", context);
        SharedPreferencesManager.writeSharedPref("confirmCheckBox", false, context);
    }
}
