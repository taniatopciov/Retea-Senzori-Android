package com.example.retea_senzori_android.nodes;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.retea_senzori_android.R;
import com.example.retea_senzori_android.databinding.LoginFragmentBinding;
import com.example.retea_senzori_android.databinding.NodesLayoutFragmentBinding;

import java.util.ArrayList;

public class NodesView extends Fragment {

    private NodesLayoutFragmentBinding binding;
    private ArrayList<NodeModel> nodeArray;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = NodesLayoutFragmentBinding.inflate(inflater, container, false);
        nodeArray = new ArrayList<>();
        //nodeArray.add(new NodeModel("Node1", "Gas", "Rain"));
        //nodeArray.add(new NodeModel("Funny Node", "Humidity", "Rain"));
        //nodeArray.add(new NodeModel("My Node", "Rain", "Temperature"));
        //nodeArray.add(new NodeModel("Stolen Node", "Gas", "Rain"));
        binding.idRVNode.setAdapter(new NodeAdapter(getContext(), nodeArray));
        binding.idRVNode.setLayoutManager(new LinearLayoutManager(getContext()));

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
