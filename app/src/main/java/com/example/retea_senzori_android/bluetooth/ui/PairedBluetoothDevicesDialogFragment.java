package com.example.retea_senzori_android.bluetooth.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.example.retea_senzori_android.R;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class PairedBluetoothDevicesDialogFragment extends DialogFragment {

    private BluetoothPairedDevicesAdapter bluetoothPairedDevicesAdapter;

    public PairedBluetoothDevicesDialogFragment() {
        // Empty constructor required for DialogFragment
    }

    public static PairedBluetoothDevicesDialogFragment newInstance() {
        return new PairedBluetoothDevicesDialogFragment();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        Set<BluetoothDevice> pairedDevices = new HashSet<>();
        if (bluetoothAdapter != null) {
            pairedDevices = bluetoothAdapter.getBondedDevices();
        }

        View view = LayoutInflater.from(getContext()).inflate(R.layout.paired_devices_list, null);

        bluetoothPairedDevicesAdapter = new BluetoothPairedDevicesAdapter(new ArrayList<>(pairedDevices), device -> {
            BluetoothDeviceConnectEvent bluetoothDeviceConnectEvent = (BluetoothDeviceConnectEvent) getTargetFragment();
            if (bluetoothDeviceConnectEvent != null) {
                bluetoothDeviceConnectEvent.connect(device);
            }
            dismiss();
        });
        RecyclerView pairedBluetoothDevicesRecyclerView = view.findViewById(R.id.pairedBluetoothDevicesRecyclerView);
        pairedBluetoothDevicesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        pairedBluetoothDevicesRecyclerView.setAdapter(bluetoothPairedDevicesAdapter);

        return new AlertDialog.Builder(getActivity())
                .setTitle("Paired Devices")
                .setView(view)
                .setNegativeButton("Close", (dialogInterface, i) -> {
                    dismiss();
                })
                .create();
    }
}
