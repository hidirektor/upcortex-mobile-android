package me.t3sl4.upcortex.UI.Screens.OnBoard;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import me.t3sl4.upcortex.R;
import me.t3sl4.upcortex.UI.Screens.FirstSetup.FirstSetup;
import me.t3sl4.upcortex.Utility.Screen.ScreenUtil;
import me.t3sl4.upcortex.Utility.SharedPreferences.SharedPreferencesManager;

public class OnBoard3 extends AppCompatActivity {

    private Button nextButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboard_3);

        ScreenUtil.hideNavigationBar(this);
        ScreenUtil.hideStatusBar(this);

        initializeComponents();
        buttonClickListeners();

        ScreenUtil.setSwipeListener(this, FirstSetup.class, OnBoard2.class);
    }

    private void initializeComponents() {
        nextButton = findViewById(R.id.nextButton);
    }

    private void buttonClickListeners() {
        nextButton.setOnClickListener(v -> {
            Intent loginIntent = new Intent(OnBoard3.this, FirstSetup.class);
            startActivity(loginIntent);
            SharedPreferencesManager.writeSharedPref("isFirstTime", false, this);
            finish();
        });
    }
}
