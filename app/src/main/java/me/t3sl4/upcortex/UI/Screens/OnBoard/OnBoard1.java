package me.t3sl4.upcortex.UI.Screens.OnBoard;

import android.content.Intent;
import android.os.Bundle;
import android.view.GestureDetector;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import me.t3sl4.upcortex.R;
import me.t3sl4.upcortex.UI.Screens.General.Dashboard;
import me.t3sl4.upcortex.Util.Screen.ScreenUtil;

public class OnBoard1 extends AppCompatActivity {

    private Button nextButton;
    private Button skipButton;
    private GestureDetector gestureDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboard_1);

        initializeComponents();
        buttonClickListeners();

        ScreenUtil.setSwipeListener(this, OnBoard2.class, null);
    }

    private void initializeComponents() {
        nextButton = findViewById(R.id.nextButton);
        skipButton = findViewById(R.id.skipButton);
    }

    private void buttonClickListeners() {
        nextButton.setOnClickListener(v -> {
            Intent onboard2Intent = new Intent(OnBoard1.this, OnBoard2.class);
            startActivity(onboard2Intent);
            finish();
        });

        skipButton.setOnClickListener(v -> {
            Intent loginIntent = new Intent(OnBoard1.this, Dashboard.class);
            startActivity(loginIntent);
            finish();
        });
    }
}