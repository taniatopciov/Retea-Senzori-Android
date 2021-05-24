package com.example.retea_senzori_android.nodes.factory;

import com.example.retea_senzori_android.bluetooth.protocol.SensorDataLogFile;
import com.example.retea_senzori_android.models.SensorLogFile;
import com.example.retea_senzori_android.sensor.SensorLogData;
import com.example.retea_senzori_android.sensor.SensorType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NodeLogCreator {
    private final SensorLogFile sensorLogFile;
    private final Map<SensorType, SensorValueMapper> sensorValueMapperMap;

    private SensorDataLogFile sensorDataLogFile;

    public NodeLogCreator(List<Sensor> sensors) {
        sensorLogFile = new SensorLogFile();
        sensorValueMapperMap = new HashMap<>();
        for (int i = 0; i < sensors.size(); i++) {
            sensorValueMapperMap.put(sensors.get(i).getSensorType(), sensors.get(i).getSensorValueMapper());
        }
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
        SensorValueMapper sensorValueMapper = sensorValueMapperMap.get(sensorLogData.sensorType);
        if (sensorValueMapper != null) {
            sensorLogData.value = sensorValueMapper.format(sensorLogData.value);
        }
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
