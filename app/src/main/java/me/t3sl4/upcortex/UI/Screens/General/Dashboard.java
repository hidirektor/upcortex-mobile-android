package me.t3sl4.upcortex.UI.Screens.General;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import java.util.Set;

import me.t3sl4.upcortex.R;
import me.t3sl4.upcortex.Service.UserDataService;
import me.t3sl4.upcortex.UI.Components.CircularCountdown.CircularCountdownView;
import me.t3sl4.upcortex.UI.Components.Sneaker.Sneaker;
import me.t3sl4.upcortex.Utility.Screen.ScreenUtil;

public class Dashboard extends AppCompatActivity {

    private CircularCountdownView circularCountdownView;
    private long countdownDuration = 5600000;

    private ScrollView mainScroll;
    private CardView nonSetupCard;

    private LinearLayout addDeviceLayout;

    private BluetoothAdapter bluetoothAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        ScreenUtil.hideNavigationBar(this);

        initializeComponents();

        nonSetup();

        addDeviceLayout.setOnClickListener(v -> startBluetoothDeviceSelection());
    }

    private void initializeComponents() {
        mainScroll = findViewById(R.id.mainScroll);
        nonSetupCard = findViewById(R.id.nonSetupCard);

        addDeviceLayout = findViewById(R.id.addDeviceLayout);

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

    private void startBluetoothDeviceSelection() {
        if (bluetoothAdapter == null || !bluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, 1);
        } else {
            Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
            if (pairedDevices.size() > 0) {
                for (BluetoothDevice device : pairedDevices) {
                    if (device.getName().startsWith("UpCortex -")) {
                        connectToDevice(device);
                        break;
                    }
                }
            }
        }
    }

    private void connectToDevice(BluetoothDevice device) {
        Sneaker.with(Dashboard.this)
                .setTitle(getString(R.string.connected_title))
                .setMessage(getString(R.string.connected_desc))
                .sneakSuccess();

        UserDataService.setUserDeviceID(this, device.getAddress());
    }
}
