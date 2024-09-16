package me.t3sl4.upcortex.Utility.Bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

public class BluetoothUtil {

    private BluetoothAdapter bluetoothAdapter;
    private BluetoothSocket bluetoothSocket;
    private BluetoothDevice connectedDevice;
    private InputStream inputStream;

    private final UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    private static final String PREFS_NAME = "BluetoothPrefs";
    private static final String DEVICE_ADDRESS_KEY = "device_address";

    public BluetoothUtil() {
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    }

    public boolean isBluetoothEnabled() {
        return bluetoothAdapter != null && bluetoothAdapter.isEnabled();
    }

    public void saveDeviceAddress(Context context, String address) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(DEVICE_ADDRESS_KEY, address);
        editor.apply();
    }

    public String getSavedDeviceAddress(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return prefs.getString(DEVICE_ADDRESS_KEY, null);
    }

    public void connectToDevice(String address, ConnectionCallback callback) {
        BluetoothDevice device = bluetoothAdapter.getRemoteDevice(address);
        new ConnectTask(device, callback).execute();
    }

    public static void clearSavedDeviceAddress(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.remove(DEVICE_ADDRESS_KEY);
        editor.apply();
    }

    public boolean isConnected() {
        return bluetoothSocket != null && bluetoothSocket.isConnected();
    }

    private class ConnectTask extends AsyncTask<Void, Void, Boolean> {
        private BluetoothDevice device;
        private ConnectionCallback callback;

        public ConnectTask(BluetoothDevice device, ConnectionCallback callback) {
            this.device = device;
            this.callback = callback;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            try {
                bluetoothSocket = device.createRfcommSocketToServiceRecord(uuid);
                bluetoothSocket.connect();
                inputStream = bluetoothSocket.getInputStream();
                connectedDevice = device;
                return true;
            } catch (IOException e) {
                Log.e("BluetoothUtil", "Bağlanma hatası: " + e.getMessage());
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if (callback != null) {
                callback.onConnectionResult(result);
            }
        }
    }

    public interface ConnectionCallback {
        void onConnectionResult(boolean isConnected);
    }

    public void closeConnection() {
        try {
            if (inputStream != null) {
                inputStream.close();
            }
            if (bluetoothSocket != null) {
                bluetoothSocket.close();
            }
        } catch (IOException e) {
            Log.e("BluetoothUtil", "Bağlantı kapatılamadı: " + e.getMessage());
        }
    }
}