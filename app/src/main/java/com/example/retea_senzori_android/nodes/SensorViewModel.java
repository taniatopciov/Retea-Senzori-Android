package com.example.retea_senzori_android.nodes;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.retea_senzori_android.sensor.SensorType;

public class SensorViewModel extends ViewModel {
    private final MutableLiveData<SensorType> sensorType;

    public SensorViewModel() {
        sensorType = new MutableLiveData<>();
    }

    public MutableLiveData<SensorType> getSensorType() {
        return sensorType;
    }

    public void setSensorType(SensorType sensorType) {
        this.sensorType.setValue(sensorType);
    }
}