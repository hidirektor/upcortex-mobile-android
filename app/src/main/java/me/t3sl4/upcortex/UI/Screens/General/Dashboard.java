package me.t3sl4.upcortex.UI.Screens.General;

import android.Manifest;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothProfile;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import me.t3sl4.upcortex.Model.Exam.Adapter.ExamAdapter;
import me.t3sl4.upcortex.Model.Exam.Exam;
import me.t3sl4.upcortex.R;
import me.t3sl4.upcortex.UI.Components.CircularCountdown.CircularCountdownView;
import me.t3sl4.upcortex.UI.Components.Sneaker.Sneaker;
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
    private BluetoothLeScanner bluetoothLeScanner;
    private BluetoothGatt bluetoothGatt;
    private BluetoothUtil bluetoothUtil;

    private ExamAdapter examAdapter;
    private LinkedList<Exam> examList = new LinkedList<>();

    private List<BluetoothDevice> scannedDevices = new ArrayList<>();

    private static final int REQUEST_ENABLE_BT = 1;
    private static final int REQUEST_PERMISSION_LOCATION = 2;
    private static final int REQUEST_PERMISSION_CONNECT = 3;
    private static final long SCAN_PERIOD = 10000; // 10 seconds

    // UUIDs for your BLE device's
    private static final UUID SERVICE_UUID = null;
    private static final UUID CHARACTERISTIC_UUID = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);


        ScreenUtil.hideNavigationBar(this);

        initializeComponents();

        loadExamList();

        addDeviceLayout.setOnClickListener(v -> startBLEDeviceScan());

        // Attempt to connect to saved device in the background
        String savedDeviceAddress = bluetoothUtil.getSavedDeviceAddress(Dashboard.this);
        if (savedDeviceAddress != null) {
            showStandartScreen();
            connectToSavedDeviceInBackground(savedDeviceAddress);
        } else {
            nonSetup();
        }
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
        bluetoothLeScanner = bluetoothAdapter.getBluetoothLeScanner();
        bluetoothUtil = new BluetoothUtil();
    }

    private void loadExamList() {
        ExamService.getAllExams(this, () -> {
            examsRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

            runOnUiThread(() -> {
                if (examList != null && !examList.isEmpty()) {
                    examAdapter = new ExamAdapter(Dashboard.this, examList);
                    examsRecyclerView.setAdapter(examAdapter);
                    Log.d("setupExamRecyclerView", "Adapter assigned to RecyclerView successfully");
                } else {
                    Log.e("setupExamRecyclerView", "Exam list is empty or null");
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

    private void startBLEDeviceScan() {
        if (bluetoothAdapter == null || bluetoothLeScanner == null) {
            Sneaker.with(this).setTitle("Bluetooth Not Supported")
                    .setMessage("Your device does not support Bluetooth.")
                    .sneakError();
            return;
        }

        if (!bluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            return;
        }

        // Check permissions
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.BLUETOOTH_SCAN,
                            Manifest.permission.BLUETOOTH_CONNECT,
                            Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_PERMISSION_LOCATION);
            return;
        }

        scannedDevices.clear();
        // Start scanning
        bluetoothLeScanner.startScan(leScanCallback);

        // Stop scanning after SCAN_PERIOD milliseconds
        new Handler().postDelayed(() -> {
            stopBLEDeviceScan();
            if (scannedDevices.isEmpty()) {
                Sneaker.with(this)
                        .setTitle("No Devices Found")
                        .setMessage("No devices starting with 'upCortex' were found.")
                        .sneakWarning();
            } else {
                showDeviceSelectionDialog();
            }
        }, SCAN_PERIOD);
    }

    private void stopBLEDeviceScan() {
        if (bluetoothLeScanner != null) {
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
            if (ActivityCompat.checkSelfPermission(Dashboard.this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            if (device.getName() != null && device.getName().startsWith("upCortex")) {
                if (!scannedDevices.contains(device)) {
                    scannedDevices.add(device);
                }
            }
        }

        @Override
        public void onBatchScanResults(List<ScanResult> results) {
            for (ScanResult result : results) {
                BluetoothDevice device = result.getDevice();
                if (ActivityCompat.checkSelfPermission(Dashboard.this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                if (device.getName() != null && device.getName().startsWith("upCortex")) {
                    if (!scannedDevices.contains(device)) {
                        scannedDevices.add(device);
                    }
                }
            }
        }

        @Override
        public void onScanFailed(int errorCode) {
            Sneaker.with(Dashboard.this)
                    .setTitle("Scan Failed")
                    .setMessage("Error code: " + errorCode)
                    .sneakError();
        }
    };

    private void showDeviceSelectionDialog() {
        List<String> deviceNames = new ArrayList<>();
        for (BluetoothDevice device : scannedDevices) {
            if (ActivityCompat.checkSelfPermission(Dashboard.this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            deviceNames.add(device.getName() + "\n" + device.getAddress());
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select a Device");
        builder.setItems(deviceNames.toArray(new String[0]), (dialog, which) -> {

            BluetoothDevice selectedDevice = scannedDevices.get(which);
            connectToDevice(selectedDevice);
        });

        builder.setOnCancelListener(dialog -> {
            // Stop scanning when dialog is cancelled
            stopBLEDeviceScan();
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void connectToDevice(BluetoothDevice device) {
        stopBLEDeviceScan();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.BLUETOOTH_CONNECT},
                    REQUEST_PERMISSION_CONNECT);
            return;
        }

        bluetoothGatt = device.connectGatt(this, false, gattCallback);

        // Save device address
        bluetoothUtil.saveDeviceAddress(this, device.getAddress());

        Sneaker.with(Dashboard.this)
                .setTitle("Connecting")
                .setMessage("Connecting to " + device.getName())
                .sneakWarning();
    }

    private void connectToSavedDeviceInBackground(String deviceAddress) {
        Handler handler = new Handler();
        handler.post(() -> {
            BluetoothDevice device = bluetoothAdapter.getRemoteDevice(deviceAddress);
            if (device != null) {
                connectToDevice(device);
            } else {
                runOnUiThread(this::nonSetup);
            }
        });
    }

    private final BluetoothGattCallback gattCallback = new BluetoothGattCallback() {
        @Override
        public void onConnectionStateChange(final BluetoothGatt gatt, int status, int newState) {
            if (newState == BluetoothProfile.STATE_CONNECTED) {
                Log.d("BLE", "Connected to GATT server.");
                if (ActivityCompat.checkSelfPermission(Dashboard.this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                bluetoothGatt = gatt;
                bluetoothUtil.setBluetoothGatt(bluetoothGatt);

                runOnUiThread(() -> {
                    Sneaker.with(Dashboard.this)
                            .setTitle("Connected")
                            .setMessage("Connected to " + gatt.getDevice().getName())
                            .sneakSuccess();
                    showStandartScreen(); // Call showStandartScreen() on successful connection
                });
                // Discover services
                gatt.discoverServices();
            } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                Log.d("BLE", "Disconnected from GATT server.");
                runOnUiThread(() -> {
                    Sneaker.with(Dashboard.this)
                            .setTitle("Disconnected")
                            .setMessage("Disconnected from " + gatt.getDevice().getName())
                            .sneakError();
                    nonSetup();
                });
            }
        }

        @Override
        public void onServicesDiscovered(final BluetoothGatt gatt, int status) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                Log.d("BLE", "Services discovered.");
                // Enable notifications or read characteristics as needed
                BluetoothGattCharacteristic characteristic = gatt
                        .getService(SERVICE_UUID)
                        .getCharacteristic(CHARACTERISTIC_UUID);
                if (characteristic != null) {
                    enableNotifications(gatt, characteristic);
                }
            } else {
                Log.w("BLE", "onServicesDiscovered received: " + status);
            }
        }

        @Override
        public void onCharacteristicRead(final BluetoothGatt gatt,
                                         final BluetoothGattCharacteristic characteristic,
                                         int status) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                byte[] data = characteristic.getValue();
                Log.d("BLE", "Characteristic read: " + bytesToHex(data));
            }
        }

        @Override
        public void onCharacteristicChanged(final BluetoothGatt gatt,
                                            final BluetoothGattCharacteristic characteristic) {
            // This is called when notifications are received
            byte[] data = characteristic.getValue();
            Log.d("BLE", "Notification received: " + bytesToHex(data));
        }

        @Override
        public void onDescriptorWrite(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
            // After enabling notifications, this method is called
            if (status == BluetoothGatt.GATT_SUCCESS) {
                Log.d("BLE", "Descriptor write successful");
            } else {
                Log.e("BLE", "Descriptor write failed with status: " + status);
            }
        }
    };

    private void enableNotifications(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.BLUETOOTH_CONNECT},
                    REQUEST_PERMISSION_CONNECT);
            return;
        }

        gatt.setCharacteristicNotification(characteristic, true);

        // Write to the descriptor to enable notifications
        BluetoothGattDescriptor descriptor = characteristic.getDescriptor(
                UUID.fromString("00002902-0000-1000-8000-00805f9b34fb"));
        if (descriptor != null) {
            descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
            gatt.writeDescriptor(descriptor);
        } else {
            Log.e("BLE", "Descriptor not found for characteristic " + characteristic.getUuid());
        }
    }

    private String bytesToHex(byte[] bytes) {
        StringBuilder stringBuilder = new StringBuilder(bytes.length * 2);
        for (byte b : bytes) {
            stringBuilder.append(String.format("%02X ", b));
        }
        return stringBuilder.toString();
    }

    // Function to read data from the device
    private void readDataFromDevice() {
        boolean success = bluetoothUtil.readCharacteristic(Dashboard.this, SERVICE_UUID, CHARACTERISTIC_UUID);
        if (!success) {
            Log.e("BLE", "Failed to initiate characteristic read");
        }
    }

    // Function to send data to the device
    private void sendDataToDevice(byte[] data) {
        boolean success = bluetoothUtil.writeCharacteristic(Dashboard.this, SERVICE_UUID, CHARACTERISTIC_UUID, data);
        if (!success) {
            Log.e("BLE", "Failed to write characteristic");
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION_LOCATION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startBLEDeviceScan();
            } else {
                Sneaker.with(this).setTitle("Permission Denied")
                        .setMessage("Location permission is required for BLE scanning.")
                        .sneakError();
            }
        } else if (requestCode == REQUEST_PERMISSION_CONNECT) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Retry connecting to the device
                if (bluetoothGatt != null) {
                    if (ActivityCompat.checkSelfPermission(Dashboard.this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    bluetoothGatt.connect();
                }
            } else {
                Sneaker.with(this).setTitle("Permission Denied")
                        .setMessage("Bluetooth connect permission is required.")
                        .sneakError();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (bluetoothGatt != null) {
            if (ActivityCompat.checkSelfPermission(Dashboard.this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            bluetoothGatt.close();
            bluetoothGatt = null;
        }
    }
}