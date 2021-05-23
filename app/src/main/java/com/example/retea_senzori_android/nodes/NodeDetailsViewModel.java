package com.example.retea_senzori_android.nodes;

import com.example.retea_senzori_android.models.SensorModel;

import java.util.List;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class NodeDetailsViewModel extends ViewModel {
    public MutableLiveData<String> node_name = new MutableLiveData<>();
    public MutableLiveData<List<SensorModel>> sensors = new MutableLiveData<>();
}