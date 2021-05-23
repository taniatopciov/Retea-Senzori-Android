package com.example.retea_senzori_android.nodes;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.retea_senzori_android.bluetooth.protocol.BluetoothNodeProtocol;
import com.example.retea_senzori_android.bluetooth.protocol.BluetoothNodeProtocolSPPImpl;
import com.example.retea_senzori_android.databinding.NodeDetailsFragmentBinding;
import com.example.retea_senzori_android.di.Injectable;
import com.example.retea_senzori_android.di.ServiceLocator;
import com.example.retea_senzori_android.models.NodeModel;
import com.example.retea_senzori_android.models.SensorModel;
import com.example.retea_senzori_android.nodes.factory.Node;
import com.example.retea_senzori_android.nodes.factory.NodeFactory;
import com.example.retea_senzori_android.services.nodes.NodeService;
import com.example.retea_senzori_android.utils.UIRunner;
import com.google.android.material.snackbar.Snackbar;

import java.util.Arrays;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

public class NodeDetailsFragment extends Fragment {

    @Injectable
    private NodeService nodeService;

    @Injectable
    private UIRunner uiRunner;

    private static final int REQUEST_ENABLE_BT = 1;

    private BluetoothNodeProtocol bluetoothNodeProtocol;

    private NodeDetailsViewModel mViewModel;
    private NodeDetailsFragmentBinding binding;

    public NodeDetailsFragment() {
        ServiceLocator.getInstance().inject(this);
    }

    public static NodeDetailsFragment newInstance() {
        return new NodeDetailsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = NodeDetailsFragmentBinding.inflate(inflater, container, false);
        mViewModel = new ViewModelProvider(requireActivity()).get(NodeDetailsViewModel.class);

        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) {
            Snackbar.make(binding.idRVSensor, "Bluetooth Not Supported", Snackbar.LENGTH_LONG).show();
            return binding.getRoot();
        }
        // todo check if bluetooth is turned off
//        if (!bluetoothAdapter.isEnabled()) {
//            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
//            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
//        } else {
//        }

        NodeModel nodeModel = NodeDetailsFragmentArgs.fromBundle(getArguments()).getNodeModel();
        mViewModel.setNode(NodeFactory.fromModel(nodeModel));

        Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
        Optional<BluetoothDevice> bluetoothDeviceOptional = pairedDevices.stream().filter(device -> device.getName().equals(nodeModel.connectedBluetoothDevice))
                .findFirst();
        if (!bluetoothDeviceOptional.isPresent()) {
            Snackbar.make(binding.idRVSensor, "Bluetooth Device not found", Snackbar.LENGTH_LONG).show();
            return binding.getRoot();
        }
        mViewModel.setBluetoothDevice(bluetoothDeviceOptional.get());

        SensorAdapter sensorAdapter = new SensorAdapter(uiRunner);

        mViewModel.getBluetoothDevice().observe(getViewLifecycleOwner(), bluetoothDevice -> {
            bluetoothNodeProtocol.connect(bluetoothDevice, sdCardErrors -> System.err.println("SDCard Error " + sdCardErrors));
        });
        mViewModel.getNode().observe(getViewLifecycleOwner(), node -> {
            binding.testId.setText(node.getNodeName());
            sensorAdapter.setSensors(node.getSensors());
        });

        binding.idRVSensor.setAdapter(sensorAdapter);
        binding.idRVSensor.setLayoutManager(new LinearLayoutManager(getContext()));

        bluetoothNodeProtocol = new BluetoothNodeProtocolSPPImpl(deviceName -> {
            Snackbar.make(binding.idRVSensor, "Connected to " + deviceName, Snackbar.LENGTH_LONG)
                    .show();
        });

        binding.liveDataButton.setOnClickListener(view -> bluetoothNodeProtocol.readLiveData(sensorLogData -> {
            // todo add cyclic reads
            Node node = mViewModel.getNode().getValue();
            if (node != null) {
                node.updateSensorValue(sensorLogData.sensorType, sensorLogData.value);
            }
        }));

        binding.logDataButton.setOnClickListener(view -> bluetoothNodeProtocol.readSensorCount(count -> {
            bluetoothNodeProtocol.readSensorTypes(count, sensorTypes -> {
                mViewModel.setSensors(sensorTypes);

                nodeModel.setSensors(Arrays.stream(sensorTypes).map(SensorModel::new).collect(Collectors.toList()));
                nodeService.updateNode(nodeModel).subscribe(System.out::println);
            });
        }));

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        bluetoothNodeProtocol.disconnect();
        bluetoothNodeProtocol = null;
    }
}