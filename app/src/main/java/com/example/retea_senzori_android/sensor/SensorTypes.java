package com.example.retea_senzori_android.sensor;

public enum SensorTypes {
    NO_TYPE((byte) 0),
    RAIN_SENSOR((byte) 1),
    GAS_SENSOR((byte) 2),
    SOIL_MOISTURE_SENSOR((byte) 3),
    LIGHT_SENSOR((byte) 4),
    TEMP_SENSOR((byte) 5),
    SIMPLE_TEMP_SENSOR((byte) 6);

    private final byte value;

    SensorTypes(byte value) {
        this.value = value;
    }

    public static SensorTypes convert(byte value) {
        for (SensorTypes type : SensorTypes.values()) {
            if (type.value == value) {
                return type;
            }
        }
        throw new IllegalArgumentException("Invalid Sensor Type Value ");
    }
}
