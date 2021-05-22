package com.example.retea_senzori_android.services;

import com.example.retea_senzori_android.models.NodeModel;
import com.example.retea_senzori_android.observables.Subject;

import java.util.List;

public interface NodeService {
    Subject<Boolean> addNode(NodeModel nodeModel);

    Subject<Boolean> updateNode(String bluetoothDeviceName, NodeModel nodeModel);

    Subject<List<NodeModel>> getAllNodes();

    NodeModel findNodeByDeviceName(String bluetoothDeviceName);
}
