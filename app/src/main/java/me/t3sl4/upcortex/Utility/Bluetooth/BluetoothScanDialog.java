package me.t3sl4.upcortex.Utility.Bluetooth;

import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import java.util.ArrayList;
import java.util.Set;

import me.t3sl4.upcortex.R;

public class BluetoothScanDialog extends DialogFragment {

    private BluetoothAdapter bluetoothAdapter;
    private ArrayAdapter<String> deviceListAdapter;
    private ArrayList<String> deviceList = new ArrayList<>();
    private ListView listViewDevices;
    private final int REQUEST_BLUETOOTH_PERMISSIONS = 1;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_bluetooth_scan, null);

        listViewDevices = view.findViewById(R.id.listViewDevices);
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        deviceListAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1, deviceList);
        listViewDevices.setAdapter(deviceListAdapter);

        startBluetoothScan();

        // Bluetooth cihazına tıklayınca bağlanma işlemi yapabilirsiniz
        listViewDevices.setOnItemClickListener((parent, view1, position, id) -> {
            // Burada cihaz seçimi yapılabilir
            String deviceInfo = deviceList.get(position);
            // Cihaz adresini seç ve gerekli işlemi yap
            // Bağlantı işlemini bluetoothUtil ile yapabilirsiniz
        });

        builder.setView(view)
                .setTitle("Bluetooth Cihazlarını Tara")
                .setNegativeButton("İptal", (dialog, id) -> {
                    // Tarama işlemini durdur
                    bluetoothAdapter.cancelDiscovery();
                    dialog.dismiss();
                });

        return builder.create();
    }

    private void startBluetoothScan() {
        Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
        if (pairedDevices.size() > 0) {
            for (BluetoothDevice device : pairedDevices) {
                deviceList.add("Eşleşmiş: " + device.getName() + "\n" + device.getAddress());
            }
        }

        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        requireActivity().registerReceiver(receiver, filter);
        bluetoothAdapter.startDiscovery();
    }

    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                String deviceInfo = "Bulundu: " + device.getName() + "\n" + device.getAddress();
                if (!deviceList.contains(deviceInfo)) {
                    deviceList.add(deviceInfo);
                    deviceListAdapter.notifyDataSetChanged();
                }
            }
        }
    };

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        requireActivity().unregisterReceiver(receiver);
        bluetoothAdapter.cancelDiscovery();
    }
}