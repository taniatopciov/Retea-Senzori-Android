package com.example.retea_senzori_android.nodes;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.retea_senzori_android.databinding.NodeDetailsFragmentBinding;
import com.example.retea_senzori_android.models.SensorModel;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

public class SensorsView extends Fragment {

    private NodeDetailsFragmentBinding binding;
    private ArrayList<SensorModel> sensorArray;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = NodeDetailsFragmentBinding.inflate(inflater, container, false);
        sensorArray = new ArrayList<>();
        sensorArray.add(new SensorModel("gaz"));
        sensorArray.add(new SensorModel("rain"));
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
