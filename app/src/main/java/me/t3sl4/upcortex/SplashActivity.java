package me.t3sl4.upcortex;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import me.t3sl4.upcortex.UI.Components.Sneaker.Sneaker;
import me.t3sl4.upcortex.UI.Screens.Auth.Login;
import me.t3sl4.upcortex.UI.Screens.OnBoard.OnBoard1;
import me.t3sl4.upcortex.UI.Screens.Program.ProgramSelection;
import me.t3sl4.upcortex.Utils.BaseUtil;
import me.t3sl4.upcortex.Utils.Bluetooth.BluetoothUtil;
import me.t3sl4.upcortex.Utils.Screen.ScreenUtil;
import me.t3sl4.upcortex.Utils.Service.UserDataService;
import me.t3sl4.upcortex.Utils.SharedPreferences.SharedPreferencesManager;

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

        continueAppFlow();
    }

    private void continueAppFlow() {
        boolean isFirstTime = SharedPreferencesManager.getSharedPref("isFirstTime", this, true);
        boolean canAccess = SharedPreferencesManager.getSharedPref("canAccess", this, true);

        if (BaseUtil.isNetworkAvailable(this)) {
            if(isFirstTime) {
                setupOnboarding();
            } else {
                if(canAccess) {
                    redirectToMainActivity();
                } else {
                    Sneaker.with(SplashActivity.this)
                            .setTitle(getString(R.string.error_title))
                            .setMessage(getString(R.string.not_permitted))
                            .setHeight(75)
                            .setDuration(1500)
                            .sneakError();
                }
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
                loginIntent = new Intent(SplashActivity.this, ProgramSelection.class);
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