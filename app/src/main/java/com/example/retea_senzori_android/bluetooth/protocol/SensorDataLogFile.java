package com.example.retea_senzori_android.bluetooth.protocol;

import com.example.retea_senzori_android.sensor.SensorLogData;

import java.util.ArrayList;
import java.util.List;

public class SensorDataLogFile {
    private final List<SensorLogData> logData = new ArrayList<>();

    public void openLogFile() {

    }

    public void closeLogFile() {

    }

    public void addSensorLogData(SensorLogData sensorLogData) {
        logData.add(sensorLogData);
    }

    public List<SensorLogData> getLogData() {
        return logData;
    }
}
