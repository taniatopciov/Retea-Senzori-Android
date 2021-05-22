package com.example.retea_senzori_android.sensor;

import org.jetbrains.annotations.NotNull;

import androidx.annotation.NonNull;

public enum LogType {
    LOG_STARTED((byte) 0),
    LOG_DATA((byte) 1),
    LOG_TIME_SET((byte) 2),
    LOG_ENDED((byte) 3),
    LOG_LIVE_DATA((byte) 4);

    private final byte value;

    LogType(byte value) {
        this.value = value;
    }

    public static LogType convert(byte value) {
        for (LogType type : LogType.values()) {
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
        return super.toString().replace("_", " ");
    }
}
