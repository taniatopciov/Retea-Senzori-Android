package com.example.retea_senzori_android.nodes;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.retea_senzori_android.R;
import com.example.retea_senzori_android.databinding.NodeDetailsFragmentBinding;
import com.example.retea_senzori_android.databinding.NodesLayoutFragmentBinding;
import com.example.retea_senzori_android.models.NodeModel;

public class NodeDetailsFragment extends Fragment {

    private NodeDetailsViewModel mViewModel;
    private NodeDetailsFragmentBinding binding;

    public static NodeDetailsFragment newInstance() {
        return new NodeDetailsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = NodeDetailsFragmentBinding.inflate(inflater, container, false);
        mViewModel = new ViewModelProvider(requireActivity()).get(NodeDetailsViewModel.class);
        mViewModel.node_name.observe(getViewLifecycleOwner(), name -> binding.testId.setText(name));

        NodeModel nodeModel = NodeDetailsFragmentArgs.fromBundle(getArguments()).getNodeModel();
        mViewModel.node_name.setValue(nodeModel.nodeName);

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

}