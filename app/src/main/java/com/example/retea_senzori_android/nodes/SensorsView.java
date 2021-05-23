package com.example.retea_senzori_android.nodes;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.retea_senzori_android.databinding.NodeDetailsFragmentBinding;
import com.example.retea_senzori_android.di.Injectable;
import com.example.retea_senzori_android.di.ServiceLocator;
import com.example.retea_senzori_android.models.SensorModel;
import com.example.retea_senzori_android.sensor.SensorType;
import com.example.retea_senzori_android.utils.UIRunner;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

public class SensorsView extends Fragment {

    @Injectable
    private UIRunner uiRunner;

    private NodeDetailsFragmentBinding binding;
    private ArrayList<SensorModel> sensorArray;

    public SensorsView() {
        ServiceLocator.getInstance().inject(this);
    }

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = NodeDetailsFragmentBinding.inflate(inflater, container, false);
        sensorArray = new ArrayList<>();
        sensorArray.add(new SensorModel(SensorType.GAS_SENSOR));
        sensorArray.add(new SensorModel(SensorType.RAIN_SENSOR));
        binding.idRVSensor.setAdapter(new SensorAdapter(uiRunner));
        binding.idRVSensor.setLayoutManager(new LinearLayoutManager(getContext()));

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
