package com.example.retea_senzori_android.nodes;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.retea_senzori_android.bluetooth.ui.PairedBluetoothDevicesDialogFragment;
import com.example.retea_senzori_android.databinding.NodesLayoutFragmentBinding;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

public class NodesView extends Fragment {

    private static final int PAIRED_BT_DEVICES_REQUEST_CODE = 310;

    private NodesLayoutFragmentBinding binding;
    private ArrayList<NodeModel> nodeArray;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = NodesLayoutFragmentBinding.inflate(inflater, container, false);

        binding.addNodeFabButton.setOnClickListener(view -> {
            PairedBluetoothDevicesDialogFragment pairedBluetoothDevicesDialogFragment = PairedBluetoothDevicesDialogFragment.newInstance(device -> {
                // TODO add Node Service

                System.out.println(device.getName());

//                bluetoothNodeProtocol.connect(device, sdCardErrors -> System.err.println("SDCard Error " + sdCardErrors));
            });
            pairedBluetoothDevicesDialogFragment.setTargetFragment(this, PAIRED_BT_DEVICES_REQUEST_CODE);
            pairedBluetoothDevicesDialogFragment.show(getParentFragmentManager(), "paired_bt_devices_fragment");
        });

        nodeArray = new ArrayList<>();
        ArrayList<SensorModel> sensors = new ArrayList<>();
        sensors.add(new SensorModel("Gas"));
        nodeArray.add(new NodeModel("Stolen Node", sensors));
        binding.idRVNode.setAdapter(new NodeAdapter(getContext(), nodeArray));
        binding.idRVNode.setLayoutManager(new LinearLayoutManager(getContext()));

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
