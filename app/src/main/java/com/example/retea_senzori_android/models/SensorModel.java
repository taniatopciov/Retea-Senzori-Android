package com.example.retea_senzori_android.models;

import java.io.Serializable;

public class SensorModel implements Serializable {

    public String sensorType;
    public String logFileId;

    public SensorModel() {

    }

    public SensorModel(String type) {
        this.sensorType = type;
    }
}
