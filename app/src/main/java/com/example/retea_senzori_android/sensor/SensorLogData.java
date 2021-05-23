package com.example.retea_senzori_android.sensor;

public class SensorLogData {
    public final LogType logType;
    public final SensorType sensorType;
    public final short batchIndex;
    public final float value;
    public final long time;

    public SensorLogData(byte logType, byte sensorType, short batchIndex, float sensorValue, long time) {
        this.logType = LogType.convert(logType);
        this.sensorType = SensorType.convert(sensorType);
        this.batchIndex = batchIndex;
        this.value = sensorValue;
        this.time = time;
    }
}
