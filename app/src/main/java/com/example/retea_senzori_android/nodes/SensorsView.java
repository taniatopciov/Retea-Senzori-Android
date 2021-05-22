package com.example.retea_senzori_android.nodes;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.retea_senzori_android.databinding.NodeDetailsFragmentBinding;
import com.example.retea_senzori_android.models.Sensor;

import java.util.ArrayList;

public class SensorsView extends Fragment {

    private NodeDetailsFragmentBinding binding;
    private ArrayList<Sensor> sensorArray;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = NodeDetailsFragmentBinding.inflate(inflater, container, false);
        sensorArray = new ArrayList<>();
        sensorArray.add(new Sensor("gaz"));
        sensorArray.add(new Sensor("rain"));
        binding.idRVSensor.setAdapter(new SensorAdapter(getContext(), sensorArray));
        binding.idRVSensor.setLayoutManager(new LinearLayoutManager(getContext()));

        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
