package com.example.retea_senzori_android.services.logs;

import com.example.retea_senzori_android.models.SensorLogFile;
import com.example.retea_senzori_android.models.SensorModel;
import com.example.retea_senzori_android.observables.Subject;

public interface LogsService {
    Subject<String> addLog(SensorLogFile sensorLog);

    Subject<SensorLogFile> getLogFromId(String id);

    Subject<SensorModel> updateSensorModelId(SensorModel sensorModel, SensorLogFile sensorLogFile);

    Subject<SensorModel> addLog(SensorModel sensorModel, SensorLogFile sensorLogFile);
}
