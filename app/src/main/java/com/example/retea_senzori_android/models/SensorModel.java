package com.example.retea_senzori_android.models;

import com.example.retea_senzori_android.sensor.SensorType;

public class SensorModel {

    public SensorType sensorType;
    public String logFileId;

    public SensorModel() {

    }

    public SensorModel(SensorType type) {
        this.sensorType = type;
    }
}
