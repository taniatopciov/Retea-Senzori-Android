package com.example.retea_senzori_android.nodes.factory;

public class GasDisplayer implements SensorValueDisplayer {
    @Override
    public String display(float value) {
        if (value <= 190) {
            return "good air quality";
        } else if (value <= 300) {
            return "moderate air quality";
        } else {
            return "bad air quality";
        }
    }
}
