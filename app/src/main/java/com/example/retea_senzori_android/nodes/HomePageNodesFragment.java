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
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.retea_senzori_android.R;
import com.example.retea_senzori_android.bluetooth.ui.PairedBluetoothDevicesDialogFragment;
import com.example.retea_senzori_android.databinding.HomePageNodesFragmentBinding;
import com.example.retea_senzori_android.di.Injectable;
import com.example.retea_senzori_android.di.ServiceLocator;
import com.example.retea_senzori_android.models.NodeModel;
import com.example.retea_senzori_android.services.nodes.NodeService;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class HomePageNodesFragment extends Fragment {

    @Injectable
    private NodeService nodeService;

    public HomePageNodesFragment() {
        ServiceLocator.getInstance().inject(this);
    }

    private static final int PAIRED_BT_DEVICES_REQUEST_CODE = 310;

    private HomePageNodesFragmentBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = HomePageNodesFragmentBinding.inflate(inflater, container, false);
        NodeAdapter nodeAdapter = new NodeAdapter();

        HomePageNodesViewModel viewModel = new ViewModelProvider(requireActivity()).get(HomePageNodesViewModel.class);
        viewModel.getNodeModels().observe(getViewLifecycleOwner(), nodeAdapter::addAllNodes);

        nodeService.getAllNodes().subscribe(viewModel::setNodeModels);

        binding.addNodeFabButton.setOnClickListener(view -> {
            PairedBluetoothDevicesDialogFragment pairedBluetoothDevicesDialogFragment = PairedBluetoothDevicesDialogFragment.newInstance(device -> {

                if (nodeService.findNodeByDeviceName(device.getName()) != null) {
                    System.out.println("Node exists in list, will not be added");
                    return;
                }

                NodeModel nodeModel = new NodeModel();
                nodeModel.connectedBluetoothDevice = device.getName();
                nodeModel.nodeName = nodeModel.connectedBluetoothDevice;
                nodeModel.sensors = new ArrayList<>();

                nodeService.addNode(nodeModel).subscribe(status -> {
                    if (!status) {
                        Snackbar.make(view, "Could not add node", Snackbar.LENGTH_SHORT)
                                .show();
                    } else {
                        nodeAdapter.addNode(nodeModel);
                    }
                });

//                bluetoothNodeProtocol.connect(device, sdCardErrors -> System.err.println("SDCard Error " + sdCardErrors));
            });
            pairedBluetoothDevicesDialogFragment.setTargetFragment(this, PAIRED_BT_DEVICES_REQUEST_CODE);
            pairedBluetoothDevicesDialogFragment.show(getParentFragmentManager(), "paired_bt_devices_fragment");
        });


        binding.idRVNode.setAdapter(nodeAdapter);
        binding.idRVNode.setLayoutManager(new LinearLayoutManager(getContext()));

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
