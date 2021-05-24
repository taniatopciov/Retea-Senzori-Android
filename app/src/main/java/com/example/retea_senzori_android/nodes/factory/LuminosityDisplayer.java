package com.example.retea_senzori_android.nodes.factory;

public class LuminosityDisplayer implements SensorValueDisplayer {
    @Override
    public String display(float value) {
        if (value <= 10) {
            return "dark";
        } else if (value <= 200) {
            return "dim light";
        } else if (value <= 500) {
            return "light";
        } else if (value <= 800) {
            return "bright light";
        } else {
            return "very bright light";
        }
    }
}
