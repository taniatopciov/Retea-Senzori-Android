package com.example.retea_senzori_android.nodes;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.retea_senzori_android.R;
import com.example.retea_senzori_android.databinding.NodeAllDataFragmentBinding;
import com.example.retea_senzori_android.di.Injectable;
import com.example.retea_senzori_android.di.ServiceLocator;
import com.example.retea_senzori_android.models.NodeModel;
import com.example.retea_senzori_android.sensor.SensorLogData;
import com.example.retea_senzori_android.sensor.SensorType;
import com.example.retea_senzori_android.services.logs.LogsService;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class NodeAllDataFragment extends Fragment {

    @Injectable
    private LogsService logsService;

    private NodeAllDataFragmentBinding binding;

    public NodeAllDataFragment() {
        ServiceLocator.getInstance().inject(this);
    }


    public static NodeAllDataFragment newInstance() {
        return new NodeAllDataFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = NodeAllDataFragmentBinding.inflate(inflater, container, false);

        NodeModel nodeModel = NodeAllDataFragmentArgs.fromBundle(getArguments()).getNodeModel();

        binding.bluetoothDeviceAllData.setText(nodeModel.connectedBluetoothDevice);
        binding.nodeNameAllData.setText(nodeModel.nodeName);

        if (nodeModel.logFileId == null || nodeModel.logFileId.isEmpty()) {
            return binding.getRoot();
        }

        logsService.getLogFromId(nodeModel.logFileId).subscribe(sensorLogFile -> {
            if (sensorLogFile == null) {
                System.out.println("No logs");
                return;
            }

            sensorLogFile.sensorLogs.forEach((sensorData) -> {
                for (SensorLogData data : sensorData.getLogData()) {

                    if (data.sensorType == SensorType.NO_TYPE) {
                        continue;
                    }

                    TableRow tableRow = new TableRow(getContext());

                    TextView timeTextView = new TextView(getContext());
                    TextView sensorTypeTextView = new TextView(getContext());
                    TextView valueTextView = new TextView(getContext());

                    timeTextView.setText(String.valueOf(data.time));
                    timeTextView.setGravity(Gravity.CENTER);

                    sensorTypeTextView.setText(data.sensorType.toString());
                    sensorTypeTextView.setGravity(Gravity.CENTER);

                    valueTextView.setText(String.valueOf(data.value));
                    valueTextView.setGravity(Gravity.CENTER);

                    tableRow.addView(timeTextView);
                    tableRow.addView(sensorTypeTextView);
                    tableRow.addView(valueTextView);

                    binding.logsTable.addView(tableRow);
                }
            });
        });

        return binding.getRoot();
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
