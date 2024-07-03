package me.t3sl4.upcortex;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.appcompat.app.AppCompatActivity;

import me.t3sl4.upcortex.UI.Components.NavigationBar.NavigationBarUtil;
import me.t3sl4.upcortex.UI.Screens.Auth.AuthSelection;
import me.t3sl4.upcortex.UI.Screens.OnBoard.OnBoard1;
import me.t3sl4.upcortex.Util.Utils;

public class SplashActivity extends AppCompatActivity {

    private static final int SPLASH_DELAY = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        NavigationBarUtil.hideNavigationBar(this);

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            Intent intent;
            if (Utils.isFirstTime(SplashActivity.this)) {
                intent = new Intent(SplashActivity.this, OnBoard1.class);
                Utils.setFirstTime(SplashActivity.this, false);
            } else {
                intent = new Intent(SplashActivity.this, AuthSelection.class);
            }
            startActivity(intent);
            finish();
        }, SPLASH_DELAY);
    }
}