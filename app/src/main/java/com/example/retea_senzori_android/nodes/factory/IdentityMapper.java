package com.example.retea_senzori_android.nodes.factory;

public class IdentityMapper implements SensorValueMapper {
    @Override
    public float format(float value) {
        return value;
    }
}
