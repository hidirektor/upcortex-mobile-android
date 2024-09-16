package me.t3sl4.upcortex.UI.Screens.General;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import java.util.ArrayList;
import java.util.Set;

import me.t3sl4.upcortex.R;
import me.t3sl4.upcortex.UI.Components.CircularCountdown.CircularCountdownView;
import me.t3sl4.upcortex.UI.Components.Sneaker.Sneaker;
import me.t3sl4.upcortex.Utility.Bluetooth.BluetoothScanDialog;
import me.t3sl4.upcortex.Utility.Bluetooth.BluetoothUtil;
import me.t3sl4.upcortex.Utility.Screen.ScreenUtil;

public class Dashboard extends AppCompatActivity {

    private CircularCountdownView circularCountdownView;
    private long countdownDuration = 5600000;

    private ScrollView mainScroll;
    private CardView nonSetupCard;

    private LinearLayout addDeviceLayout;

    private BluetoothAdapter bluetoothAdapter;
    private ArrayAdapter<String> deviceListAdapter;
    private ArrayList<String> deviceList = new ArrayList<>();
    private ListView listViewDevices;
    private BluetoothUtil bluetoothUtil;

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

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
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
        if (bluetoothAdapter == null) {
            Sneaker.with(this).setTitle("Bluetooth Özelliği Yok")
                    .setMessage("Cihazınızda Bluetooth özelliği bulunmuyor.")
                    .sneakError();
            return;
        }

        if (!bluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, 1);
            return;
        }

        // Eşleşmiş cihazları al
        Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
        if (pairedDevices.size() > 0) {
            boolean foundDevice = false;
            for (BluetoothDevice device : pairedDevices) {
                if (device.getName().startsWith("UpCortex -")) {
                    connectToDevice(device);
                    foundDevice = true;
                    break;
                }
            }

            if (!foundDevice) {
                Sneaker.with(this).setTitle("Cihaz Bulunamadı")
                        .setMessage("Eşleşmiş cihazlar arasında uygun cihaz bulunamadı.")
                        .sneakWarning();
            }

        } else {
            Sneaker.with(this)
                    .setTitle("Eşleşmiş Cihaz Yok")
                    .setMessage("Cihazınızda daha önce eşleştirilmiş bir Bluetooth cihazı bulunmuyor.")
                    .sneakWarning();

            BluetoothScanDialog bluetoothScanDialog = new BluetoothScanDialog();
            bluetoothScanDialog.show(getSupportFragmentManager(), "BluetoothScanDialog");
        }
    }

    private void connectToDevice(BluetoothDevice device) {
        bluetoothUtil.saveDeviceAddress(this, device.getAddress());
        Sneaker.with(Dashboard.this)
                .setTitle(getString(R.string.connected_title))
                .setMessage(getString(R.string.connected_desc))
                .sneakSuccess();
    }
}
