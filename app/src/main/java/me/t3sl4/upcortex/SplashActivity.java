package me.t3sl4.upcortex;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.appcompat.app.AppCompatActivity;

import me.t3sl4.upcortex.UI.Components.NavigationBar.NavigationBarUtil;
import me.t3sl4.upcortex.UI.Screens.Auth.AuthSelection;
import me.t3sl4.upcortex.UI.Screens.FirstSetup.FirstSetup;
import me.t3sl4.upcortex.UI.Screens.General.Dashboard;
import me.t3sl4.upcortex.UI.Screens.OnBoard.OnBoard1;
import me.t3sl4.upcortex.Util.SharedPreferences.SPUtil;

public class SplashActivity extends AppCompatActivity {

    private static final int SPLASH_DELAY = 2000;
    Intent redirectIntent;
    private SPUtil sharedPrefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        NavigationBarUtil.hideNavigationBar(this);

        sharedPrefManager = new SPUtil(this);

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            if (SPUtil.isFirstTime(SplashActivity.this)) {
                redirectIntent = new Intent(SplashActivity.this, OnBoard1.class);
                SPUtil.setFirstTime(SplashActivity.this, false);
            } else if (!sharedPrefManager.getBoolean("canAccess")) {
                redirectIntent = new Intent(SplashActivity.this, FirstSetup.class);
            } else if (!sharedPrefManager.getString("userToken").isEmpty()) {
                redirectIntent = new Intent(SplashActivity.this, Dashboard.class);
            } else {
                redirectIntent = new Intent(SplashActivity.this, AuthSelection.class);
            }
            startActivity(redirectIntent);
            finish();
        }, SPLASH_DELAY);
    }
}