package me.t3sl4.upcortex;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.appcompat.app.AppCompatActivity;

import me.t3sl4.upcortex.UI.Components.NavigationBar.NavigationBarUtil;
import me.t3sl4.upcortex.UI.Screens.Auth.AuthSelection;
import me.t3sl4.upcortex.UI.Screens.OnBoard.OnBoard1;
import me.t3sl4.upcortex.Util.SharedPreferences.SPUtil;

public class SplashActivity extends AppCompatActivity {

    private static final int SPLASH_DELAY = 2000;
    Intent redirectIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        NavigationBarUtil.hideNavigationBar(this);

        if(SPUtil.isFirstTime(SplashActivity.this)) {
            redirectIntent = new Intent(SplashActivity.this, OnBoard1.class);
            SPUtil.setFirstTime(SplashActivity.this, false);
            new Handler(Looper.getMainLooper()).postDelayed(() -> startActivity(redirectIntent), SPLASH_DELAY);
        } else {
            redirectIntent = new Intent(SplashActivity.this, AuthSelection.class);
            startActivity(redirectIntent);
        }
        finish();
    }
}