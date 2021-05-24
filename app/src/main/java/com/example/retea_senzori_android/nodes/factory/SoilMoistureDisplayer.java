package com.example.retea_senzori_android.nodes.factory;

public class SoilMoistureDisplayer implements SensorValueDisplayer {
    @Override
    public String display(float value) {
        if (value <= 370) {
            return "in water";
        } else if (value <= 600) {
            return "soil is humid";
        } else if (value <= 100) {
            return "soil is dry";
        } else {
            return "not in soil";
        }
    }
}
