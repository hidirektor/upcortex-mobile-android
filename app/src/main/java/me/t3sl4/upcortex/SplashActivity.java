package me.t3sl4.upcortex;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import me.t3sl4.upcortex.Service.UserDataService;
import me.t3sl4.upcortex.UI.Components.Sneaker.Sneaker;
import me.t3sl4.upcortex.UI.Screens.Auth.Login;
import me.t3sl4.upcortex.UI.Screens.FirstSetup.FirstSetupError;
import me.t3sl4.upcortex.UI.Screens.General.Dashboard;
import me.t3sl4.upcortex.UI.Screens.OnBoard.OnBoard1;
import me.t3sl4.upcortex.Utility.Bluetooth.BluetoothUtil;
import me.t3sl4.upcortex.Utility.Permission.PermissionUtil;
import me.t3sl4.upcortex.Utility.Screen.ScreenUtil;
import me.t3sl4.upcortex.Utility.SharedPreferences.SharedPreferencesManager;
import me.t3sl4.upcortex.Utility.Utils;

public class SplashActivity extends AppCompatActivity {

    private static final int SPLASH_DELAY = 2000;
    private ImageView appLogo;
    private Animation fadeIn;
    private Animation fadeOut;

    private BluetoothUtil bluetoothUtil;
    String savedDeviceAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        bluetoothUtil = new BluetoothUtil();
        savedDeviceAddress = bluetoothUtil.getSavedDeviceAddress(this);

        componentInitialize();

        startLoadingAnimation();

