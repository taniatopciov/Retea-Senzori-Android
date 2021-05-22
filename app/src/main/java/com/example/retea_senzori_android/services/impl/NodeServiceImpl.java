package com.example.retea_senzori_android.services.impl;

import com.example.retea_senzori_android.models.NodeModel;
import com.example.retea_senzori_android.models.ProfileModel;
import com.example.retea_senzori_android.observables.Observer;
import com.example.retea_senzori_android.observables.Subject;
import com.example.retea_senzori_android.persistance.FirebaseRepository;
import com.example.retea_senzori_android.services.NodeService;

import java.util.HashMap;
import java.util.Map;

public class NodeServiceImpl implements NodeService, Observer<ProfileModel> {

    private static final String USER_DATA_COLLECTION_PATH = "users";

    private ProfileModel currentProfileModel;

    private final FirebaseRepository<ProfileModel> profileModelFirebaseRepository;

    public NodeServiceImpl(FirebaseRepository<ProfileModel> profileModelFirebaseRepository) {
        this.profileModelFirebaseRepository = profileModelFirebaseRepository;
    }

    @Override
    public Subject<Boolean> addNode(NodeModel nodeModel) {
        Subject<Boolean> subject = new Subject<>();
        if (currentProfileModel == null) {
            subject.setState(false);
        } else {
            currentProfileModel.nodes.add(nodeModel);
            updateData(subject);
        }

        return subject;
    }

    private void updateData(Subject<Boolean> subject) {
        Map<String, Object> map = new HashMap<>();
        map.put("nodes", currentProfileModel.nodes);
        String pathToDocument = USER_DATA_COLLECTION_PATH + "/" + currentProfileModel.id;
        profileModelFirebaseRepository.updateDocument(pathToDocument, map).subscribe(subject::setState);
    }

    @Override
    public Subject<Boolean> updateNode(String bluetoothDeviceName, NodeModel nodeModel) {
        Subject<Boolean> subject = new Subject<>();
        if (currentProfileModel == null) {
            subject.setState(false);
        } else {
            boolean nodeFound = false;

            for (NodeModel node : currentProfileModel.nodes) {
                if (node.connectedBluetoothDevice.equals(bluetoothDeviceName)) {
                    nodeFound = true;
                    node.nodeName = nodeModel.nodeName;
                    node.lastUpdate = nodeModel.lastUpdate;
                    node.sensors = nodeModel.sensors;
                    break;
                }
            }

            if (nodeFound) {
                updateData(subject);
            }
        }

        return subject;
    }

    @Override
    public void observe(ProfileModel value) {
        currentProfileModel = value;
    }
}
