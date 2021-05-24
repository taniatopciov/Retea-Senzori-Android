package com.example.retea_senzori_android.nodes.factory;

import com.example.retea_senzori_android.models.SensorModel;
import com.example.retea_senzori_android.observables.Subject;
import com.example.retea_senzori_android.sensor.SensorType;

public class Sensor extends Subject<Float> {
    private final SensorModel sensorModel;

    public Sensor(SensorModel sensorModel) {
        this.sensorModel = sensorModel;
    }

    public SensorModel getSensorModel() {
        return sensorModel;
    }

    public SensorType getSensorType() {
        return sensorModel.sensorType;
    }
}
