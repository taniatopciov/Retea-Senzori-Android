package com.example.retea_senzori_android.sensor;

import org.jetbrains.annotations.NotNull;

import androidx.annotation.NonNull;

public enum SensorType {
    NO_TYPE((byte) 0),
    RAIN_SENSOR((byte) 1),
    GAS_SENSOR((byte) 2),
    SOIL_MOISTURE_SENSOR((byte) 3),
    LIGHT_SENSOR((byte) 4),
    TEMP_SENSOR((byte) 5),
    SIMPLE_TEMP_SENSOR((byte) 6);

    private final byte value;

    SensorType(byte value) {
        this.value = value;
    }

    public static SensorType convert(byte value) {
        for (SensorType type : SensorType.values()) {
            if (type.value == value) {
                return type;
            }
        }
        throw new IllegalArgumentException("Invalid Sensor Type Value ");
    }

    @NonNull
    @NotNull
    @Override
    public String toString() {
        String value = super.toString();

        if (value.contains("TEMP")) {
            value = value.replace("TEMP", "TEMPERATURE");
        }

        return value.replace("_", " ");
    }
}
