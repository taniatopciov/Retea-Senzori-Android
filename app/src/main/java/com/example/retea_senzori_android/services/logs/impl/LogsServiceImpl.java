package com.example.retea_senzori_android.services.logs.impl;

import com.example.retea_senzori_android.models.SensorLogFile;
import com.example.retea_senzori_android.models.SensorModel;
import com.example.retea_senzori_android.observables.Subject;
import com.example.retea_senzori_android.persistance.FirebaseRepository;
import com.example.retea_senzori_android.services.logs.LogsService;

public class LogsServiceImpl implements LogsService {
    private static final String LOGS_DATA_COLLECTION_PATH = "logs";
    private final FirebaseRepository<SensorLogFile> sensorLogFirebaseRepository;

    public LogsServiceImpl(FirebaseRepository<SensorLogFile> sensorLogFirebaseRepository) {
        this.sensorLogFirebaseRepository = sensorLogFirebaseRepository;
    }

    public Subject<String> addLog(SensorLogFile sensorLog){
        return sensorLogFirebaseRepository.createDocument(LOGS_DATA_COLLECTION_PATH, sensorLog);
    }

    public Subject<SensorLogFile> getLogFromId(String id){
        String pathToLogs = LOGS_DATA_COLLECTION_PATH + "/" + id;
        return sensorLogFirebaseRepository.getDocument(pathToLogs, SensorLogFile.class);
    }

    public Subject<SensorModel> updateSensorModelId(SensorModel sensorModel, SensorLogFile sensorLogFile){
        Subject<SensorModel> subjectSensor = new Subject<SensorModel>();
        this.addLog(sensorLogFile).subscribe( id -> {sensorModel.logFileId = id; subjectSensor.setState(sensorModel);});
        return subjectSensor;
    }
}
