package com.example.retea_senzori_android.bluetooth.ui;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.retea_senzori_android.bluetooth.protocol.BluetoothNodeProtocol;
import com.example.retea_senzori_android.bluetooth.protocol.BluetoothNodeProtocolSPPImpl;
import com.example.retea_senzori_android.databinding.FragmentBluetoothManagerBinding;
import com.google.android.material.snackbar.Snackbar;

import java.util.function.Consumer;

import androidx.fragment.app.Fragment;

public class BluetoothManagerFragment extends Fragment implements Consumer<BluetoothDevice> {

    public static final int PAIRED_BT_DEVICES_REQUEST_CODE = 300;

    private static final int REQUEST_ENABLE_BT = 1;

    private FragmentBluetoothManagerBinding binding;
    private BluetoothNodeProtocol bluetoothNodeProtocol;

    public BluetoothManagerFragment() {
    }

    public static BluetoothManagerFragment newInstance() {
        return new BluetoothManagerFragment();
    }

//    private final BroadcastReceiver receiver = new BroadcastReceiver() {
//        public void onReceive(Context context, Intent intent) {
//            String action = intent.getAction();
//            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
//                // Discovery has found a device. Get the BluetoothDevice
//                // object and its info from the Intent.
//                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
//                String deviceName = device.getName();
//                String deviceHardwareAddress = device.getAddress(); // MAC address
////                bluetoothPairedDevicesAdapter.addDevice(device);
//            }
//        }
//    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentBluetoothManagerBinding.inflate(inflater, container, false);
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        binding.getPairedDevicesButton.setOnClickListener(view -> {
            System.out.println("show paired devices");
            PairedBluetoothDevicesDialogFragment pairedBluetoothDevicesDialogFragment = PairedBluetoothDevicesDialogFragment.newInstance(this::accept);
            pairedBluetoothDevicesDialogFragment.setTargetFragment(this, PAIRED_BT_DEVICES_REQUEST_CODE);
            pairedBluetoothDevicesDialogFragment.show(getParentFragmentManager(), "paired_bt_devices_fragment");
        });

        bluetoothNodeProtocol = new BluetoothNodeProtocolSPPImpl(deviceName -> Snackbar.make(container, "Connected to " + deviceName, Snackbar.LENGTH_LONG).show());

        if (bluetoothAdapter == null) {
            Snackbar.make(container, "Bluetooth Not Supported", Snackbar.LENGTH_LONG).show();
        } else {
            if (!bluetoothAdapter.isEnabled()) {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            } else {


                binding.requestSensorTypesButton.setOnClickListener(view -> {
//                    bluetoothNodeProtocol.sendCommand(BluetoothNodeProtocolSPPImpl.CONNECT_STRING);
//                    bluetoothNodeProtocol.sendCommand(BluetoothNodeProtocolSPPImpl.REQUEST_SENSOR_COUNT_STRING);
//                    bluetoothNodeProtocol.sendCommand(BluetoothNodeProtocolSPPImpl.REQUEST_SENSOR_TYPES_STRING);
                });

                binding.requestDataButton.setOnClickListener(view -> {
//                    bluetoothNodeProtocol.sendCommand(BluetoothNodeProtocolSPPImpl.REPLAY_DATA_FROM_CURRENT_LOG_STRING);
                });

//                IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
//                getActivity().registerReceiver(receiver, filter);
            }
        }


        return binding.getRoot();
    }

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        switch (resultCode) {
//            case REQUEST_ENABLE_BT: {
////                pairedDevices = bluetoothAdapter.getBondedDevices();
//            }
//            break;
//        }
//    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
//        getActivity().unregisterReceiver(receiver);
    }

    @Override
    public void accept(BluetoothDevice device) {
        System.out.println(device.getName());
        binding.connectedDeviceNameTextView.setText(device.getName());

        bluetoothNodeProtocol.connect(device, sdCardErrors -> System.err.println("SDCard Error " + sdCardErrors));
    }
}