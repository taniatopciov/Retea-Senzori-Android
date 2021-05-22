package com.example.retea_senzori_android.models;

import java.util.ArrayList;
import java.util.List;

public class NodeModel {

    public String nodeName;
    public String connectedBluetoothDevice;
    public String lastUpdate;
    public List<Sensor> sensors;

    public NodeModel(){

    }

    public NodeModel(String nodeName, Sensor s1, Sensor s2){
        this.nodeName = nodeName;
        sensors = new ArrayList<>();
        sensors.add(s1);
        sensors.add(s2);
    }
}