        // İzinleri kontrol et ve izin pop-up'larını göster
        if (PermissionUtil.hasLocationPermission(this)) {
            if (PermissionUtil.hasNotificationPermission(this)) {
                if (PermissionUtil.hasBluetoothPermission(this)) {
                    continueAppFlow();
                } else {
                    showPermissionPopup(3); // Bluetooth izni
                }
            } else {
                showPermissionPopup(2); // Bildirim izni
            }
        } else {
            showPermissionPopup(1); // Konum izni
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PermissionUtil.LOCATION_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (!PermissionUtil.hasNotificationPermission(this)) {
                    showPermissionPopup(2);
                } else if (!PermissionUtil.hasBluetoothPermission(this)) {
                    showPermissionPopup(3);
                } else {
                    continueAppFlow();
                }
            } else {
                showPermissionPopup(1);
            }
        } else if (requestCode == PermissionUtil.NOTIFICATION_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (!PermissionUtil.hasBluetoothPermission(this)) {
                    showPermissionPopup(3);
                } else {
                    continueAppFlow();
                }
            } else {
                showPermissionPopup(2);
            }
        } else if (requestCode == PermissionUtil.BLUETOOTH_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                continueAppFlow();
            } else {
                showPermissionPopup(3); // Bluetooth izni
            }
        }
    }

    private void showPermissionPopup(int permStatus) {
        LayoutInflater inflater = LayoutInflater.from(this);
        View dialogView = inflater.inflate(R.layout.permission_explanation_popup, null);

        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
        alertBuilder.setView(dialogView);

        ImageView permIcon = dialogView.findViewById(R.id.permIcon);
        TextView permTitleText = dialogView.findViewById(R.id.permTitle);
        TextView permDescText = dialogView.findViewById(R.id.permDesc);

        Button acceptButton = dialogView.findViewById(R.id.allowButton);
        Button denyButton = dialogView.findViewById(R.id.denyButton);

        AlertDialog alert = alertBuilder.create();

        String locationPermTitle = getString(R.string.permLocationTitle);
        String locationPermDesc = getString(R.string.permLocationDesc);

        String notificationPermTitle = getString(R.string.permNotificationTitle);
        String notificationPermDesc = getString(R.string.permNotificationDesc);

        String bluetoothPermTitle = getString(R.string.permBluetoothTitle);
        String bluetoothPermDesc = getString(R.string.permBluetoothDesc);

        if (permStatus == 1) {
            permIcon.setImageDrawable(getDrawable(R.drawable.ikon_location_perm));
            permTitleText.setText(locationPermTitle);
            permDescText.setText(locationPermDesc);
        } else if (permStatus == 2) {
            permIcon.setImageDrawable(getDrawable(R.drawable.ikon_notification_perm));
            permTitleText.setText(notificationPermTitle);
            permDescText.setText(notificationPermDesc);
        } else if (permStatus == 3) {
            permIcon.setImageDrawable(getDrawable(R.drawable.ikon_bluetooth_perm)); // Yeni ikon
            permTitleText.setText(bluetoothPermTitle);
            permDescText.setText(bluetoothPermDesc);
        }

        alertBuilder.setCancelable(true);
        acceptButton.setOnClickListener(v -> {
            alert.dismiss();
            if (permStatus == 1) {
                PermissionUtil.requestLocationPermission(SplashActivity.this);
            } else if (permStatus == 2) {
                PermissionUtil.requestNotificationPermission(SplashActivity.this);
            } else if (permStatus == 3) {
                PermissionUtil.requestBluetoothPermission(SplashActivity.this);
            }
        });

        denyButton.setOnClickListener(v -> {
            String permErrorMsg = getString(R.string.locationPermError);
            String errorTitleMsg = getString(R.string.error_title);
            if (permStatus == 1) {
                Sneaker.with(this).setTitle(errorTitleMsg).setMessage(permErrorMsg).sneakError();
            }
            alert.dismiss();
        });

        alert.show();
        alert.getWindow().setBackgroundDrawableResource(R.drawable.background_permission_dialog);
    }

    private void continueAppFlow() {
        boolean isFirstTime = SharedPreferencesManager.getSharedPref("isFirstTime", this, true);
        boolean canAccess = SharedPreferencesManager.getSharedPref("canAccess", this, true);

        if (Utils.isNetworkAvailable(this)) {
            if (canAccess) {
                if (isFirstTime) {
                    setupOnboarding();
                } else {
                    if (savedDeviceAddress != null) {
                        bluetoothUtil.connectToDevice(savedDeviceAddress, isConnected -> {
                            if (isConnected) {
                                Sneaker.with(SplashActivity.this)
                                        .setTitle(getString(R.string.connected_title))
                                        .setMessage(getString(R.string.connected_desc))
                                        .sneakSuccess();
                            }
                        });
                    }
                    redirectToMainActivity();
                }
            } else {
                Intent firstSetupIntent = new Intent(SplashActivity.this, FirstSetupError.class);
                startActivity(firstSetupIntent);
                finish();
            }
        } else {
            showNetworkErrorDialog();
        }
    }

    private void showNetworkErrorDialog() {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_network_error);
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.background_button_exams);

        Button retryButton = dialog.findViewById(R.id.buttonRetry);
        Button closeButton = dialog.findViewById(R.id.buttonClose);

        retryButton.setOnClickListener(v -> {
            dialog.dismiss();
            Intent intent = getIntent();
            finish();
            startActivity(intent);
        });

        closeButton.setOnClickListener(v -> {
            dialog.dismiss();
            finishAffinity();
        });

        dialog.setCancelable(false);
        dialog.show();
    }

    private void redirectToMainActivity() {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(() -> {
            Intent loginIntent;
            stopLoadingAnimation();
            if (UserDataService.getAccessToken(this) != null && !UserDataService.getAccessToken(this).isEmpty()) {
                loginIntent = new Intent(SplashActivity.this, Dashboard.class);
            } else {
                loginIntent = new Intent(SplashActivity.this, Login.class);
            }
            startActivity(loginIntent);
            finish();
        }, SPLASH_DELAY);
    }

    private void setupOnboarding() {
        SharedPreferencesManager.writeSharedPref("isFirstTime", false, this);
        Intent intent = new Intent(SplashActivity.this, OnBoard1.class);
        startActivity(intent);
        finish();
    }

    private void componentInitialize() {
        ScreenUtil.hideNavigationBar(this);

        appLogo = findViewById(R.id.appLogo);
        fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        fadeOut = AnimationUtils.loadAnimation(this, R.anim.fade_out);
    }

    private void startLoadingAnimation() {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(() -> {
            appLogo.startAnimation(fadeOut);
            fadeOut.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    appLogo.startAnimation(fadeIn);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }
            });
        }, 0);

        fadeIn.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                appLogo.startAnimation(fadeOut);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
    }

    private void stopLoadingAnimation() {
        appLogo.clearAnimation();
    }
}