package com.example.retea_senzori_android.services.logs;

import com.example.retea_senzori_android.models.SensorLogFile;
import com.example.retea_senzori_android.observables.Subject;

public interface LogsService {
    Subject<String> addLog(SensorLogFile sensorLog);

    Subject<SensorLogFile> getLogFromId(String id);

    Subject<String> addLog(String logFileId, SensorLogFile sensorLogFile);
}
