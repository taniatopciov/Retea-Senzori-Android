package com.example.retea_senzori_android.nodes.factory;

import com.example.retea_senzori_android.bluetooth.protocol.SensorDataLogFile;
import com.example.retea_senzori_android.models.SensorLogFile;
import com.example.retea_senzori_android.sensor.LogType;
import com.example.retea_senzori_android.sensor.SensorLogData;

import java.util.List;

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
            List<SensorLogData> logData = sensorDataLogFile.getLogData();

            sensorDataLogFile.setLogData(updateLogDataTimes(logData));

            sensorLogFile.sensorLogs.add(sensorDataLogFile);
        }
    }

    public void addSensorLogData(SensorLogData sensorLogData) {
        sensorDataLogFile.addSensorLogData(sensorLogData);
    }

    public SensorLogFile getSensorLogFile() {
        return sensorLogFile;
    }

    private List<SensorLogData> updateLogDataTimes(List<SensorLogData> logData) {
        int timeIndex = -1;
        for (int i = 0; i < logData.size(); i++) {
            if (logData.get(i).logType.equals(LogType.LOG_TIME_SET)) {
                timeIndex = i;
                break;
            }
        }

        if (timeIndex == -1) {
            return logData;
        }
        long time = logData.get(timeIndex).time;

        for (int i = timeIndex - 1; i >= 0; i--) {
            logData.get(i).time = time - logData.get(i).time;
        }

        for (int i = timeIndex + 1; i < logData.size(); i++) {
            if (logData.get(i).logType == LogType.LOG_TIME_SET) {
                time = logData.get(i).time;
            } else {
                logData.get(i).time = time + logData.get(i).time;
            }
        }

        return logData;
    }
}