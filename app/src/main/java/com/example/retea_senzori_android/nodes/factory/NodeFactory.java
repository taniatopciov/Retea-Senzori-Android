package com.example.retea_senzori_android.nodes.factory;

import com.example.retea_senzori_android.models.NodeModel;
import com.example.retea_senzori_android.models.SensorModel;

public class NodeFactory {
    public static Node fromModel(NodeModel nodeModel) {
        Node node = new Node(nodeModel.nodeName, nodeModel.connectedBluetoothDevice, nodeModel.lastUpdate);

        if (nodeModel.sensors != null) {
            for (SensorModel sensorModel : nodeModel.sensors) {
                node.addSensor(SensorFactory.fromModel(sensorModel));
            }
        }

        return node;
    }
}
