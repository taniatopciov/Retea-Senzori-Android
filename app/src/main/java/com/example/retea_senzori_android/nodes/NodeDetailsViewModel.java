package com.example.retea_senzori_android.nodes;

import android.bluetooth.BluetoothDevice;

import com.example.retea_senzori_android.nodes.factory.Node;
import com.example.retea_senzori_android.sensor.SensorType;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class NodeDetailsViewModel extends ViewModel {
    private final MutableLiveData<BluetoothDevice> bluetoothDevice = new MutableLiveData<>();
    private final MutableLiveData<Node> node = new MutableLiveData<>();

    public void setBluetoothDevice(BluetoothDevice bluetoothDevice) {
        this.bluetoothDevice.postValue(bluetoothDevice);
    }

    public void setNode(Node node) {
        this.node.postValue(node);
    }

    public MutableLiveData<BluetoothDevice> getBluetoothDevice() {
        return bluetoothDevice;
    }

    public MutableLiveData<Node> getNode() {
        return node;
    }

    public void setSensors(SensorType[] sensorTypes) {
        Node n = node.getValue();
        if (n != null) {

            n.setSensors(sensorTypes);
            setNode(n);
        }
    }

    public void setName(String newName) {
        Node n = node.getValue();
        if (n != null) {

            n.setNodeName(newName);
            setNode(n);
        }
    }
}