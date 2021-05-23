package com.example.retea_senzori_android.nodes.factory;

import com.example.retea_senzori_android.models.SensorModel;
import com.example.retea_senzori_android.observables.Subject;

public class Sensor extends Subject<Float> {
    private final SensorModel sensorModel;
    private final SensorValueMapper sensorValueMapper;

    public Sensor(SensorModel sensorModel, SensorValueMapper sensorValueMapper) {
        this.sensorModel = sensorModel;
        this.sensorValueMapper = sensorValueMapper;
    }

    public float formatValue(float value) {
        return sensorValueMapper.format(value);
    }

    public int formatValue(int value) {
        return sensorValueMapper.format(value);
    }

    public SensorModel getSensorModel() {
        return sensorModel;
    }
}
