package com.example.retea_senzori_android.nodes;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
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
import com.example.retea_senzori_android.services.nodes.NodeService;
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
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = NodeDetailsFragmentBinding.inflate(inflater, container, false);
        mViewModel = new ViewModelProvider(requireActivity()).get(NodeDetailsViewModel.class);

        SensorAdapter sensorAdapter = new SensorAdapter();
        mViewModel.node_name.observe(getViewLifecycleOwner(), name -> binding.testId.setText(name));
        mViewModel.sensors.observe(getViewLifecycleOwner(), sensorAdapter::setSensors);

        binding.idRVSensor.setAdapter(sensorAdapter);
        binding.idRVSensor.setLayoutManager(new LinearLayoutManager(getContext()));

        NodeModel nodeModel = NodeDetailsFragmentArgs.fromBundle(getArguments()).getNodeModel();
        mViewModel.node_name.setValue(nodeModel.nodeName);
        mViewModel.sensors.setValue(nodeModel.sensors);

        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        bluetoothNodeProtocol = new BluetoothNodeProtocolSPPImpl(deviceName -> {
            Snackbar.make(binding.idRVSensor, "Connected to " + deviceName, Snackbar.LENGTH_LONG)
                    .show();

            bluetoothNodeProtocol.readSensorCount(count -> {
                System.out.println(count);
                bluetoothNodeProtocol.readSensorTypes(count, sensorTypes -> {
                    nodeModel.setSensors(Arrays.stream(sensorTypes).map(SensorModel::new).collect(Collectors.toList()));
                    mViewModel.sensors.postValue(nodeModel.sensors);
                    nodeService.updateNode(nodeModel).subscribe(System.out::println);
                });
            });
        });

        if (bluetoothAdapter == null) {
            Snackbar.make(binding.idRVSensor, "Bluetooth Not Supported", Snackbar.LENGTH_LONG).show();
        } else {
            if (!bluetoothAdapter.isEnabled()) {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            } else {


                Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
                Optional<BluetoothDevice> bluetoothDeviceOptional = pairedDevices.stream().filter(device -> device.getName().equals(nodeModel.connectedBluetoothDevice))
                        .findFirst();
                bluetoothDeviceOptional.ifPresent(device -> bluetoothNodeProtocol.connect(device, sdCardErrors -> System.err.println("SDCard Error " + sdCardErrors)));


//
//                binding.requestSensorTypesButton.setOnClickListener(view -> {
////                    bluetoothNodeProtocol.sendCommand(BluetoothNodeProtocolSPPImpl.CONNECT_STRING);
////                    bluetoothNodeProtocol.sendCommand(BluetoothNodeProtocolSPPImpl.REQUEST_SENSOR_COUNT_STRING);
////                    bluetoothNodeProtocol.sendCommand(BluetoothNodeProtocolSPPImpl.REQUEST_SENSOR_TYPES_STRING);
//                });
//
//                binding.requestDataButton.setOnClickListener(view -> {
////                    bluetoothNodeProtocol.sendCommand(BluetoothNodeProtocolSPPImpl.REPLAY_DATA_FROM_CURRENT_LOG_STRING);
//                });
//
//                IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
//                getActivity().registerReceiver(receiver, filter);
            }
        }

        binding.liveDataButton.setOnClickListener(view -> {
            bluetoothNodeProtocol.readLiveData(sensorLogData -> {
                System.out.println(sensorLogData.sensorType + " " + sensorLogData.value);
            });
        });


        binding.logDataButton.setOnClickListener(view -> {
            bluetoothNodeProtocol.readSensorCount(count -> {
                System.out.println(count);
                bluetoothNodeProtocol.readSensorTypes(count, sensorTypes -> {
                    nodeModel.setSensors(Arrays.stream(sensorTypes).map(SensorModel::new).collect(Collectors.toList()));
                    mViewModel.sensors.postValue(nodeModel.sensors);
                    nodeService.updateNode(nodeModel).subscribe(System.out::println);
                });
            });
        });


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