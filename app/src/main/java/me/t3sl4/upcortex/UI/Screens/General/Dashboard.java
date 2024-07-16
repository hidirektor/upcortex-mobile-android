package me.t3sl4.upcortex.UI.Screens.General;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.ScrollView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import me.t3sl4.upcortex.R;
import me.t3sl4.upcortex.UI.Components.CircularCountdown.CircularCountdownView;
import me.t3sl4.upcortex.Util.Utils;

public class Dashboard extends AppCompatActivity {

    private CircularCountdownView circularCountdownView;
    private long countdownDuration = 5600000;

    private ScrollView mainScroll;
    private CardView nonSetupCard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        Utils.hideNavigationBar(this);

        initializeComponents();

        nonSetup();
    }

    private void initializeComponents() {
        mainScroll = findViewById(R.id.mainScroll);
        nonSetupCard = findViewById(R.id.nonSetupCard);

        circularCountdownView = findViewById(R.id.circularCountdownView);
        circularCountdownView.setDuration(countdownDuration);
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

    private void nonSetup() {
        mainScroll.setVisibility(View.GONE);
        nonSetupCard.setVisibility(View.VISIBLE);
    }

    private void showStandartScreen() {
        mainScroll.setVisibility(View.VISIBLE);
        nonSetupCard.setVisibility(View.GONE);

        startCountdown(countdownDuration);
    }
}
