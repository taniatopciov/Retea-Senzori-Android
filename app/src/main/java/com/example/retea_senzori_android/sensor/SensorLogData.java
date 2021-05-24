package com.example.retea_senzori_android.sensor;

import org.jetbrains.annotations.NotNull;

import androidx.annotation.NonNull;

public class SensorLogData {
    public final LogType logType;
    public final SensorType sensorType;
    public final int batchIndex; // should be short, but firebase does not support it
    public float value;
    public long time;

    public SensorLogData() {
        logType = LogType.LOG_STARTED;
        sensorType = SensorType.NO_TYPE;
        batchIndex = 0;
        value = 0.0f;
        time = 0;
    }

    public SensorLogData(byte logType, byte sensorType, short batchIndex, float sensorValue, long time) {
        this.logType = LogType.convert(logType);
        this.sensorType = SensorType.convert(sensorType);
        this.batchIndex = batchIndex;
        this.value = sensorValue;
        this.time = time;
    }

    @NonNull
    @NotNull
    @Override
    public String toString() {
        return logType + " " + sensorType + " " + batchIndex + " " + value + " " + time;
    }
}
