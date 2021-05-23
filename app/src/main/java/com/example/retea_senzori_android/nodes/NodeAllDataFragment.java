package com.example.retea_senzori_android.nodes;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.retea_senzori_android.R;
import com.example.retea_senzori_android.databinding.NodeAllDataFragmentBinding;
import com.example.retea_senzori_android.databinding.SensorFragmentBinding;
import com.example.retea_senzori_android.models.NodeModel;
import com.example.retea_senzori_android.models.SensorModel;

public class NodeAllDataFragment extends Fragment {
    private NodeAllDataFragmentBinding binding;

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
