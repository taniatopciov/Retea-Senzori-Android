package com.example.retea_senzori_android.nodes;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
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
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.retea_senzori_android.R;
import com.example.retea_senzori_android.bluetooth.protocol.BluetoothNodeProtocol;
import com.example.retea_senzori_android.bluetooth.protocol.BluetoothNodeProtocolSPPImpl;
import com.example.retea_senzori_android.databinding.NodeDetailsFragmentBinding;
import com.example.retea_senzori_android.di.Injectable;
import com.example.retea_senzori_android.di.ServiceLocator;
import com.example.retea_senzori_android.models.NodeModel;
import com.example.retea_senzori_android.models.SensorModel;
import com.example.retea_senzori_android.nodes.factory.Node;
import com.example.retea_senzori_android.nodes.factory.NodeFactory;
import com.example.retea_senzori_android.nodes.renameNodePopup.RenameNodeDialogFragment;
import com.example.retea_senzori_android.services.nodes.NodeService;
import com.example.retea_senzori_android.utils.runners.CyclicRunner;
import com.example.retea_senzori_android.utils.runners.UIRunner;
import com.google.android.material.snackbar.Snackbar;

import java.util.Arrays;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class NodeDetailsFragment extends Fragment {

    @Injectable
    private NodeService nodeService;

    @Injectable
    private UIRunner uiRunner;

    private static final int REQUEST_ENABLE_BT = 1;
    private static final int RENAME_NODE_DIALOG_REQUEST_CODE = 100;

    private BluetoothNodeProtocol bluetoothNodeProtocol;

    private NodeDetailsViewModel mViewModel;
    private NodeDetailsFragmentBinding binding;
    private CyclicRunner cyclicRunner;

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

        NodeModel nodeModel = NodeDetailsFragmentArgs.fromBundle(getArguments()).getNodeModel();
        mViewModel.setNode(NodeFactory.fromModel(nodeModel));

        SensorAdapter sensorAdapter = new SensorAdapter(uiRunner);

        mViewModel.getBluetoothDevice().observe(getViewLifecycleOwner(), bluetoothDevice -> {
            bluetoothNodeProtocol.connect(bluetoothDevice, sdCardErrors -> System.err.println("SDCard Error " + sdCardErrors));
        });
        mViewModel.getNode().observe(getViewLifecycleOwner(), node -> {
            binding.nodeNameNodePage.setText(node.getNodeName());
            binding.bluetoothDeviceNodePage.setText(node.getConnectedBluetoothDevice());
            sensorAdapter.setSensors(node.getSensors());
        });

        binding.idRVSensor.setAdapter(sensorAdapter);
        binding.idRVSensor.setLayoutManager(new LinearLayoutManager(getContext()));

        binding.changeNodeNameButton.setOnClickListener(view -> {
            RenameNodeDialogFragment renameNodeDialogFragment = RenameNodeDialogFragment.newInstance(binding.nodeNameNodePage.getText().toString(), newName -> {
                nodeModel.nodeName = newName;
                mViewModel.setName(newName);
                nodeService.updateNode(nodeModel).subscribe(System.out::println);
            });
            renameNodeDialogFragment.setTargetFragment(this, RENAME_NODE_DIALOG_REQUEST_CODE);
            renameNodeDialogFragment.show(getParentFragmentManager(), "rename_node_dialog");
        });

        binding.allDataButton.setOnClickListener(view -> {
            Navigation.findNavController(view).navigate(NodeDetailsFragmentDirections.navigateToAllData(nodeModel));
        });

        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) {
            Snackbar.make(binding.nodePageId, "Bluetooth Not Supported", Snackbar.LENGTH_LONG).show();
            return binding.getRoot();
        }
        // todo check if bluetooth is turned off
//        if (!bluetoothAdapter.isEnabled()) {
//            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
//            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
//        } else {
//        }

        Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
        Optional<BluetoothDevice> bluetoothDeviceOptional = pairedDevices.stream().filter(device -> device.getName().equals(nodeModel.connectedBluetoothDevice))
                .findFirst();
        if (!bluetoothDeviceOptional.isPresent()) {
            Snackbar.make(binding.idRVSensor, "Bluetooth Device not found", Snackbar.LENGTH_LONG).show();
            return binding.getRoot();
        }
        mViewModel.setBluetoothDevice(bluetoothDeviceOptional.get());


        bluetoothNodeProtocol = new BluetoothNodeProtocolSPPImpl(deviceName -> {
            Snackbar.make(binding.idRVSensor, "Connected to " + deviceName, Snackbar.LENGTH_SHORT)
                    .show();
        });

        binding.liveDataButton.setOnClickListener(view -> {
            if (cyclicRunner != null) {
                binding.liveDataButton.setText("Live Data");
                stopLiveDataRead();
            } else {
                binding.liveDataButton.setText("Stop");
                startLiveDataRead();
            }
        });

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
        if (bluetoothNodeProtocol != null) {
            bluetoothNodeProtocol.disconnect();
            bluetoothNodeProtocol = null;
        }
        stopLiveDataRead();
    }

    private void startLiveDataRead() {
        cyclicRunner = new CyclicRunner(1000);
        cyclicRunner.run(() ->
                bluetoothNodeProtocol.readLiveData(sensorLogData -> {
                    Node node = mViewModel.getNode().getValue();
                    if (node != null) {
                        node.updateSensorValue(sensorLogData.sensorType, sensorLogData.value);
                    }
                }));
    }

    private void stopLiveDataRead() {
        if (cyclicRunner != null) {
            cyclicRunner.stop();
            cyclicRunner = null;
        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }
}