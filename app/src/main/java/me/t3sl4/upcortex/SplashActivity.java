package me.t3sl4.upcortex;

import android.Manifest;
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
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import me.t3sl4.upcortex.UI.Components.Sneaker.Sneaker;
import me.t3sl4.upcortex.UI.Screens.Auth.Login;
import me.t3sl4.upcortex.UI.Screens.FirstSetup.FirstSetupError;
import me.t3sl4.upcortex.UI.Screens.OnBoard.OnBoard1;
import me.t3sl4.upcortex.Utility.Screen.ScreenUtil;
import me.t3sl4.upcortex.Utility.SharedPreferences.SharedPreferencesManager;
import me.t3sl4.upcortex.Utility.Utils;

public class SplashActivity extends AppCompatActivity {

    private static final int SPLASH_DELAY = 2000;
    Intent redirectIntent;
    private SharedPreferencesManager sharedPrefManager;

    private ImageView appLogo;
    private Animation fadeIn;
    private Animation fadeOut;

    private Dialog uyariDiyalog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        componentInitialize();

        startLoadingAnimation();

        if (checkLocationPermission()) {
            if (checkNotificationPermission()) {
                continueAppFlow();
            } else {
                showPermissionPopup(2); // Bildirim izni
            }
        } else {
            showPermissionPopup(1); // Konum izni
        }
    }

    private boolean checkLocationPermission() {
        return ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    private boolean checkNotificationPermission() {
        return ContextCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestLocationPermission() {
        ActivityCompat.requestPermissions(this,
                new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                101);
    }

    private void requestNotificationPermission() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.POST_NOTIFICATIONS},
                102);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 101) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (!checkNotificationPermission()) {
                    showPermissionPopup(2);
                } else {
                    continueAppFlow();
                }
            } else {
                showPermissionPopup(1);
            }
        } else if (requestCode == 102) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                continueAppFlow();
            } else {
                showPermissionPopup(2);
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

        if(permStatus == 1) {
            permIcon.setImageDrawable(getDrawable(R.drawable.ikon_location_perm));
            permTitleText.setText(locationPermTitle);
            permDescText.setText(locationPermDesc);
        } else {
            permIcon.setImageDrawable(getDrawable(R.drawable.ikon_notification_perm));
            permTitleText.setText(notificationPermTitle);
            permDescText.setText(notificationPermDesc);
        }

        alertBuilder.setCancelable(true);
        acceptButton.setOnClickListener(v -> {
            alert.dismiss();
            if (permStatus == 1) {
                requestLocationPermission();
            } else {
                requestNotificationPermission();
            }
        });

        denyButton.setOnClickListener(v -> {
            String permErrorMsg = getString(R.string.locationPermError);
            String errorTitleMsg = getString(R.string.error_title);
            if(permStatus == 1) {
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
        //Utils.setSystemLanguage(this);

        if(Utils.isNetworkAvailable(this)) {
            if(canAccess) {
                if(isFirstTime) {
                    setupOnboarding();
                } else {
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
            stopLoadingAnimation();
            Intent intent = new Intent(SplashActivity.this, Login.class);
            startActivity(intent);
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

        uyariDiyalog = new Dialog(this);
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