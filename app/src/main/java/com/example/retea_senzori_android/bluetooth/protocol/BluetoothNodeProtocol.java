package com.example.retea_senzori_android.bluetooth.protocol;

import android.bluetooth.BluetoothDevice;

public interface BluetoothNodeProtocol {
    void connect(BluetoothDevice device);

    void disconnect();

    void sendCommand(String command);
}
