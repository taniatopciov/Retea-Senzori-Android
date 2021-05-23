package com.example.retea_senzori_android.nodes;

import com.example.retea_senzori_android.models.NodeModel;

import java.util.List;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class HomePageNodesViewModel extends ViewModel {
    private final MutableLiveData<List<NodeModel>> nodeModels;

    public HomePageNodesViewModel() {
        nodeModels = new MutableLiveData<>();
    }

    public void setNodeModels(List<NodeModel> nodeModels) {
        this.nodeModels.setValue(nodeModels);
    }

    public MutableLiveData<List<NodeModel>> getNodeModels() {
        return nodeModels;
    }
}
