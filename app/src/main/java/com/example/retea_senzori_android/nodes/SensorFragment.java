package com.example.retea_senzori_android.nodes;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.retea_senzori_android.R;
import com.example.retea_senzori_android.authentication.service.AuthenticationService;
import com.example.retea_senzori_android.databinding.SensorFragmentBinding;
import com.example.retea_senzori_android.di.Injectable;
import com.example.retea_senzori_android.di.ServiceLocator;
import com.example.retea_senzori_android.models.NodeModel;
import com.example.retea_senzori_android.models.SensorModel;
import com.example.retea_senzori_android.services.logs.LogsService;
import com.example.retea_senzori_android.services.logs.impl.LogsServiceImpl;

public class SensorFragment extends Fragment {

    @Injectable
    LogsService logsService;


    private SensorViewModel mViewModel;

    private SensorFragmentBinding binding;

    public SensorFragment(){
        ServiceLocator.getInstance().inject(this);
    }

    public static SensorFragment newInstance() {
        return new SensorFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = SensorFragmentBinding.inflate(inflater, container, false);
        mViewModel = new ViewModelProvider(requireActivity()).get(SensorViewModel.class);

        SensorModel sensorModel = SensorFragmentArgs.fromBundle(getArguments()).getSensorModel();
        logsService.getLogFromId(sensorModel.logFileId).subscribe( sensorLogFile -> {
            sensorLogFile.sensorLogs.forEach( (sensorData) -> {
                sensorData.getLogData().forEach( (data) -> {
                    TableRow tr = new TableRow(getContext());
                    TextView text1 = new TextView(getContext());
                    TextView text2 = new TextView(getContext());

                    text1.setText(String.valueOf(data.time));
                    text1.setGravity(Gravity.CENTER);
                    text2.setText(String.valueOf(data.value));
                    text2.setGravity(Gravity.CENTER);

                    tr.addView(text1);
                    tr.addView(text2);
                    binding.logsTable.addView(tr);
                });
            });
        });
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

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

}