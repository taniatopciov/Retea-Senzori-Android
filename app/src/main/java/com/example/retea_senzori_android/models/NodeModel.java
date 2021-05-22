package com.example.retea_senzori_android.models;

import java.util.ArrayList;
import java.util.List;

public class NodeModel {

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
}
