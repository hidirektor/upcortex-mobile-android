package me.t3sl4.upcortex.UI.Screens.OnBoard;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import me.t3sl4.upcortex.R;
import me.t3sl4.upcortex.UI.Screens.General.Dashboard;
import me.t3sl4.upcortex.Util.Screen.ScreenUtil;

public class OnBoard2 extends AppCompatActivity {

    private Button nextButton;
    private Button skipButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboard_2);

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
            Intent loginIntent = new Intent(OnBoard2.this, Dashboard.class);
            startActivity(loginIntent);
            finish();
        });
    }
}
