package me.t3sl4.upcortex;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import me.t3sl4.upcortex.UI.Components.NavigationBar.NavigationBarUtil;
import me.t3sl4.upcortex.UI.Screens.OnBoard.OnBoard1;

public class SplashActivity extends AppCompatActivity {

    private static final int SPLASH_DELAY = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        NavigationBarUtil.hideNavigationBar(this);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, OnBoard1.class);
                startActivity(intent);
                finish();
            }
        }, SPLASH_DELAY);
    }
}