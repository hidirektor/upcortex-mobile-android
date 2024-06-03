package me.t3sl4.upcortex.UI.Screens.General;

import android.os.Bundle;
import android.os.CountDownTimer;

import androidx.appcompat.app.AppCompatActivity;

import me.t3sl4.upcortex.R;
import me.t3sl4.upcortex.UI.Components.CircularCountdown.CircularCountdownView;
import me.t3sl4.upcortex.UI.Components.NavigationBar.NavigationBarUtil;
import me.t3sl4.upcortex.Util.Utils;

public class Dashboard extends AppCompatActivity {

    private CircularCountdownView circularCountdownView;
    private long countdownDuration = 5600000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        NavigationBarUtil.hideNavigationBar(this);
        Utils.hideStatusBar(this);

        circularCountdownView = findViewById(R.id.circularCountdownView);
        circularCountdownView.setDuration(countdownDuration);

        startCountdown(countdownDuration);
    }

    private void startCountdown(long duration) {
        new CountDownTimer(duration, 1000) {
            public void onTick(long millisUntilFinished) {
                circularCountdownView.setRemainingTime(millisUntilFinished);
            }

            public void onFinish() {
                circularCountdownView.setRemainingTime(0);
            }
        }.start();
    }
}
