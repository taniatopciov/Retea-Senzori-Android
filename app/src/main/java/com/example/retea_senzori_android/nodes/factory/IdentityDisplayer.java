package com.example.retea_senzori_android.nodes.factory;

public class IdentityDisplayer implements SensorValueDisplayer {
    @Override
    public String display(float value) {
        return String.valueOf(value);
    }
}
