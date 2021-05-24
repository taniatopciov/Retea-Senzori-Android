package com.example.retea_senzori_android.nodes.factory;

import com.example.retea_senzori_android.models.SensorModel;
import com.example.retea_senzori_android.observables.Subject;
import com.example.retea_senzori_android.sensor.SensorType;

public class Sensor extends Subject<Float> {
    private final SensorModel sensorModel;
    private final SensorValueMapper sensorValueMapper;

    public Sensor(SensorModel sensorModel, SensorValueMapper sensorValueMapper) {
        this.sensorModel = sensorModel;
        this.sensorValueMapper = sensorValueMapper;
    }

    @Override
    public void setState(Float state) {
        super.setState(sensorValueMapper.format(state));
    }

    public SensorModel getSensorModel() {
        return sensorModel;
    }

    public SensorValueMapper getSensorValueMapper() {
        return sensorValueMapper;
    }

    public SensorType getSensorType() {
        return sensorModel.sensorType;
    }
}
