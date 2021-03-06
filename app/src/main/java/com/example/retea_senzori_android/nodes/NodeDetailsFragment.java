package com.example.retea_senzori_android.nodes;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.retea_senzori_android.R;
import com.example.retea_senzori_android.bluetooth.protocol.BluetoothNodeProtocol;
import com.example.retea_senzori_android.bluetooth.protocol.BluetoothNodeProtocolSPPImpl;
import com.example.retea_senzori_android.databinding.NodeDetailsFragmentBinding;
import com.example.retea_senzori_android.di.Injectable;
import com.example.retea_senzori_android.di.ServiceLocator;
import com.example.retea_senzori_android.models.NodeModel;
import com.example.retea_senzori_android.models.SensorLogFile;
import com.example.retea_senzori_android.models.SensorModel;
import com.example.retea_senzori_android.nodes.factory.Node;
import com.example.retea_senzori_android.nodes.factory.NodeFactory;
import com.example.retea_senzori_android.nodes.factory.NodeLogCreator;
import com.example.retea_senzori_android.nodes.readdata.ReadDataDialogFragment;
import com.example.retea_senzori_android.nodes.renameNodePopup.RenameNodeDialogFragment;
import com.example.retea_senzori_android.observables.Subject;
import com.example.retea_senzori_android.sensor.SensorLogData;
import com.example.retea_senzori_android.services.logs.LogsService;
import com.example.retea_senzori_android.services.nodes.NodeService;
import com.example.retea_senzori_android.utils.activity.ActivityForResultLauncher;
import com.example.retea_senzori_android.utils.runners.CyclicRunner;
import com.example.retea_senzori_android.utils.runners.UIRunner;
import com.google.android.material.snackbar.Snackbar;

import java.util.Arrays;
import java.util.Optional;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.stream.Collectors;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

public class NodeDetailsFragment extends Fragment {

    @Injectable
    private NodeService nodeService;

    @Injectable
    private UIRunner uiRunner;

    @Injectable
    private ActivityForResultLauncher activityForResultLauncher;

    @Injectable
    private LogsService logsService;

    private static final int RENAME_NODE_DIALOG_REQUEST_CODE = 100;
    private static final int READ_DATA_DIALOG_REQUEST_CODE = 110;

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
        sensorAdapter.setNodeLogId(nodeModel.logFileId);

