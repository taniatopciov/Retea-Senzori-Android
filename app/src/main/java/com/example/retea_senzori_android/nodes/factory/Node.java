package com.example.retea_senzori_android.nodes.factory;

import com.example.retea_senzori_android.sensor.SensorType;

import java.util.ArrayList;
import java.util.List;

public class Node {
    private final String connectedBluetoothDevice;

    private String nodeName;
    private String lastUpdate;
    private final List<Sensor> sensors;

    protected Node(String nodeName, String connectedBluetoothDevice, String lastUpdate) {
        this.nodeName = nodeName;
        this.connectedBluetoothDevice = connectedBluetoothDevice;
        this.lastUpdate = lastUpdate == null ? "" : lastUpdate;
        this.lastUpdate = "";
        this.sensors = new ArrayList<>();
    }

    public void addSensor(Sensor sensor) {
        this.sensors.add(sensor);
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    public void setLastUpdate(String lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public String getNodeName() {
        return nodeName;
    }

    public String getConnectedBluetoothDevice() {
        return connectedBluetoothDevice;
    }

    public String getLastUpdate() {
        return lastUpdate;
    }

    public List<Sensor> getSensors() {
        return sensors;
    }

    public void updateSensorValue(SensorType sensorType, float value) {
        for (int i = 0; i < sensors.size(); i++) {
            Sensor sensor = sensors.get(i);
            if (sensor.getSensorModel().sensorType.equals(sensorType)) {
                sensor.setState(value);
                return;
            }
        }
    }

    public void setSensors(SensorType[] sensorTypes) {
        if (sensorTypes == null || sensorTypes.length == 0) {
            return;
        }

        sensors.clear();
        for (SensorType sensorType : sensorTypes) {
            addSensor(SensorFactory.fromType(sensorType));
        }
    }
}
