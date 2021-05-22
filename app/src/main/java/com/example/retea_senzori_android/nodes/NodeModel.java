package com.example.retea_senzori_android.nodes;

import java.util.ArrayList;
import java.util.List;

public class NodeModel {
    public String node_name;
    public ArrayList<SensorModel> sensorArray;

    public NodeModel(String node_name, ArrayList<SensorModel> sensorArray){
        this.node_name = node_name;
        this.sensorArray = sensorArray;
    }

    public void addSensor(SensorModel sensor){
        sensorArray.add(sensor);
    }
}
