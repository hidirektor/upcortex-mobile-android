package me.t3sl4.upcortex.UI.Screens.General;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Set;

import me.t3sl4.upcortex.Model.Exam.Adapter.ExamAdapter;
import me.t3sl4.upcortex.Model.Exam.Exam;
import me.t3sl4.upcortex.R;
import me.t3sl4.upcortex.UI.Components.CircularCountdown.CircularCountdownView;
import me.t3sl4.upcortex.UI.Components.Sneaker.Sneaker;
import me.t3sl4.upcortex.UI.Screens.Auth.Register.Register1;
import me.t3sl4.upcortex.Utility.Bluetooth.BluetoothScanDialog;
import me.t3sl4.upcortex.Utility.Bluetooth.BluetoothUtil;
import me.t3sl4.upcortex.Utility.HTTP.Requests.Exam.ExamService;
import me.t3sl4.upcortex.Utility.Screen.ScreenUtil;

public class Dashboard extends AppCompatActivity {

    private CircularCountdownView circularCountdownView;
    private long countdownDuration = 5600000;

    private CardView headerCard;
    private CardView usingDetailCard;
    private CardView nonSetupCard;

    private LinearLayout addDeviceLayout;

    private RecyclerView examsRecyclerView;

    private BluetoothAdapter bluetoothAdapter;
    private ArrayAdapter<String> deviceListAdapter;
    private ArrayList<String> deviceList = new ArrayList<>();
    private ListView listViewDevices;
    private BluetoothUtil bluetoothUtil;

    private ExamAdapter examAdapter;
    private LinkedList<Exam> examList = new LinkedList<>();

    private FloatingActionButton buyButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        ScreenUtil.hideNavigationBar(this);

        initializeComponents();

        checkBluetoothDeviceStatus();
        loadExamList();

        addDeviceLayout.setOnClickListener(v -> startBluetoothDeviceSelection());

        buyButton.setOnClickListener(v -> {
            Intent registerIntent = new Intent(Dashboard.this, Register1.class);
            startActivity(registerIntent);
        });
    }

    private void initializeComponents() {
        headerCard = findViewById(R.id.headerCard);
        usingDetailCard = findViewById(R.id.usingDetailCard);
        nonSetupCard = findViewById(R.id.nonSetupCard);

        addDeviceLayout = findViewById(R.id.addDeviceLayout);

        circularCountdownView = findViewById(R.id.circularCountdownView);
        circularCountdownView.setDuration(countdownDuration);

        examsRecyclerView = findViewById(R.id.examsRecyclerView);

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        bluetoothUtil = new BluetoothUtil();

        buyButton = findViewById(R.id.homeConstraint);
        Animation pulseAnimation = AnimationUtils.loadAnimation(this, R.anim.pulse);
        buyButton.startAnimation(pulseAnimation);
    }

    private void loadExamList() {
        //Boolean isFirstExam = SharedPreferencesManager.getSharedPref("firstExam", this, false);

        ExamService.getAllExams(this, () -> {
            examsRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

            runOnUiThread(() -> {
                if (examList != null && !examList.isEmpty()) {
                    examAdapter = new ExamAdapter(Dashboard.this, examList);
                    examsRecyclerView.setAdapter(examAdapter);
                    Log.d("setupExamRecyclerView", "Adapter RecyclerView'a başarıyla atandı");
                } else {
                    Log.e("setupExamRecyclerView", "News list is empty or null");
                }
            });
        }, () -> {
            Sneaker.with(Dashboard.this)
                    .setTitle(getString(R.string.error_title))
                    .setMessage(getString(R.string.get_exams_error))
                    .sneakError();
        }, examList);
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
        headerCard.setVisibility(View.GONE);
        usingDetailCard.setVisibility(View.GONE);
        nonSetupCard.setVisibility(View.VISIBLE);
    }

    private void showStandartScreen() {
        headerCard.setVisibility(View.VISIBLE);
        usingDetailCard.setVisibility(View.VISIBLE);
        nonSetupCard.setVisibility(View.GONE);

        startCountdown(countdownDuration);
    }

    private void checkBluetoothDeviceStatus() {
        String savedDeviceAddress = bluetoothUtil.getSavedDeviceAddress(Dashboard.this);

        if (savedDeviceAddress != null) {
            showStandartScreen();
        } else {
            nonSetup();
        }
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
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            startActivityForResult(enableBtIntent, 1);
            return;
        }

        // Eşleşmiş cihazları al
        Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
        if (pairedDevices.size() > 0) {
            boolean foundDevice = false;
            for (BluetoothDevice device : pairedDevices) {
                if (device.getName().startsWith("upCortex HW")) {
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