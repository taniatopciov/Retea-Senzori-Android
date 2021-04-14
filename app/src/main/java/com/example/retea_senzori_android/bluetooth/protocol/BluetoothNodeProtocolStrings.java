package com.example.retea_senzori_android.bluetooth.protocol;

public class BluetoothNodeProtocolStrings {

    public static final String CONNECT_STRING = "CONN";
    public static final String GO_TO_SLEEP_STRING = "KILL";
    public static final String STOP_STRING = "STOP";
    public static final String LIVE_DATA_STRING = "LIVE";
    public static final String REQUEST_SENSOR_COUNT_STRING = "RQSC";
    public static final String SEND_SENSOR_COUNT_STRING = "SCNT";
    public static final String REQUEST_SENSOR_TYPES_STRING = "RQST";
    public static final String SEND_SENSOR_TYPE_STRING = "STYP";
    public static final String LOG_DATA_PACKET_STRING = "DATA";
    public static final String REPLAY_ALL_DATA_FROM_ALL_LOGS_STRING = "PLAY";
    public static final String REPLAY_DATA_FROM_CURRENT_LOG_STRING = "RQDT";
    public static final String OPEN_LOG_FILE_STRING = "OPEN";
    public static final String CLOSE_LOG_FILE_STRING = "CLOS";
    public static final String SET_UNIX_TIME_STRING = "TIME";
    public static final String SET_SAVE_TO_LOG_TIME_INTERVAL_STRING = "SETI";
    public static final String SD_CARD_ERROR_STRING = "SDER";
    public static final String TIMEOUT_OCCURRED_STRING = "TOUT";
    public static final String LOG_REPLAY_DONE_STRING = "DONE";

    public static final int MESSAGE_HEADER_LENGTH = 4;
}
