package me.t3sl4.upcortex.UI.Screens.General;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothProfile;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import me.t3sl4.upcortex.Model.Exam.Adapter.ExamAdapter;
import me.t3sl4.upcortex.Model.Exam.Exam;
import me.t3sl4.upcortex.R;
import me.t3sl4.upcortex.UI.Components.CircularCountdown.CircularCountdownView;
import me.t3sl4.upcortex.UI.Components.Sneaker.Sneaker;
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

    private ExamAdapter examAdapter;
    private LinkedList<Exam> examList = new LinkedList<>();

    private BluetoothAdapter bluetoothAdapter;
    private BluetoothLeScanner bluetoothLeScanner;
    public boolean scanning;
    public Handler handler;
    public static final long SCAN_PERIOD = 10000; // 10 seconds
    public List<BluetoothDevice> scannedDevices;
    public ArrayAdapter<String> deviceListAdapter;
    public AlertDialog deviceSelectionDialog;
    public List<String> deviceNames = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        ScreenUtil.hideNavigationBar(this);

        initializeComponents();

        checkBluetoothDeviceStatus();
        loadExamList();

        addDeviceLayout.setOnClickListener(v -> startBluetoothDeviceSelection());

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter != null) {
            // Get BluetoothLeScanner instance from BluetoothAdapter
            bluetoothLeScanner = bluetoothAdapter.getBluetoothLeScanner();
        } else {
            Toast.makeText(this, "Bluetooth not supported on this device", Toast.LENGTH_SHORT).show();
        }
        scanning = false;
        handler = new Handler();
        scannedDevices = new ArrayList<>();
    }

    private void initializeComponents() {
        headerCard = findViewById(R.id.headerCard);
        usingDetailCard = findViewById(R.id.usingDetailCard);
        nonSetupCard = findViewById(R.id.nonSetupCard);

        addDeviceLayout = findViewById(R.id.addDeviceLayout);

        circularCountdownView = findViewById(R.id.circularCountdownView);
        circularCountdownView.setDuration(countdownDuration);

        examsRecyclerView = findViewById(R.id.examsRecyclerView);
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
        //String savedDeviceAddress = bluetoothUtil.getSavedDeviceAddress(Dashboard.this);
        String savedDeviceAddress = null;

        if (savedDeviceAddress != null) {
            showStandartScreen();
        } else {
            nonSetup();
        }
    }

    private void startBluetoothDeviceSelection() {
        startScan(Dashboard.this);
    }

    public void startScan(Context context) {
        if (!scanning) {
            handler.postDelayed(() -> {
                scanning = false;
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                bluetoothLeScanner.stopScan(leScanCallback);
            }, SCAN_PERIOD);

            scanning = true;
            bluetoothLeScanner.startScan(leScanCallback);
            showDeviceSelectionDialog(context);
        } else {
            scanning = false;
            bluetoothLeScanner.stopScan(leScanCallback);
        }
    }

    public void stopScan() {
        if (bluetoothLeScanner != null && scanning) {
            scanning = false;
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            bluetoothLeScanner.stopScan(leScanCallback);
        }
    }

    private final ScanCallback leScanCallback = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            BluetoothDevice device = result.getDevice();
            if (!scannedDevices.contains(device)) {
                scannedDevices.add(device);
                String deviceInfo = getDeviceName(device) + "\n" + device.getAddress();
                deviceNames.add(deviceInfo);
                deviceListAdapter.notifyDataSetChanged();
            }
        }
    };

    private void showDeviceSelectionDialog(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View dialogView = View.inflate(context, R.layout.dialog_bluetooth_scan, null);
        ListView listViewDevices = dialogView.findViewById(R.id.listViewDevices);

        // Initialize the ArrayAdapter for displaying device names
        deviceListAdapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, deviceNames);
        listViewDevices.setAdapter(deviceListAdapter);

        listViewDevices.setOnItemClickListener((parent, view, position, id) -> {
            BluetoothDevice selectedDevice = scannedDevices.get(position);
            connectToDevice(selectedDevice, context); // Perform connection with selected device
            if (deviceSelectionDialog != null && deviceSelectionDialog.isShowing()) {
                deviceSelectionDialog.dismiss(); // Close dialog after selection
            }
        });

        builder.setView(dialogView);
        builder.setOnCancelListener(dialog -> stopScan()); // Stop scanning if dialog is cancelled
        deviceSelectionDialog = builder.create();
        deviceSelectionDialog.show();
    }

    private void connectToDevice(BluetoothDevice device, Context context) {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED
                && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            ActivityCompat.requestPermissions((Activity) context,
                    new String[]{Manifest.permission.BLUETOOTH_CONNECT}, 1001);
            return;
        }

        BluetoothGatt bluetoothGatt = device.connectGatt(context, false, new BluetoothGattCallback() {
            @Override
            public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
                if (newState == BluetoothProfile.STATE_CONNECTED) {
                    Log.d("BluetoothScanner", "Connected to GATT server.");

                    if (ActivityCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    gatt.discoverServices();

                    runOnUiThread(() -> {
                        Toast.makeText(context, "Connected to " + getDeviceName(device), Toast.LENGTH_SHORT).show();
                    });
                } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                    Log.d("BluetoothScanner", "Disconnected from GATT server.");

                    runOnUiThread(() -> {
                        Toast.makeText(context, "Disconnected from " + getDeviceName(device), Toast.LENGTH_SHORT).show();
                    });
                }
            }

            @Override
            public void onServicesDiscovered(BluetoothGatt gatt, int status) {
                if (status == BluetoothGatt.GATT_SUCCESS) {
                    Log.d("BluetoothScanner", "Services discovered.");
                    // Here you can interact with the discovered services and characteristics.
                } else {
                    Log.w("BluetoothScanner", "onServicesDiscovered received: " + status);
                }
            }

            @Override
            public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
                if (status == BluetoothGatt.GATT_SUCCESS) {
                    Log.d("BluetoothScanner", "Characteristic read: " + characteristic.getValue());
                    // You can process the characteristic value here
                }
            }
        });

        Log.d("BluetoothScanner", "Attempting to connect to " + device.getName());
    }

    private String getDeviceName(BluetoothDevice device) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            return null;
        }
        String name = device.getName();
        return (name != null && !name.isEmpty()) ? name : "Unknown Device";
    }
}