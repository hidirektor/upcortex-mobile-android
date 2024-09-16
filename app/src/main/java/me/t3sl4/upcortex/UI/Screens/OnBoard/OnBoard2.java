package me.t3sl4.upcortex.UI.Screens.OnBoard;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import me.t3sl4.upcortex.R;
import me.t3sl4.upcortex.UI.Screens.FirstSetup.FirstSetup;
import me.t3sl4.upcortex.Utility.Screen.ScreenUtil;
import me.t3sl4.upcortex.Utility.SharedPreferences.SharedPreferencesManager;
import me.t3sl4.upcortex.Utility.Utils;

public class OnBoard2 extends AppCompatActivity {

    private Button nextButton;
    private Button skipButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboard_2);

        Utils.hideNavigationBar(this);
        Utils.hideStatusBar(this);

        initializeComponents();
        buttonClickListeners();

        ScreenUtil.setSwipeListener(this, OnBoard3.class, OnBoard1.class);
    }

    private void initializeComponents() {
        nextButton = findViewById(R.id.nextButton);
        skipButton = findViewById(R.id.skipButton);
    }

    private void buttonClickListeners() {
        nextButton.setOnClickListener(v -> {
            Intent onboard3Intent = new Intent(OnBoard2.this, OnBoard3.class);
            startActivity(onboard3Intent);
            finish();
        });

        skipButton.setOnClickListener(v -> {
            Intent loginIntent = new Intent(OnBoard2.this, FirstSetup.class);
            startActivity(loginIntent);
            SharedPreferencesManager.setFirstTime(OnBoard2.this, false);
            finish();
        });
    }
}
