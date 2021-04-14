package com.example.retea_senzori_android.bluetooth.protocol;

public enum SDCardErrors {
    SD_INIT_ERROR((byte) 0),
    SD_OPEN_FILE_ERROR((byte) 1),
    SD_CREATE_FILE_ERROR((byte) 2),
    SD_CREATE_FOLDER_ERROR((byte) 3),
    SD_READ_ERROR((byte) 4),
    SD_WRITE_ERROR((byte) 5);

    private final byte value;

    SDCardErrors(byte value) {
        this.value = value;
    }

    public static SDCardErrors convert(byte value) {
        for (SDCardErrors type : SDCardErrors.values()) {
            if (type.value == value) {
                return type;
            }
        }
        throw new IllegalArgumentException("Invalid Sensor Type Value ");
    }
}
