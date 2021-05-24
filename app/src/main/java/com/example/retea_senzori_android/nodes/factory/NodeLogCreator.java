package com.example.retea_senzori_android.nodes.factory;

import com.example.retea_senzori_android.bluetooth.protocol.SensorDataLogFile;
import com.example.retea_senzori_android.models.SensorLogFile;
import com.example.retea_senzori_android.sensor.SensorLogData;

public class NodeLogCreator {
    private final SensorLogFile sensorLogFile;

    private SensorDataLogFile sensorDataLogFile;

    public NodeLogCreator() {
        sensorLogFile = new SensorLogFile();
    }

    public void startLog() {
        sensorDataLogFile = new SensorDataLogFile();
    }

    public void endLog() {
        if (sensorDataLogFile != null) {
            sensorLogFile.sensorLogs.add(sensorDataLogFile);
        }
    }

    public void addSensorLogData(SensorLogData sensorLogData) {
        sensorDataLogFile.addSensorLogData(sensorLogData);
    }

    public SensorLogFile getSensorLogFile() {
        return sensorLogFile;
    }
//
//    public Map<SensorType, SensorLogFile> getLogFileSeparated() {
//        Map<SensorType, SensorLogFile> sensorTypeSensorLogFileMap = new HashMap<>();
//
//        for (Map.Entry<SensorType, SensorValueMapper> entry : sensorValueMapperMap.entrySet()) {
//            SensorType sensorType = entry.getKey();
//            sensorTypeSensorLogFileMap.put(sensorType, new SensorLogFile());
//        }
//
//        for (SensorDataLogFile logFile : sensorLogFile.sensorLogs) {
//            for (SensorLogData logData : logFile.getLogData()) {
//
//                sensorTypeSensorLogFileMap.get(logData.sensorType).sensorLogs.add()
//            }
//        }
//
//        for (int i = 0; i <; i++) {
//
//        }
//
//        return sensorTypeSensorLogFileMap;
//    }
}
