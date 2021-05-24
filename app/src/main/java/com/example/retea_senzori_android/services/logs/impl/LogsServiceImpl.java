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

    public Subject<String> addLog(SensorLogFile sensorLog) {
        return sensorLogFirebaseRepository.createDocument(LOGS_DATA_COLLECTION_PATH, sensorLog);
    }

    public Subject<SensorLogFile> getLogFromId(String id) {
        String pathToLogs = LOGS_DATA_COLLECTION_PATH + "/" + id;
        //Subject<SensorLogFile> subjectLog = new BehaviourSubject<SensorLogFile>();
        //SensorLogFile slFile = new SensorLogFile();
//        SensorDataLogFile sdlFile = new SensorDataLogFile();
//        SensorLogData slData = new SensorLogData((byte)2, (byte)3, (short) 3, 4.0f, 12);
//        sdlFile.addSensorLogData(slData);
//        slFile.sensorLogs = new ArrayList<>();
//        slFile.sensorLogs.add(sdlFile);
//        subjectLog.setState(slFile);
//
//        return subjectLog;

        return sensorLogFirebaseRepository.getDocument(pathToLogs, SensorLogFile.class);
    }

    public Subject<SensorModel> updateSensorModelId(SensorModel sensorModel, SensorLogFile sensorLogFile) {
        Subject<SensorModel> subjectSensor = new Subject<>();
        this.addLog(sensorLogFile).subscribe(id -> {
            sensorModel.logFileId = id;
            subjectSensor.setState(sensorModel);
        });
        return subjectSensor;
    }

    @Override
    public Subject<SensorModel> addLog(SensorModel sensorModel, SensorLogFile sensorLogFile) {
        Subject<SensorModel> subject = new Subject<>();

        if (sensorModel.logFileId == null) {
            this.addLog(sensorLogFile).subscribe(id -> {
                sensorModel.logFileId = id;
                subject.setState(sensorModel);
            });
        } else {
            this.sensorLogFirebaseRepository.setDocument(LOGS_DATA_COLLECTION_PATH, sensorModel.logFileId, sensorLogFile).subscribe(success -> {
                subject.setState(sensorModel);
            });
        }

        return subject;
    }
}
