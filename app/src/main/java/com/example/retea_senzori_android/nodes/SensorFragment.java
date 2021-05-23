package com.example.retea_senzori_android.nodes;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.retea_senzori_android.R;
import com.example.retea_senzori_android.databinding.SensorFragmentBinding;
import com.example.retea_senzori_android.models.NodeModel;
import com.example.retea_senzori_android.models.SensorModel;

public class SensorFragment extends Fragment {

    private SensorViewModel mViewModel;

    private SensorFragmentBinding binding;

    public static SensorFragment newInstance() {
        return new SensorFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = SensorFragmentBinding.inflate(inflater, container, false);
        mViewModel = new ViewModelProvider(requireActivity()).get(SensorViewModel.class);

        SensorModel sensorModel = SensorFragmentArgs.fromBundle(getArguments()).getSensorModel();
        mViewModel.setSensorType(sensorModel.sensorType);
        mViewModel.getSensorType().observe(getViewLifecycleOwner(), sensorType -> binding.sensorType.setText(sensorType.toString()));

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