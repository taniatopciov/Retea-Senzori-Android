package com.example.retea_senzori_android.bluetooth.protocol;

import android.bluetooth.BluetoothDevice;

import com.example.retea_senzori_android.observables.Subject;
import com.example.retea_senzori_android.sensor.SensorLogData;
import com.example.retea_senzori_android.sensor.SensorType;

import java.util.List;
import java.util.function.Consumer;

public interface BluetoothNodeProtocol {
    void connect(BluetoothDevice device, Consumer<SDCardErrors> onSDCardErrorReceived);

    void disconnect();

    void readSensorCount(Consumer<Integer> onSensorCountRead);

    void readSensorTypes(int sensorCount, Consumer<SensorType[]> onSensorTypesRead);

    void readLiveData(Consumer<SensorLogData> onLiveDataRead);

    Subject<SensorDataLogFile> readCurrentLogData();

    void readAllLogData(Consumer<List<SensorDataLogFile>> onCurrentLogRead);

    void setUnixTime();

    void setSaveToLogInterval(int interval);

    void stopCurrentCommunication();
}
