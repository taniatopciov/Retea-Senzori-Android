package com.example.retea_senzori_android.models;

import com.example.retea_senzori_android.bluetooth.protocol.SensorDataLogFile;
import com.example.retea_senzori_android.persistance.FirebaseDocument;

import java.util.List;

public class SensorLogFile extends FirebaseDocument {
    public List<SensorDataLogFile> sensorLogs;
}