        mViewModel.getBluetoothDevice().observe(getViewLifecycleOwner(), bluetoothDevice -> {
            if (!bluetoothDevice.getName().equals(nodeModel.connectedBluetoothDevice)) {
                return;
            }
            if (bluetoothNodeProtocol != null) {
                bluetoothNodeProtocol.connect(bluetoothDevice, sdCardErrors -> System.err.println("SDCard Error " + sdCardErrors));
            }
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

            Subject<Void> readDataDialogSubject = new Subject<>();

            ReadDataDialogFragment readDataDialogFragment = ReadDataDialogFragment.newInstance(readDataDialogSubject);
            readDataDialogFragment.setTargetFragment(this, READ_DATA_DIALOG_REQUEST_CODE);
            readDataDialogFragment.show(getParentFragmentManager(), "read_data_dialog");
            final boolean[] called = {false};

            if (bluetoothNodeProtocol != null) {
                bluetoothNodeProtocol.readAllLogData(sensorDataLogFiles -> {
                    Node node = mViewModel.getNode().getValue();
                    if (node == null) {
                        return;
                    }

                    NodeLogCreator nodeLogCreator = new NodeLogCreator();

                    for (int i = 0; i < sensorDataLogFiles.size(); i++) {
                        nodeLogCreator.startLog();
                        for (int j = 0; j < sensorDataLogFiles.get(i).getLogData().size(); j++) {
                            SensorLogData sensorLogData = sensorDataLogFiles.get(i).getLogData().get(j);
                            nodeLogCreator.addSensorLogData(sensorLogData);
                        }
                        nodeLogCreator.endLog();
                    }

                    SensorLogFile sensorLogFile = nodeLogCreator.getSensorLogFile();

                    logsService.addLog(node.getNodeModel().logFileId, sensorLogFile).subscribe(logFileId -> {
                        NodeModel model = node.getNodeModel();
                        model.logFileId = logFileId;
                        sensorAdapter.setNodeLogId(logFileId);
                        nodeService.updateNode(model).subscribe(status -> {
                            readDataDialogSubject.setState(null);

                            if (!called[0]) {
                                called[0] = true;
                                Navigation.findNavController(view).navigate(NodeDetailsFragmentDirections.navigateToAllData(model));
                            }
                        });
                    });
                });
            } else {
                readDataDialogSubject.setState(null);
            }
        });

        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) {
            Snackbar.make(binding.nodePageId, "Bluetooth Not Supported", Snackbar.LENGTH_LONG).show();
            return binding.getRoot();
        }

        if (!bluetoothAdapter.isEnabled()) {
            bluetoothAdapter.enable();

            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            activityForResultLauncher.launch(enableBtIntent, result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    System.out.println("BT Enabled");

                    connectToNode(nodeModel, bluetoothAdapter);
                }
            });
        } else {
            if (!connectToNode(nodeModel, bluetoothAdapter)) {
                return binding.getRoot();
            }
        }

        binding.liveDataButton.setOnClickListener(view -> {
            if (bluetoothNodeProtocol == null) {
                Snackbar.make(binding.idRVSensor, "Not connected to node", Snackbar.LENGTH_SHORT)
                        .show();
                return;
            }
            if (cyclicRunner != null) {
                binding.liveDataButton.setText(R.string.live_data);
                stopLiveDataRead();
            } else {
                binding.liveDataButton.setText(R.string.stop);
                startLiveDataRead();
            }
        });

        binding.readNodeButton.setOnClickListener(view -> {
            if (bluetoothNodeProtocol == null) {
                Snackbar.make(binding.idRVSensor, "Node not connected! Trying to connect...", Snackbar.LENGTH_SHORT).show();
                connectToNode(nodeModel, bluetoothAdapter);
                return;
            }
            bluetoothNodeProtocol.readSensorCount(count -> {
                bluetoothNodeProtocol.readSensorTypes(count, sensorTypes -> {
                    mViewModel.setSensors(sensorTypes);
                    nodeModel.sensors = Arrays.stream(sensorTypes).map(SensorModel::new).collect(Collectors.toList());
                    nodeService.updateNode(nodeModel).subscribe(System.out::println);

//                    bluetoothNodeProtocol.setUnixTime();

//                    bluetoothNodeProtocol.readCurrentLogData().subscribe(sensorDataLogFile -> {
//                        for (int i = 0; i < sensorDataLogFile.getLogData().size(); i++) {
//                            SensorLogData sensorLogData = sensorDataLogFile.getLogData().get(i);
//                            System.out.println(sensorLogData);
//                        }
//
//                        bluetoothNodeProtocol.setUnixTime();
//                    });
                });
            });
        });
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
        cyclicRunner.run(() -> {
            if (bluetoothNodeProtocol == null) {
                return;
            }
            bluetoothNodeProtocol.readLiveData(sensorLogData -> {
                Node node = mViewModel.getNode().getValue();
                if (node != null) {
                    System.out.println(sensorLogData.value);
                    node.updateSensorValue(sensorLogData.sensorType, sensorLogData.value);
                }
            });
        });
    }

    private boolean connectToNode(NodeModel nodeModel, BluetoothAdapter bluetoothAdapter) {
        Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
        Optional<BluetoothDevice> bluetoothDeviceOptional = pairedDevices.stream().filter(device -> device.getName().equals(nodeModel.connectedBluetoothDevice))
                .findFirst();
        if (!bluetoothDeviceOptional.isPresent()) {
            Snackbar.make(binding.idRVSensor, "Bluetooth Device not found", Snackbar.LENGTH_LONG).show();
            return false;
        }
        mViewModel.setBluetoothDevice(bluetoothDeviceOptional.get());

        bluetoothNodeProtocol = new BluetoothNodeProtocolSPPImpl(deviceName -> {
            if (deviceName == null) {
                Snackbar.make(binding.idRVSensor, "Couldn't connect to " + nodeModel.connectedBluetoothDevice, Snackbar.LENGTH_SHORT)
                        .show();
                bluetoothNodeProtocol = null;
            } else {
                Snackbar.make(binding.idRVSensor, "Connected to " + deviceName, Snackbar.LENGTH_SHORT)
                        .show();

                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        if (bluetoothNodeProtocol != null) {
                            bluetoothNodeProtocol.setUnixTime();
                        }
                    }
                }, 500);
            }
        });
        return true;
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