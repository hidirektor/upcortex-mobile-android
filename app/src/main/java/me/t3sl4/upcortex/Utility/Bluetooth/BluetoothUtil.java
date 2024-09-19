package me.t3sl4.upcortex.Utility.Bluetooth;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;

import java.util.UUID;

public class BluetoothUtil {

    private static final String PREFS_NAME = "BluetoothPrefs";
    private static final String KEY_DEVICE_ADDRESS = "DeviceAddress";
    private static final int REQUEST_PERMISSION_CONNECT = 3; // Ensure this matches your request code

    private BluetoothAdapter bluetoothAdapter;
    private BluetoothGatt bluetoothGatt;

    public BluetoothUtil() {
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    }

    public interface ConnectionCallback {
        void onConnectionResult(boolean isConnected);
    }

    // Save the device address to shared preferences
    public void saveDeviceAddress(Context context, String address) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        prefs.edit().putString(KEY_DEVICE_ADDRESS, address).apply();
    }

    // Retrieve the saved device address
    public String getSavedDeviceAddress(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return prefs.getString(KEY_DEVICE_ADDRESS, null);
    }

    // Get the saved BluetoothDevice
    public BluetoothDevice getSavedDevice(Context context) {
        String address = getSavedDeviceAddress(context);
        if (address != null && bluetoothAdapter != null) {
            return bluetoothAdapter.getRemoteDevice(address);
        }
        return null;
    }

    // Clear saved device address (if needed)
    public void clearSavedDeviceAddress(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        prefs.edit().remove(KEY_DEVICE_ADDRESS).apply();
    }

    // Set the BluetoothGatt instance
    public void setBluetoothGatt(BluetoothGatt gatt) {
        this.bluetoothGatt = gatt;
    }

    // Get the BluetoothGatt instance
    public BluetoothGatt getBluetoothGatt() {
        return bluetoothGatt;
    }

    // Connect to a Bluetooth device
    public void connectToDevice(Context context, String deviceAddress, ConnectionCallback callback) {
        BluetoothDevice device = bluetoothAdapter.getRemoteDevice(deviceAddress);
        if (device == null) {
            callback.onConnectionResult(false);
            return;
        }

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            // Request permissions if necessary
            ActivityCompat.requestPermissions((Activity) context,
                    new String[]{Manifest.permission.BLUETOOTH_CONNECT},
                    REQUEST_PERMISSION_CONNECT);
            callback.onConnectionResult(false);
            return;
        }

        bluetoothGatt = device.connectGatt(context, false, new BluetoothGattCallback() {
            @Override
            public void onConnectionStateChange(final BluetoothGatt gatt, int status, int newState) {
                super.onConnectionStateChange(gatt, status, newState);
                if (newState == BluetoothProfile.STATE_CONNECTED) {
                    setBluetoothGatt(gatt);
                    callback.onConnectionResult(true);
                } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                    callback.onConnectionResult(false);
                }
            }
            // Implement other callback methods as needed
        });

        // Save device address
        saveDeviceAddress(context, device.getAddress());
    }

    // Read data from a characteristic
    public boolean readCharacteristic(Context context, UUID serviceUUID, UUID characteristicUUID) {
        if (bluetoothGatt == null) return false;

        BluetoothGattCharacteristic characteristic = bluetoothGatt
                .getService(serviceUUID)
                .getCharacteristic(characteristicUUID);

        if (characteristic == null) {
            return false;
        }

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            return false;
        }

        return bluetoothGatt.readCharacteristic(characteristic);
    }

    // Write data to a characteristic
    public boolean writeCharacteristic(Context context, UUID serviceUUID, UUID characteristicUUID, byte[] data) {
        if (bluetoothGatt == null) return false;

        BluetoothGattCharacteristic characteristic = bluetoothGatt
                .getService(serviceUUID)
                .getCharacteristic(characteristicUUID);

        if (characteristic == null) {
            return false;
        }

        characteristic.setValue(data);

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            return false;
        }

        return bluetoothGatt.writeCharacteristic(characteristic);
    }
}