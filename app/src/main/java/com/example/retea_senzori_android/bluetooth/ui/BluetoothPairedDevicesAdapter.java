package com.example.retea_senzori_android.bluetooth.ui;

import android.bluetooth.BluetoothDevice;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.retea_senzori_android.R;

import java.util.List;
import java.util.function.Consumer;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class BluetoothPairedDevicesAdapter extends RecyclerView.Adapter<BluetoothPairedDevicesAdapter.ViewHolder> {

    private final List<BluetoothDevice> bluetoothDevices;
    private Consumer<BluetoothDevice> onItemClick;

    public BluetoothPairedDevicesAdapter(List<BluetoothDevice> bluetoothDevices, Consumer<BluetoothDevice> onItemClick) {
        this.onItemClick = onItemClick;
        this.bluetoothDevices = bluetoothDevices;
    }

    @NonNull
    @Override
    public BluetoothPairedDevicesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bluetooth_paired_device, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BluetoothPairedDevicesAdapter.ViewHolder holder, int position) {
        holder.deviceNameTextView.setText(bluetoothDevices.get(position).getName());
        holder.deviceConnectButton.setOnClickListener(view -> onItemClick.accept(bluetoothDevices.get(position)));
    }

    @Override
    public int getItemCount() {
        return bluetoothDevices.size();
    }

    public void addDevice(BluetoothDevice device) {
        bluetoothDevices.add(device);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView deviceNameTextView;
        public Button deviceConnectButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            deviceNameTextView = itemView.findViewById(R.id.deviceNameTextView);
            deviceConnectButton = itemView.findViewById(R.id.connectToDeviceButton);
        }
    }
}
