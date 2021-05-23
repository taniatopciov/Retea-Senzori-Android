package com.example.retea_senzori_android.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class NodeModel implements Serializable {

    public String nodeName;
    public String connectedBluetoothDevice;
    public String lastUpdate;
    public List<SensorModel> sensors;

    public NodeModel() {

    }

    public NodeModel(String nodeName, ArrayList<SensorModel> sensors) {
        this.nodeName = nodeName;
        this.sensors = sensors;
    }

    public void setSensors(List<SensorModel> sensors) {
//        if (this.sensors == null || this.sensors.size() == 0) {
//
//            return;
//        }
        this.sensors = sensors;
    }
}
